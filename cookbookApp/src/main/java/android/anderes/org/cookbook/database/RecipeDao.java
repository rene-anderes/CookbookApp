package android.anderes.org.cookbook.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeEntity entity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(RecipeEntity entity);

    @Query("DELETE FROM recipe_table")
    void deleteAll();

    @Query("SELECT * FROM recipe_table WHERE recipeId = :id")
    LiveData<RecipeEntity> getRecipe(String id);
}
