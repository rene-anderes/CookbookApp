package android.anderes.org.cookbook.repository;

import android.anderes.org.cookbook.ServiceLocator;
import android.anderes.org.cookbook.database.RecipeAbstractDao;
import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.anderes.org.cookbook.database.RecipeEntity;
import android.anderes.org.cookbook.infrastructure.RecipeAbstract;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeAbstractRepository {

    private final RecipeService service;
    private final RecipeAbstractDao recipeAbstractDao;

    public RecipeAbstractRepository(@NonNull final RecipeService service,
                                    @NonNull final RecipeAbstractDao recipeAbstractDao) {
        this.service = service;
        this.recipeAbstractDao = recipeAbstractDao;
    }

    public LiveData<Resource<List<RecipeAbstractEntity>>> getRecipes() {
        return new NetworkBoundResource<List<RecipeAbstractEntity>, List<RecipeAbstract>>() {

            @Override
            protected void saveCallResult(@NonNull List<RecipeAbstract> data) {
                for (RecipeAbstract r : data) {
                    RecipeAbstractEntity entity = new RecipeAbstractEntity();
                    entity.setRecipeId(r.getId());
                    entity.setTitle(r.getTitle());
                    entity.setLastUpdate(r.getEditingDate());
                    recipeAbstractDao.insert(entity);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RecipeAbstractEntity> data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<RecipeAbstractEntity>> loadFromDb() {
                return recipeAbstractDao.getAllRecipeAbstract();
            }

            @NonNull
            @Override
            protected Observable<List<RecipeAbstract>> createCall() {
                return service.getRecipeAbstract();
            }

        }.getAsLiveData();
    }
}
