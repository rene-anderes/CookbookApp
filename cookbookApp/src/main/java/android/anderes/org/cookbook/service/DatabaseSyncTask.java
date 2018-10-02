package android.anderes.org.cookbook.service;

import android.anderes.org.cookbook.infrastructure.RecipeAbstract;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.anderes.org.cookbook.repository.Resource;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DatabaseSyncTask extends AsyncTask<Void, Void, Void> {

    private final RecipeService service;
    private final Consumer<List<RecipeAbstract>> handleRecipes =
            response -> { Log.v("Sync", "-------------- SYNC --------------"); };

    public DatabaseSyncTask(RecipeService service) {
        this.service = service;
    }

    @Override
    protected Void doInBackground(Void... Void) {
        service.getRecipeAbstract()
                .subscribeOn(Schedulers.io()) // Darf nicht im MainThread gemacht werden
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleRecipes, t -> {
                    Log.e("Sync", "-------------- SYNC-ERROR --------------");
                }).isDisposed();
        return null;
    }
}
