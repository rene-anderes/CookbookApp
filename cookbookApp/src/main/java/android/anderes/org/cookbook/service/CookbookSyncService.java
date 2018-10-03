package android.anderes.org.cookbook.service;

import android.anderes.org.cookbook.AppConfiguration;
import android.anderes.org.cookbook.ServiceLocatorForApp;
import android.anderes.org.cookbook.database.RecipeAbstractDao;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.anderes.org.cookbook.repository.IngredientRepository;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.anderes.org.cookbook.repository.RecipeRepository;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class CookbookSyncService extends IntentService {

    public CookbookSyncService() {
        super("CookbookSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final RecipeAbstractRepository recipeAbstractRepository =
                ServiceLocatorForApp.getInstance().getRecipeAbstractRepository();
        final RecipeRepository recipeRepository =
                ServiceLocatorForApp.getInstance().getRecipeRepository();
        final IngredientRepository ingredientRepository =
                ServiceLocatorForApp.getInstance().getIngredientRepository();
        final AppConfiguration config = ServiceLocatorForApp.getInstance().getAppConfiguration();
        if (config.isOnline()) {
            final DatabaseSyncTask task = new DatabaseSyncTask(recipeAbstractRepository, recipeRepository, ingredientRepository, config);
            task.execute();
        }
    }
}
