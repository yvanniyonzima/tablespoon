package com.example.tablespoon.classes.myRecipeRecycler;

public class RecipeCardItem {
    private int mImageResource;
    private String mRecipeCardText1;
    private String mRecipeCardText2;

    public RecipeCardItem(int imageResource, String cardText1, String cardText2, String recipeDescription)
    {
        mImageResource = imageResource;
        mRecipeCardText1 = cardText1;
        mRecipeCardText2 = cardText2;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public void setImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getRecipeCardText1() {
        return mRecipeCardText1;
    }

    public void setRecipeCardText1(String mRecipeCardText1) {
        this.mRecipeCardText1 = mRecipeCardText1;
    }

    public String getRecipeCardText2() {
        return mRecipeCardText2;
    }

    public void setRecipeCardText2(String mRecipeCardText2) {
        this.mRecipeCardText2 = mRecipeCardText2;
    }



    @Override
    public String toString() {
        return "RecipeCardItem{" +
                "mImageResource=" + mImageResource +
                ", mRecipeCardText1='" + mRecipeCardText1 + '\'' +
                ", mRecipeCardText2='" + mRecipeCardText2 + '\'' +
                '}';
    }
}
