package org.anderes.app.cookbook;

import org.anderes.app.cookbook.database.CookbookDatabase;
import org.anderes.app.cookbook.database.IngredientDao;
import org.anderes.app.cookbook.database.RecipeAbstractDao;
import org.anderes.app.cookbook.database.RecipeDao;
import org.anderes.app.cookbook.infrastructure.RecipeService;
import org.anderes.app.cookbook.repository.IngredientRepository;
import org.anderes.app.cookbook.repository.RecipeAbstractRepository;
import org.anderes.app.cookbook.repository.RecipeRepository;

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
