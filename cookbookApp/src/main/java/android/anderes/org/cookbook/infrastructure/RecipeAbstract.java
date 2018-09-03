package android.anderes.org.cookbook.infrastructure;

public class RecipeAbstract {

    private String title;
    private String id;
    private long editingDate;

    public RecipeAbstract() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public RecipeAbstract setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getId() {
        return id;
    }

    public RecipeAbstract setId(String id) {
        this.id = id;
        return this;
    }

    public long getEditingDate() {
        return editingDate;
    }

    public RecipeAbstract setEditingDate(long editingDate) {
        this.editingDate = editingDate;
        return this;
    }

}
