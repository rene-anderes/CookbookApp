package org.anderes.app.cookbook.repository;

import org.anderes.app.cookbook.database.IngredientDao;
import org.anderes.app.cookbook.database.IngredientEntity;
import org.anderes.app.cookbook.infrastructure.Ingredient;
import org.anderes.app.cookbook.infrastructure.Recipe;
import org.anderes.app.cookbook.infrastructure.RecipeService;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class IngredientRepository {

    private final RecipeService service;
    private final IngredientDao ingredientDao;


    public IngredientRepository(@NonNull final RecipeService recipeService,
                                @NonNull final IngredientDao ingredientDao) {
        this.service = recipeService;
        this.ingredientDao = ingredientDao;
    }

    public LiveData<Resource<List<IngredientEntity>>> getIngredients(String recipeId) {
        return new NetworkBoundResource<List<IngredientEntity>, Recipe>() {

            @Override
            protected void saveCallResult(@NonNull final Recipe data) {
                final List<IngredientEntity> list = mapToEntityCollection(data);
                ingredientDao.updateByRecipeId(data.getId(), list);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<IngredientEntity> data) {
                return data == null || data.size() == 0;
            }

            @NonNull
            @Override
            protected LiveData<List<IngredientEntity>> loadFromDb() {
                return ingredientDao.getIngredients(recipeId);
            }

            @NonNull
            @Override
            protected Observable<Recipe> createCall() {
                return service.getRecipe(recipeId);
            }
        }.getAsLiveData();
    }

    public int deleteOrphan(List<String> recipeIds) {
        ingredientDao.deleteOrphan(recipeIds);
        return ingredientDao.count();
    }

    public int updateAllDataInDatabase(String recipeId, List<IngredientEntity> ingredients) {
        return ingredientDao.updateByRecipeId(recipeId, ingredients);
    }

    public void updateAllDataInDatabase(String recipeId) {
        final Recipe recipe = service.getRecipe(recipeId).blockingSingle();
        updateAllDataInDatabase(recipeId, mapToEntityCollection(recipe));
    }

    private List<IngredientEntity> mapToEntityCollection(@NonNull final Recipe recipe) {
        final List<IngredientEntity> list = new ArrayList<>(recipe.getIngredients().size());
        for (Ingredient ingredient : recipe.getIngredients()) {
            final IngredientEntity entity = mapToEntity(recipe.getId(), ingredient);
            list.add(entity);
        }
        return list;
    }

    private IngredientEntity mapToEntity(String recipeId, Ingredient ingredient) {
        final IngredientEntity entity = new IngredientEntity();
        entity.setRecipeId(recipeId);
        entity.setComment(ingredient.getComment());
        entity.setDescription(ingredient.getDescription());
        entity.setPortion(ingredient.getPortion());
        return entity;
    }
}
