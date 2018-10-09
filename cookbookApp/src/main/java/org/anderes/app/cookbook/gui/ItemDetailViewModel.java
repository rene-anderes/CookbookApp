package org.anderes.app.cookbook.gui;

import org.anderes.app.cookbook.database.RecipeEntity;
import org.anderes.app.cookbook.repository.RecipeRepository;
import org.anderes.app.cookbook.repository.Resource;
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
