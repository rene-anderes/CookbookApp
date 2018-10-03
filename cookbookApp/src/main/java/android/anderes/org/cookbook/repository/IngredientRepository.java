package android.anderes.org.cookbook.repository;

import android.anderes.org.cookbook.database.IngredientDao;
import android.anderes.org.cookbook.database.IngredientEntity;
import android.anderes.org.cookbook.infrastructure.Ingredient;
import android.anderes.org.cookbook.infrastructure.Recipe;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
                for (Ingredient ingredient : data.getIngredients()) {
                    final IngredientEntity entity = new IngredientEntity();
                    entity.setRecipeId(data.getId());
                    entity.setComment(ingredient.getComment());
                    entity.setDescription(ingredient.getDescription());
                    entity.setPortion(ingredient.getPortion());
                    ingredientDao.insert(entity);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<IngredientEntity> data) {
                return data == null || data.size() == 0;
            }

            @NonNull
            @Override
            protected LiveData<List<IngredientEntity>> loadFromDb() {
                return ingredientDao.getIngredientsForRecipe(recipeId);
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
}
