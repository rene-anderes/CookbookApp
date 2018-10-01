package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.database.RecipeEntity;
import android.anderes.org.cookbook.repository.RecipeRepository;
import android.anderes.org.cookbook.repository.Resource;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

public class ItemDetailViewModel extends ViewModel {

    private RecipeRepository repository;
    private LiveData<Resource<RecipeEntity>> recipe;

    public void setRepository(@NonNull final RecipeRepository repository) {
        this.repository = repository;
    }

    LiveData<Resource<RecipeEntity>> getRecipe(@NonNull final String recipeId) {
        if (recipe == null) {
            recipe = repository.getRecipe(recipeId);
        }
        return recipe;
    }
}
