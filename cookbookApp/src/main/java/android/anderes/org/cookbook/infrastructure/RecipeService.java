package android.anderes.org.cookbook.infrastructure;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("resources-api/recipes")
    Observable<List<RecipeAbstract>> getRecipeAbstract();
}
