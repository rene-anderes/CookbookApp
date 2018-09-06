package android.anderes.org.cookbook.infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recipe {

    private String id;
    private long editingDate;
    private long addingDate;
    private String title;
    private String preparation;
    private String preamble;
    private String noOfPeople;
    private Set<String> tags = new HashSet<String>();
    private Integer rating = Integer.valueOf(0);
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by the Date object.
     * @return time
     */
    public Long getEditingDate() {
        return this.editingDate;
    }

    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by the Date object.
     * @return time
     */
    public Long getAddingDate() {
        return this.addingDate;
    }

    public void setEditingDate(long lastUpdateTime) {
        this.editingDate =lastUpdateTime;

    }

    public void setAddingDate(long addingDateTime) {
        this.addingDate = addingDateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(final String preparation) {
        this.preparation = preparation;
    }

    public String getPreamble() {
        return preamble;
    }

    public void setPreamble(final String preamble) {
        this.preamble = preamble;
    }

    public String getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(final String noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }
}
