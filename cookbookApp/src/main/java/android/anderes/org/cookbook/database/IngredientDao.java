package android.anderes.org.cookbook.database;

import android.anderes.org.cookbook.infrastructure.Ingredient;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IngredientEntity entity);

    @Query("DELETE FROM ingredient_table")
    void deleteAll();

    @Query("SELECT * FROM ingredient_table WHERE recipeId = :recipeId")
    LiveData<List<IngredientEntity>> getIngredientsForRecipe(String recipeId);
}
