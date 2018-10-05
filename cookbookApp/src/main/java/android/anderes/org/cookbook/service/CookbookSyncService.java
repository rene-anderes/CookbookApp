package android.anderes.org.cookbook.service;

import android.anderes.org.cookbook.AppConfiguration;
import android.anderes.org.cookbook.AppConstants;
import android.anderes.org.cookbook.R;
import android.anderes.org.cookbook.ServiceLocatorForApp;
import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.anderes.org.cookbook.repository.IngredientRepository;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.anderes.org.cookbook.repository.RecipeRepository;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.anderes.org.cookbook.AppConstants.BROADCAST_SYNC_ACTION;
import static android.anderes.org.cookbook.AppConstants.BROADCAST_SYNC_ACTION_KEY;

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
            Log.d("Sync", "Internet-Verbindung vorhanden.");
            doIt(recipeAbstractRepository, recipeRepository, ingredientRepository, config);
        } else {
            Log.i("Sync", "Keine Internet-Verbindung.");
        }
    }

    private void doIt(@NonNull final RecipeAbstractRepository recipeAbstractRepository,
                      @NonNull final RecipeRepository recipeRepository,
                      @NonNull final IngredientRepository ingredientRepository,
                      @NonNull final AppConfiguration configuration) {

        Log.i("Sync", "Sync gestartet ...");
        try {
            final List<RecipeAbstractEntity> entities = recipeAbstractRepository.getRecipeCollectionFromRemote();
            recipeAbstractRepository.updateAllDataInDatabase(entities);
            if(configuration.isFullSync()) {
                Log.i("Sync", "Full-Sync ist aktiviert.");
                processFullSync(entities, recipeRepository, ingredientRepository);
                sendFinished();
            } else {
                Log.i("Sync", "Full-Sync ist deaktiviert.");
            }

            final List<String> recipeIds = new ArrayList<>();
            for (RecipeAbstractEntity recipeAbstractEntity : entities) {
                recipeIds.add(recipeAbstractEntity.getRecipeId());
            }
            recipeRepository.deleteOrphan(recipeIds);
            ingredientRepository.deleteOrphan(recipeIds);
            Log.d("Sync", "Verwaiste Einträge konsolidiert.");
        } catch (Exception e) {
            Log.e("Sync", "Sync Error: " + e.getMessage());
        }
        Log.i("Sync", "Sync beendet.");
    }

    private void sendFinished() {
        final LocalBroadcastManager locBcMan = LocalBroadcastManager.getInstance(this);
        final Bundle messBundle = new Bundle();
        messBundle.putString(BROADCAST_SYNC_ACTION_KEY, getResources().getString(R.string.msg_recipes_updated));
        final Intent returnIntent = new Intent(BROADCAST_SYNC_ACTION);
        returnIntent.putExtras(messBundle);
        locBcMan.sendBroadcast(returnIntent);
    }

    private void processFullSync(List<RecipeAbstractEntity> entities,
                                 @NonNull final RecipeRepository recipeRepository,
                                 @NonNull final IngredientRepository ingredientRepository) throws IOException {

        for (RecipeAbstractEntity entity : entities) {
            if(recipeRepository.isSyncNecessary(entity.getRecipeId(), entity.getLastUpdate())) {
                recipeRepository.updateAllDataInDatabase(entity.getRecipeId());
                ingredientRepository.updateAllDataInDatabase(entity.getRecipeId());
                Log.d("Sync", "Rezept-Id: " + entity.getRecipeId() + " - Daten aktualisiert.");
            } else {
                Log.d("Sync", "Rezept-Id: " + entity.getRecipeId() + " - Daten aktuell.");
            }
        }
    }
}
