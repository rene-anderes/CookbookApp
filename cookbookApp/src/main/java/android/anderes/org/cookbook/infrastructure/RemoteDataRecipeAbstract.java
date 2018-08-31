package android.anderes.org.cookbook.infrastructure;

import android.anderes.org.cookbook.model.RecipeAbstract;

import java.util.ArrayList;
import java.util.List;

import static android.anderes.org.cookbook.infrastructure.RemoteDataRecipeAbstract.Status.NONE;

public class RemoteDataRecipeAbstract {

    public enum Status { OK, ERROR, NONE };

    private List<RecipeAbstract> recipes = new ArrayList<>(0);
    private Status status = NONE;

    public RemoteDataRecipeAbstract setData(final List<RecipeAbstract> recipes) {
        this.recipes = recipes;
        return this;
    }

    public List<RecipeAbstract> getData() {
        return recipes;
    }

    public Status getStatus() {
        return status;
    }

    public RemoteDataRecipeAbstract setStatus(Status status) {
        this.status = status;
        return this;
    }
}
