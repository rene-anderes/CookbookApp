package android.anderes.org.cookbook.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(tableName = "recipe_table")
public class RecipeEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "recipeId")
    private String recipeId;
    private String title;
    private long editingDate;
    private long addingDate;
    private String preparation;
    private String preamble;
    private String noOfPeople;
    @TypeConverters(TagConverter.class)
    private Set<String> tags = new HashSet<>();
    private int rating;

    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(@NonNull String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public long getEditingDate() {
        return editingDate;
    }

    public void setEditingDate(long editingDate) {
        this.editingDate = editingDate;
    }

    public long getAddingDate() {
        return addingDate;
    }

    public void setAddingDate(long addingDate) {
        this.addingDate = addingDate;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getPreamble() {
        return preamble;
    }

    public void setPreamble(String preamble) {
        this.preamble = preamble;
    }

    public String getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(String noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}