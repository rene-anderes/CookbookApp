package android.anderes.org.cookbook.repository;

import android.anderes.org.cookbook.database.RecipeDao;
import android.anderes.org.cookbook.database.RecipeEntity;
import android.anderes.org.cookbook.infrastructure.Recipe;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import io.reactivex.Observable;

public class RecipeRepository {
    private final RecipeService service;
    private final RecipeDao recipeDao;

    public RecipeRepository(final RecipeService recipeService, final RecipeDao recipeDao) {
        this.service = recipeService;
        this.recipeDao = recipeDao;
    }

    public LiveData<Resource<RecipeEntity>> getRecipe(final String recipeId) {
        return new NetworkBoundResource<RecipeEntity, Recipe>() {

            @Override
            protected void saveCallResult(@NonNull Recipe data) {
                final RecipeEntity entity = new RecipeEntity();
                entity.setRecipeId(data.getId());
                entity.setTitle(data.getTitle());
                entity.setTags(data.getTags());
                recipeDao.update(entity);
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
}
