package android.anderes.org.cookbook.repository;

import android.anderes.org.cookbook.infrastructure.ApiResponse;
import android.anderes.org.cookbook.infrastructure.RecipeAbstract;
import android.anderes.org.cookbook.infrastructure.RecipeAbstractResponse;
import android.anderes.org.cookbook.model.Resource;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class RecipeAbstractRepository {

    public LiveData<Resource<List<RecipeAbstract>>> getRecipes() {
        return new NetworkBoundResource<List<RecipeAbstract>, RecipeAbstractResponse>() {

            @Override
            protected void saveCallResult(@NonNull RecipeAbstractResponse item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<RecipeAbstract> data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<RecipeAbstract>> loadFromDb() {
                return null;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeAbstractResponse>> createCall() {
                return null;
            }

        }.getAsLiveData();
    }
}
