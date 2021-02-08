package com.example.tablespoon;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tablespoon.classes.Recipe;

public class ViewRecipeActivity extends AppCompatActivity
{

    private ImageView mRecipeImage;
    private ScrollView mRecipeItems;
    private TextView mRecipeNameView;
    private TextView mRecipeDescriptionView;
    private TextView mRecipeIngredientsLabel;
    private TextView mRecipeIngredientsView;
    private TextView mRecipeStepsLabel;
    private TextView mRecipeStepsView;

    private Recipe mRecipe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe_layout);
        mRecipeImage = (ImageView) findViewById(R.id.view_recipe_image);
        mRecipeItems = (ScrollView) findViewById(R.id.view_recipe_items);
        mRecipeNameView = (TextView) findViewById(R.id.view_recipe_name);
        mRecipeDescriptionView = (TextView) findViewById(R.id.view_recipe_description);
        mRecipeIngredientsLabel = (TextView) findViewById(R.id.view_recipe_ingredient_label);
        mRecipeIngredientsView = (TextView) findViewById(R.id.view_recipe_ingredient);
        mRecipeStepsLabel = (TextView) findViewById(R.id.view_recipe_steps_label);
        mRecipeStepsView = (TextView) findViewById(R.id.view_recipe_steps);

        mRecipe = new Recipe((Recipe) getIntent().getSerializableExtra("RECIPE_TO_VIEW"));

        mRecipeNameView.setText(mRecipe.getName());
        mRecipeDescriptionView.setText(mRecipe.getDescription());

        String ingredients = "";
        for(String ingredient: mRecipe.getIngredients())
        {
            ingredients += ingredient + "\n";
        }
        mRecipeIngredientsView.setText(ingredients);

        String steps = "";
        int stepCount = 1;
        for(String step : mRecipe.getSteps())
        {
            steps += Integer.toString(stepCount) + ". " + step + "\n";
            stepCount++;
        }
        mRecipeStepsView.setText(steps);






    }
}
