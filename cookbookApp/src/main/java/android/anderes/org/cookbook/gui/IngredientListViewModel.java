package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.database.IngredientEntity;
import android.anderes.org.cookbook.repository.IngredientRepository;
import android.anderes.org.cookbook.repository.Resource;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

public class IngredientListViewModel extends ViewModel {

    private LiveData<Resource<List<IngredientEntity>>> ingredients;
    private IngredientRepository repository;

    public void setRepository(@NonNull final IngredientRepository repository) {
        this.repository = repository;
    }

    LiveData<Resource<List<IngredientEntity>>> getIngredients(@NonNull final String recipeId) {
        ingredients = repository.getIngredients(recipeId);
        return ingredients;
    }
}
