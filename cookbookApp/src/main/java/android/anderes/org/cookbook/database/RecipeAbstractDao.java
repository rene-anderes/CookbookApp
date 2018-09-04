package android.anderes.org.cookbook.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeAbstractDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeAbstractEntity entity);

    @Query("DELETE FROM recipeAbstract_table")
    void deleteAll();

    @Query("SELECT * from recipeAbstract_table ORDER BY title ASC")
    LiveData<List<RecipeAbstractEntity>> getAllRecipeAbstract();
}
