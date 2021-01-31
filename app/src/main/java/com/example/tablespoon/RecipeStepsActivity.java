package com.example.tablespoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeStepsActivity extends AppCompatActivity
{
    //varibales
    private EditText recipeStepField;
    private Button addStepButton;
    private ListView recipeStepsView;
    private Button cancelAddRecipe;
    private Button previousPage;
    private Button doneAddingRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_steps_layout);

        //bind ui elements to variables
        recipeStepField = (EditText) findViewById(R.id.add_step_field);
        addStepButton = (Button) findViewById(R.id.add_step_button);
        recipeStepsView = (ListView) findViewById(R.id.steps_view);
        cancelAddRecipe = (Button) findViewById(R.id.cancel_add_recipe_button2);
        previousPage = (Button) findViewById(R.id.add_recipe_first_page);
        doneAddingRecipe = (Button) findViewById(R.id.done_adding_recipe);

        //handle cancel click
        cancelAddRecipe.setOnClickListener((View v)->
        {
            Intent cancelAddRecipe2 = new Intent(RecipeStepsActivity.this, MyRecipes.class);
            startActivity(cancelAddRecipe2);
        });

        //handle previous click
        previousPage.setOnClickListener((View v)->
        {
            Intent previousAddRecipePage = new Intent(RecipeStepsActivity.this, AddRecipeActivity.class);
            startActivity(previousAddRecipePage);
        });
    }
}
