package org.anderes.app.cookbook.gui;

import org.anderes.app.cookbook.database.RecipeAbstractEntity;
import org.anderes.app.cookbook.repository.RecipeAbstractRepository;
import org.anderes.app.cookbook.repository.Resource;
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
