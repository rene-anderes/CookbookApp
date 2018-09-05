package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.anderes.org.cookbook.repository.Resource;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class ItemListViewModel extends ViewModel {

    private final LiveData<Resource<List<RecipeAbstractEntity>>> recipes;

    public ItemListViewModel(RecipeAbstractRepository repository) {
        recipes = repository.getRecipes();
    }

    LiveData<Resource<List<RecipeAbstractEntity>>> getRecipes() {
        return recipes;
    }
}
