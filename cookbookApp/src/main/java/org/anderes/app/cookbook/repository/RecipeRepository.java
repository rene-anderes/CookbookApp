package org.anderes.app.cookbook.repository;

import org.anderes.app.cookbook.database.RecipeDao;
import org.anderes.app.cookbook.database.RecipeEntity;
import org.anderes.app.cookbook.infrastructure.Recipe;
import org.anderes.app.cookbook.infrastructure.RecipeService;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Observable;
import retrofit2.HttpException;

public class RecipeRepository {
    private final RecipeService service;
    private final RecipeDao recipeDao;

    public RecipeRepository(@NonNull final RecipeService recipeService,
                            @NonNull final RecipeDao recipeDao) {
        this.service = recipeService;
        this.recipeDao = recipeDao;
    }

    public LiveData<Resource<RecipeEntity>> getRecipe(final String recipeId) {
        return new NetworkBoundResource<RecipeEntity, Recipe>() {

            @Override
            protected void saveCallResult(@NonNull Recipe data) {
                recipeDao.insert(mapToEntity(data));
            }

            @Override
            protected boolean shouldFetch(@Nullable RecipeEntity data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<RecipeEntity> loadFromDb() {
                return recipeDao.getRecipe(recipeId);
            }

            @NonNull
            @Override
            protected Observable<Recipe> createCall() {
                return service.getRecipe(recipeId);
            }
        }.getAsLiveData();
    }

    /**
     * Liefert ein Rezepte von der Remote Data Source.
     * Kein Update der lokalen Datenbank und die Methode wird synchron ausgeführt.
     */
    @NonNull
    public RecipeEntity getRecipeFromRemote(@NonNull final String recipeId) throws IOException {
        try {
            final Recipe recipe = service.getRecipe(recipeId).blockingSingle();
            return mapToEntity(recipe);
        } catch (HttpException | NoSuchElementException e) {
            throw new IOException(e.getMessage());
        }
    }

    @NonNull
    private RecipeEntity mapToEntity(@NonNull final Recipe data) {
        final RecipeEntity entity = new RecipeEntity();
        entity.setRecipeId(data.getId());
        entity.setTitle(data.getTitle());
        entity.setTags(data.getTags());
        entity.setAddingDate(data.getAddingDate());
        entity.setEditingDate(data.getEditingDate());
        entity.setRating(data.getRating());
        entity.setPreamble(data.getPreamble());
        entity.setPreparation(data.getPreparation());
        entity.setNoOfPeople(data.getNoOfPeople());
        return entity;
    }

    /**
     * Es werden die Daten aus der Datenbank gelesen.
     * Die Methode wird synchron aufgerufen.
     */
    @Nullable
    public RecipeEntity getRecipeFromDatabase(@NonNull final String recipeId) {
        return recipeDao.getRecipeById(recipeId);
    }

    /**
     * Speichert die Daten in der Datenbank. Existiert der Datensatz noch nicht
     * (anhand Primery Key) so wird er angelegt, sonst aktualisiert.
     */
    public void saveDataToDatabase(@NonNull final RecipeEntity entity) {
        final RecipeEntity databaseRecipe = recipeDao.getRecipeById(entity.getRecipeId());
        if (databaseRecipe == null) {
            recipeDao.insert(entity);
        } else {
            recipeDao.update(entity);
        }
    }

    /**
     * Überprüft, ob eine Synchronisation anhand des Aktualisierungsdatum
     * notwendig ist. Das Datum wird als long übergeben.
     * <p>Beachte: Existiert die Rezept-ID in der Datenbank nicht wird false zurück geliefert.</p>
     */
    public boolean isSyncNecessary(@NonNull final String recipeId, long updateDate) {
        return  recipeDao.isSyncNecessary(recipeId, updateDate);
    }

    /**
     *  Gibt an, ob ein Rezept mit der übergebenen ID existiert.
     */
    public boolean isExists(@NonNull final String recipeId) {
        return recipeDao.isExists(recipeId);
    }

    /**
     * Löscht alle Rezepte deren Id in der Liste NICHT vorkommen.
     */
    public void deleteOrphan(final List<String> recipeIds) {
        recipeDao.deleteOrphan(recipeIds);
    }

    public void updateAllDataInDatabase(String recipeId) throws IOException {
        final RecipeEntity recipe = getRecipeFromRemote(recipeId);
        saveDataToDatabase(recipe);
    }
}
