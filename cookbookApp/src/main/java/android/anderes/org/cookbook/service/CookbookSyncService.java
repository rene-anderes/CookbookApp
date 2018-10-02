package android.anderes.org.cookbook.service;

import android.anderes.org.cookbook.AppConfiguration;
import android.anderes.org.cookbook.ServiceLocatorForApp;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class CookbookSyncService extends IntentService {

    public CookbookSyncService() {
        super("CookbookSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final RecipeService service = ServiceLocatorForApp.getInstance().getRecipeService();
        final AppConfiguration config = ServiceLocatorForApp.getInstance().getAppConfiguration();
        if (config.isOnline()) {
            final DatabaseSyncTask task = new DatabaseSyncTask(service);
            task.execute();
        }
    }
}
