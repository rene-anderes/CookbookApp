package android.anderes.org.cookbook;

import android.anderes.org.cookbook.database.CookbookDatabase;
import android.anderes.org.cookbook.database.IngredientDao;
import android.anderes.org.cookbook.database.RecipeAbstractDao;
import android.anderes.org.cookbook.database.RecipeDao;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.anderes.org.cookbook.repository.IngredientRepository;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.anderes.org.cookbook.repository.RecipeRepository;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocatorForApp implements ServiceLocator {

    private CookbookDatabase database;
    private final Context context;
    private Retrofit retrofit;
    private static ServiceLocator instance;
    private final static String BASE_URL = "http://www.anderes.org/";

    static ServiceLocator getNewInstance(final Context context) {
        synchronized (ServiceLocatorForApp.class) {
            instance = new ServiceLocatorForApp(context, BASE_URL);
        }
        return instance;
    }

    public static @NonNull ServiceLocator getInstance() {
        return instance;
    }

    private ServiceLocatorForApp(final Context context, final String baseHttpUrl) {
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
                    database = Room.databaseBuilder(context, CookbookDatabase.class, "cookbook_database").build();
                }
            }
        }
        return database;
    }

    @Override
    public RecipeAbstractRepository getRecipeAbstractRepository() {
        return new RecipeAbstractRepository(getRecipeService(), getRecipeAbstractDao());
    }

    @Override
    public RecipeService getRecipeService() {
        return retrofit.create(RecipeService.class);
    }

    @Override
    public RecipeAbstractDao getRecipeAbstractDao() {
        return getDatabase().recipeAbstractDao();
    }

    @Override
    public RecipeDao getRecipeDao() { return getDatabase().recipeDao(); }

    @Override
    public IngredientDao getIngredientDao() {
        return getDatabase().ingredientDao();
    }

    @Override
    public RecipeRepository getRecipeRepository() {
        return new RecipeRepository(getRecipeService(), getRecipeDao());
    }

    @Override
    public IngredientRepository getIngredientRepository() {
        return new IngredientRepository(getRecipeService(), getIngredientDao());
    }

    @Override
    public AppConfiguration getAppConfiguration() {
        return new AppUtilities(context);
    }
}
