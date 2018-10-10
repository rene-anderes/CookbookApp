package org.anderes.app.cookbook.service;

import org.anderes.app.cookbook.AppConfiguration;
import org.anderes.app.cookbook.R;
import org.anderes.app.cookbook.ServiceLocator;
import org.anderes.app.cookbook.ServiceLocatorProvider;
import org.anderes.app.cookbook.database.RecipeAbstractEntity;
import org.anderes.app.cookbook.repository.IngredientRepository;
import org.anderes.app.cookbook.repository.RecipeAbstractRepository;
import org.anderes.app.cookbook.repository.RecipeRepository;
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

import static org.anderes.app.cookbook.AppConstants.BROADCAST_SYNC_ACTION;
import static org.anderes.app.cookbook.AppConstants.BROADCAST_SYNC_MESSAGE_KEY;
import static org.anderes.app.cookbook.AppConstants.LOG_TAG_SYNC;

public class CookbookSyncService extends IntentService {

    public CookbookSyncService() {
        super("CookbookSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ServiceLocator serviceLocator = ServiceLocatorProvider.getInstance();
        final RecipeAbstractRepository recipeAbstractRepository = serviceLocator.getRecipeAbstractRepository();
        final RecipeRepository recipeRepository = serviceLocator.getRecipeRepository();
        final IngredientRepository ingredientRepository = serviceLocator.getIngredientRepository();
        final AppConfiguration config = serviceLocator.getAppConfiguration();

        if (config.isOnline()) {
            Log.d(LOG_TAG_SYNC, "Internet-Verbindung vorhanden.");
            doIt(recipeAbstractRepository, recipeRepository, ingredientRepository, config);
        } else {
            Log.i(LOG_TAG_SYNC, "Keine Internet-Verbindung.");
        }
    }

    private void doIt(@NonNull final RecipeAbstractRepository recipeAbstractRepository,
                      @NonNull final RecipeRepository recipeRepository,
                      @NonNull final IngredientRepository ingredientRepository,
                      @NonNull final AppConfiguration configuration) {

        Log.i(LOG_TAG_SYNC, "Sync gestartet ...");
        try {
            final List<RecipeAbstractEntity> entities = recipeAbstractRepository.getRecipeCollectionFromRemote();
            recipeAbstractRepository.updateAllDataInDatabase(entities);
            if(configuration.isFullSync()) {
                Log.i(LOG_TAG_SYNC, "Full-Sync ist aktiviert.");
                processFullSync(entities, recipeRepository, ingredientRepository);
            } else {
                Log.i(LOG_TAG_SYNC, "Full-Sync ist deaktiviert.");
                Log.d(LOG_TAG_SYNC, "Existierende Daten werden aktualisiert.");
                processExistsSync(entities, recipeRepository, ingredientRepository);
            }

            final List<String> recipeIds = new ArrayList<>();
            for (RecipeAbstractEntity recipeAbstractEntity : entities) {
                recipeIds.add(recipeAbstractEntity.getRecipeId());
            }
            recipeRepository.deleteOrphan(recipeIds);
            ingredientRepository.deleteOrphan(recipeIds);
            Log.d(LOG_TAG_SYNC, "Verwaiste Einträge konsolidiert.");
        } catch (Exception e) {
            Log.e(LOG_TAG_SYNC, "Sync Error: " + e.getMessage());
        }
        sendFinishedMessage();
        Log.i(LOG_TAG_SYNC, "Sync beendet.");
    }

    private void sendFinishedMessage() {
        final LocalBroadcastManager locBcMan = LocalBroadcastManager.getInstance(this);
        final Bundle messBundle = new Bundle();
        messBundle.putString(BROADCAST_SYNC_MESSAGE_KEY, getResources().getString(R.string.msg_recipes_updated));
        final Intent returnIntent = new Intent(BROADCAST_SYNC_ACTION);
        returnIntent.putExtras(messBundle);
        locBcMan.sendBroadcast(returnIntent);
    }

    private void processFullSync(@NonNull final List<RecipeAbstractEntity> entities,
                                 @NonNull final RecipeRepository recipeRepository,
                                 @NonNull final IngredientRepository ingredientRepository) throws IOException {

        for (RecipeAbstractEntity entity : entities) {
            if(!recipeRepository.isExists(entity.getRecipeId()) ||
                    recipeRepository.isSyncNecessary(entity.getRecipeId(), entity.getLastUpdate())) {
                recipeRepository.updateAllDataInDatabase(entity.getRecipeId());
                ingredientRepository.updateAllDataInDatabase(entity.getRecipeId());
                Log.d(LOG_TAG_SYNC, "Rezept-Id: " + entity.getRecipeId() + " - Daten aktualisiert.");
            } else {
                Log.d(LOG_TAG_SYNC, "Rezept-Id: " + entity.getRecipeId() + " - Daten aktuell.");
            }
        }
    }

    private void processExistsSync(@NonNull final List<RecipeAbstractEntity> entities,
                                 @NonNull final RecipeRepository recipeRepository,
                                 @NonNull final IngredientRepository ingredientRepository) throws IOException {

        for (RecipeAbstractEntity entity : entities) {
            if(recipeRepository.isSyncNecessary(entity.getRecipeId(), entity.getLastUpdate())) {
                recipeRepository.updateAllDataInDatabase(entity.getRecipeId());
                ingredientRepository.updateAllDataInDatabase(entity.getRecipeId());
                Log.d(LOG_TAG_SYNC, "Rezept-Id: " + entity.getRecipeId() + " - Daten aktualisiert.");
            } else {
                Log.d(LOG_TAG_SYNC, "Rezept-Id: " + entity.getRecipeId() + " - Daten aktuell.");
            }
        }
    }
}
