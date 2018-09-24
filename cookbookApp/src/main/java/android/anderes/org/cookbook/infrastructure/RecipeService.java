package android.anderes.org.cookbook.infrastructure;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeService {

    @GET("resources-api/recipes")
    Observable<List<RecipeAbstract>> getRecipeAbstract();

    @GET("resources-api/recipes/{id}")
    Observable<Recipe> getRecipe(@Path("id") String recipeId);
}
