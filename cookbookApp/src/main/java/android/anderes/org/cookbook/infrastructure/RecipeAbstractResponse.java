package android.anderes.org.cookbook.infrastructure;

import java.util.ArrayList;
import java.util.List;

import static android.anderes.org.cookbook.infrastructure.RecipeAbstractResponse.Status.NONE;

public class RecipeAbstractResponse {

    public enum Status { OK, ERROR, NONE };

    private List<RecipeAbstract> recipes = new ArrayList<>(0);
    private Status status = NONE;

    public RecipeAbstractResponse setData(final List<RecipeAbstract> recipes) {
        this.recipes = recipes;
        return this;
    }

    public List<RecipeAbstract> getData() {
        return recipes;
    }

    public Status getStatus() {
        return status;
    }

    public RecipeAbstractResponse setStatus(Status status) {
        this.status = status;
        return this;
    }
}
