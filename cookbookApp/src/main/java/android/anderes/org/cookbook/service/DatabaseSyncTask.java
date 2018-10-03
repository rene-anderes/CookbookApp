package android.anderes.org.cookbook.service;

import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class DatabaseSyncTask extends AsyncTask<Void, Void, Void> {

    private final RecipeAbstractRepository repository;

    public DatabaseSyncTask(@NonNull final RecipeAbstractRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Void doInBackground(Void... Void) {

        try {
            final List<RecipeAbstractEntity> entities = repository.getRecipeCollectionFromRemote();
            repository.updateAllDataInDatabase(entities);
        } catch (IOException e) {
            Log.e("Sync", e.getMessage());
        }

        return null;
    }

}
