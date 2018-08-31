package android.anderes.org.cookbook.model;

public class RecipeAbstract {

    private String title;
    private String recipeId;
    private long lastUpdate;

    public String getTitle() {
        return title;
    }

    public RecipeAbstract setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public RecipeAbstract setRecipeId(String recipeId) {
        this.recipeId = recipeId;
        return this;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public RecipeAbstract setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

}
