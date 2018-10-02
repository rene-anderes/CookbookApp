package android.anderes.org.cookbook;

import android.anderes.org.cookbook.database.CookbookDatabase;
import android.anderes.org.cookbook.database.IngredientDao;
import android.anderes.org.cookbook.database.RecipeAbstractDao;
import android.anderes.org.cookbook.database.RecipeDao;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.anderes.org.cookbook.repository.IngredientRepository;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.anderes.org.cookbook.repository.RecipeRepository;

public interface ServiceLocator {

    CookbookDatabase getDatabase();

    RecipeAbstractRepository getRecipeAbstractRepository();

    RecipeService getRecipeService();

    RecipeAbstractDao getRecipeAbstractDao();

    RecipeDao getRecipeDao();

    IngredientDao getIngredientDao();

    RecipeRepository getRecipeRepository();

    IngredientRepository getIngredientRepository();

    AppConfiguration getAppConfiguration();
}
