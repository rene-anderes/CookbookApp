package org.anderes.app.cookbook.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.support.annotation.NonNull;

import java.util.List;

@Dao
public abstract class RecipeAbstractDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(RecipeAbstractEntity entity);

    @Query("DELETE FROM recipeAbstract_table")
    public abstract void deleteAll();

    @Query("SELECT * from recipeAbstract_table ORDER BY title ASC")
    public abstract LiveData<List<RecipeAbstractEntity>> getAllRecipeAbstract();

    @Transaction
    public void updateData(@NonNull final List<RecipeAbstractEntity> data) {
        deleteAll();
        insertAll(data);
    }

    @Insert
    public abstract void insertAll(List<RecipeAbstractEntity> data);

    @Query("SELECT * from recipeAbstract_table ORDER BY title ASC")
    public abstract List<RecipeAbstractEntity> getRecipeAbstractCollection();
}
