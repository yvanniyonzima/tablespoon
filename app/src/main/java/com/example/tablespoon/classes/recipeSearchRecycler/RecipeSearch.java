package com.example.tablespoon.classes.recipeSearchRecycler;

public class RecipeSearch
{
    private String mRecipeName;
    private String mRecipeCookTime;
    private String mServings;
    private String mURL;
    private String mImageURL;

    public RecipeSearch(String recipeName, String recipeCookTime, String servings, String url, String imageUrl)
    {
        mRecipeName = recipeName;
        mRecipeCookTime = recipeCookTime;
        mServings = servings;
        mURL = url;
        mImageURL = imageUrl;
    }

    public String getRecipeName() {
        return mRecipeName;
    }

    public void setRecipeName(String recipeName) {
        this.mRecipeName = recipeName;
    }

    public String getRecipeCookTime() {
        return mRecipeCookTime;
    }

    public void setRecipeCookTime(String recipeCookTime) {
        this.mRecipeCookTime = recipeCookTime;
    }

    public String getServings() {
        return mServings;
    }

    public void setServings(String servings) {
        this.mServings = servings;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String URL) {
        this.mURL = URL;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        this.mImageURL = imageURL;
    }

    @Override
    public String toString() {
        return "RecipeSearch{" +
                "mRecipeName='" + mRecipeName + '\'' +
                ", mRecipeCookTime='" + mRecipeCookTime + '\'' +
                ", mServings='" + mServings + '\'' +
                ", mURL='" + mURL + '\'' +
                ", mImageURL='" + mImageURL + '\'' +
                '}';
    }
}
