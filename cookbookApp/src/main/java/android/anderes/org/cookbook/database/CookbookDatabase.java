package android.anderes.org.cookbook.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(
        entities = { RecipeAbstractEntity.class, RecipeEntity.class, IngredientEntity.class },
        version = 1)
public abstract class CookbookDatabase extends RoomDatabase {
    public abstract RecipeAbstractDao recipeAbstractDao();
    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();
}
