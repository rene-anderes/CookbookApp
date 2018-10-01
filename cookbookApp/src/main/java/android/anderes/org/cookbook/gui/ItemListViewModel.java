package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.anderes.org.cookbook.repository.Resource;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

public class ItemListViewModel extends ViewModel {

    private LiveData<Resource<List<RecipeAbstractEntity>>> recipes;
    private RecipeAbstractRepository repository;

    public ItemListViewModel() {

    }

    void setRepository(@NonNull final RecipeAbstractRepository repository) {
        this.repository = repository;
    }

    LiveData<Resource<List<RecipeAbstractEntity>>> getRecipes() {
        if(recipes == null) {
            recipes = repository.getRecipes();
        }
        return recipes;
    }
}
