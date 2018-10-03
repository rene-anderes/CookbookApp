package android.anderes.org.cookbook.service;

import android.anderes.org.cookbook.AppConfiguration;
import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.anderes.org.cookbook.database.RecipeEntity;
import android.anderes.org.cookbook.repository.IngredientRepository;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.anderes.org.cookbook.repository.RecipeRepository;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSyncTask extends AsyncTask<Void, Void, Void> {

    private final RecipeAbstractRepository repository;
    private final AppConfiguration configuration;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public DatabaseSyncTask(@NonNull final RecipeAbstractRepository recipeAbstractRepository,
                            @NonNull final RecipeRepository recipeRepository,
                            @NonNull final IngredientRepository ingredientRepository,
                            @NonNull final AppConfiguration configuration) {
        this.repository = recipeAbstractRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.configuration = configuration;
    }

    @Override
    protected Void doInBackground(Void... Void) {

        try {
            final List<RecipeAbstractEntity> entities = repository.getRecipeCollectionFromRemote();
            repository.updateAllDataInDatabase(entities);
            if(configuration.isFullSync()) {
                for (RecipeAbstractEntity entity : entities) {
                    if(recipeRepository.isSyncNecessary(entity.getRecipeId(), entity.getLastUpdate())) {
                        recipeRepository.updateAllDataInDatabase(entity.getRecipeId());
                        ingredientRepository.updateAllDataInDatabase(entity.getRecipeId());
                    }
                }
            }
            final List<String> recipeIds = new ArrayList<>();
            for (RecipeAbstractEntity recipeAbstractEntity : entities) {
                recipeIds.add(recipeAbstractEntity.getRecipeId());
            }
            recipeRepository.deleteOrphan(recipeIds);
            ingredientRepository.deleteOrphan(recipeIds);

        } catch (IOException e) {
            Log.e("Sync", e.getMessage());
        }

        return null;
    }

}
