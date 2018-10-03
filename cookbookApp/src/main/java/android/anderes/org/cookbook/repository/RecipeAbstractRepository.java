package android.anderes.org.cookbook.repository;

import android.anderes.org.cookbook.database.RecipeAbstractDao;
import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.anderes.org.cookbook.infrastructure.RecipeAbstract;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.HttpException;

public class RecipeAbstractRepository {

    private final RecipeService service;
    private final RecipeAbstractDao recipeAbstractDao;

    public RecipeAbstractRepository(@NonNull final RecipeService service,
                                    @NonNull final RecipeAbstractDao recipeAbstractDao) {
        this.service = service;
        this.recipeAbstractDao = recipeAbstractDao;
    }

    public LiveData<Resource<List<RecipeAbstractEntity>>> getRecipes() {
        return new NetworkBoundResource<List<RecipeAbstractEntity>, List<RecipeAbstract>>() {

            @Override
            protected void saveCallResult(@NonNull List<RecipeAbstract> data) {
                recipeAbstractDao.updateData(mapToEntities(data));
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RecipeAbstractEntity> data) {
                return data == null || data.size() == 0;
            }

            @NonNull
            @Override
            protected LiveData<List<RecipeAbstractEntity>> loadFromDb() {
                return recipeAbstractDao.getAllRecipeAbstract();
            }

            @NonNull
            @Override
            protected Observable<List<RecipeAbstract>> createCall() {
                return service.getRecipeAbstract();
            }

        }.getAsLiveData();
    }

    /**
     * Liefert die Kurzform der Rezepte von der Remote Data Source.
     * Kein Update der lokalen Datenbank und die Methode wird synchron ausgeführt.
     */
    @NonNull
    public List<RecipeAbstractEntity> getRecipeCollectionFromRemote() throws IOException {
        try {
            final List<RecipeAbstract> recipes = service.getRecipeAbstract().blockingFirst(new ArrayList<>());
            return mapToEntities(recipes);
        } catch (HttpException e) {
            throw new IOException(e.getMessage());
        }

    }

    @NonNull
    private List<RecipeAbstractEntity> mapToEntities(@NonNull final List<RecipeAbstract> recipes) {
        final List<RecipeAbstractEntity> data = new ArrayList<>(recipes.size());
        for (RecipeAbstract r : recipes) {
            final RecipeAbstractEntity entity = new RecipeAbstractEntity();
            entity.setRecipeId(r.getId());
            entity.setTitle(r.getTitle());
            entity.setLastUpdate(r.getEditingDate());
            data.add(entity);
        }
        return data;
    }

    /**
     * Es werden alle Daten in der lokalen Datenbank gelöscht
     * und die übergebene Liste von Rezpten wird in die Datenbank geschrieben.
     * Diese Operationen Löschen und Schreiben werden in einer Transaktion und synchron ausgeführt.
     */
    public void updateAllDataInDatabase(@NonNull final List<RecipeAbstractEntity> recipes) {
        recipeAbstractDao.updateData(recipes);
    }

    /**
     * Es werden die Daten aus der Datenbank gelesen.
     * Die Methode wird synchron aufgerufen.
     */
    @NonNull
    public List<RecipeAbstractEntity> getRecipeCollectionFromDatabase() {
        return recipeAbstractDao.getRecipeAbstractCollection();
    }
}
