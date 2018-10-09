package org.anderes.app.cookbook.infrastructure;

public class Ingredient {


    private String resourceId;
    private String portion;
    private String description;
    private String comment;

    public Ingredient() {
        super();
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getPortion() {
        return portion;
    }

    public void setPortion(final String portion) {
        this.portion = portion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }
}
