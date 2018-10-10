package org.anderes.app.cookbook;

import org.anderes.app.cookbook.database.CookbookDatabase;
import org.anderes.app.cookbook.database.IngredientDao;
import org.anderes.app.cookbook.database.RecipeAbstractDao;
import org.anderes.app.cookbook.database.RecipeDao;
import org.anderes.app.cookbook.infrastructure.RecipeService;
import org.anderes.app.cookbook.repository.IngredientRepository;
import org.anderes.app.cookbook.repository.RecipeAbstractRepository;
import org.anderes.app.cookbook.repository.RecipeRepository;
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
    private final static String BASE_URL = "http://www.anderes.org/";

    ServiceLocatorForApp(final Context context) {
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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
