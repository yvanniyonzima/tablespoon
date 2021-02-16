package com.example.tablespoon;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tablespoon.classes.Recipe;

public class ViewRecipeActivity extends AppCompatActivity
{
    private static String TAG = ViewRecipeActivity.class.getName();
    private ImageView mRecipeImage;
    private ScrollView mRecipeItems;
    private TextView mRecipeNameView;
    private TextView mRecipeDescriptionView;
    private TextView mRecipeIngredientsView;
    private TextView mRecipeStepsView;

    private Recipe mRecipe;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_recipe_menu,menu);
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe_layout);
        mRecipeImage = (ImageView) findViewById(R.id.view_recipe_image);
        mRecipeItems = (ScrollView) findViewById(R.id.view_recipe_items);
        mRecipeNameView = (TextView) findViewById(R.id.view_recipe_name);
        mRecipeDescriptionView = (TextView) findViewById(R.id.view_recipe_description);
        mRecipeIngredientsView = (TextView) findViewById(R.id.view_recipe_ingredient);
        mRecipeStepsView = (TextView) findViewById(R.id.view_recipe_steps);

        mRecipe = new Recipe((Recipe) getIntent().getSerializableExtra("RECIPE"));

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.edit_recipe:
                Log.i(TAG,"Editing recipe " + mRecipe.getName());
                //create a new intent to pass to addRecipe with information alreadu filled
                Intent editRecipe = new Intent(ViewRecipeActivity.this, AddRecipeActivity.class);
                editRecipe.putExtra("INSTANCE","edit_recipe");
                editRecipe.putExtra("RECIPE",mRecipe);
                editRecipe.putExtra("RECIPE_NAME", mRecipe.getName());
                startActivity(editRecipe);
                return true;
            case R.id.delete_recipe_menu:
                Log.i(TAG,"Deleting recipe " + mRecipe.getName());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
