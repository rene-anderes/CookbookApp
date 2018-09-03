package android.anderes.org.cookbook.repository;

import android.anderes.org.cookbook.infrastructure.RecipeAbstract;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.anderes.org.cookbook.model.Resource;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeAbstractRepository {

    private final Retrofit retrofit;
    private List<RecipeAbstract> list = new ArrayList<>(0);
    private MediatorLiveData<List<RecipeAbstract>> data = new MediatorLiveData<>();

    public RecipeAbstractRepository(final String httpUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(httpUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public LiveData<Resource<List<RecipeAbstract>>> getRecipes() {
        return new NetworkBoundResource<List<RecipeAbstract>, List<RecipeAbstract>>() {

            @Override
            protected void saveCallResult(@NonNull List<RecipeAbstract> item) {
                list = item;
            }

            @Override
            protected boolean shouldFetch(@Nullable List<RecipeAbstract> data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<RecipeAbstract>> loadFromDb() {
                data.setValue(list);
                return data;
            }

            @NonNull
            @Override
            protected Observable<List<RecipeAbstract>> createCall() {
                final RecipeService service = retrofit.create(RecipeService.class);
                return service.getRecipeAbstract();
            }

        }.getAsLiveData();
    }
}
