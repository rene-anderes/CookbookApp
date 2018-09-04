package android.anderes.org.cookbook;

import android.anderes.org.cookbook.database.CookbookDatabase;
import android.anderes.org.cookbook.database.RecipeAbstractDao;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.arch.persistence.room.Room;
import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocatorForTest implements ServiceLocator {

    private CookbookDatabase database;
    private final Context context;
    private Retrofit retrofit;

    public ServiceLocatorForTest(final Context context, final String baseHttpUrl) {
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(baseHttpUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public CookbookDatabase getDatabase() {
        if (database == null) {
            synchronized (CookbookDatabase.class) {
                if (database == null) {
                    database = Room.inMemoryDatabaseBuilder(context, CookbookDatabase.class)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return database;
    }

    @Override
    public RecipeAbstractRepository getRecipeAbstractRepository() {
        return null;
    }

    @Override
    public RecipeService getRecipeService() {
        return retrofit.create(RecipeService.class);
    }

    @Override
    public RecipeAbstractDao getRecipeAbstractDao() {
        return getDatabase().recipeAbstractDao();
    }


}
