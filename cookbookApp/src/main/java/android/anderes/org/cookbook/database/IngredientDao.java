package android.anderes.org.cookbook.database;

import android.anderes.org.cookbook.infrastructure.Ingredient;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

@Dao
public abstract class IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(IngredientEntity entity);

    @Query("DELETE FROM ingredient_table")
    public abstract void deleteAll();

    @Query("SELECT * FROM ingredient_table WHERE recipeId = :recipeId ORDER BY description ASC")
    public abstract LiveData<List<IngredientEntity>> getIngredientsForRecipe(String recipeId);

    @Query("DELETE FROM ingredient_table WHERE recipeId NOT IN(:recipeIds)")
    public abstract void deleteOrphan(List<String> recipeIds);

    @Query("DELETE FROM ingredient_table WHERE recipeId = :recipeId")
    public abstract void deleteByRecipeId(String recipeId);

    @Query("SELECT COUNT(*) FROM ingredient_table")
    public abstract int count();

    @Insert
    public abstract void insertAll(List<IngredientEntity> ingredients);

    @Transaction
    public int updateByRecipeId(String recipeId, List<IngredientEntity> ingredients) {
        for (IngredientEntity e : ingredients) {
            if (!e.getRecipeId().equals(recipeId)) {
                throw new IllegalArgumentException();
            }
        }
        deleteByRecipeId(recipeId);
        insertAll(ingredients);
        return countByRecipeId(recipeId);
    }

    @Query("SELECT COUNT(*) FROM ingredient_table WHERE recipeId = :recipeId")
    abstract int countByRecipeId(String recipeId);
}
