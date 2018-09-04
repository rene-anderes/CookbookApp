package android.anderes.org.cookbook;

import android.anderes.org.cookbook.database.CookbookDatabase;
import android.anderes.org.cookbook.database.RecipeAbstractDao;
import android.anderes.org.cookbook.infrastructure.RecipeService;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;

import retrofit2.Retrofit;

public interface ServiceLocator {

    CookbookDatabase getDatabase();

    RecipeAbstractRepository getRecipeAbstractRepository();

    RecipeService getRecipeService();

    RecipeAbstractDao getRecipeAbstractDao();
}
