package com.example.tablespoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class AddRecipeActivity extends AppCompatActivity
{
    //variables
    private EditText recipeNameField;
    private Spinner recipeTypeChoice;
    private EditText servingsField;
    private EditText recipeDescriptionField;
    private EditText addIngredientField;
    private Button addIngredientButton;
    private ListView ingredientsView;
    private Button nextPageButton;
    private Button cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_layout);

        //bind ui elements to variables
        recipeNameField = (EditText) findViewById(R.id.recipe_name_field);
        recipeTypeChoice = (Spinner) findViewById(R.id.recipe_type_choice);
        servingsField = (EditText) findViewById(R.id.servings);
        recipeDescriptionField = (EditText) findViewById(R.id.description_field);
        addIngredientField = (EditText) findViewById(R.id.ingredient_field);
        addIngredientButton = (Button) findViewById(R.id.add_ingredient_button);
        ingredientsView = (ListView) findViewById(R.id.ingredient_listView);
        nextPageButton = (Button) findViewById(R.id.next_button);
        cancelButton = (Button) findViewById(R.id.cancel_add_recipe_button);

        //add recipe choices to spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        recipeTypeChoice.setAdapter(adapter);

        //handle cancel adding new recipe
        cancelButton.setOnClickListener((View v) ->
        {
            Intent goBackToRecipes = new Intent(AddRecipeActivity.this,MyRecipes.class);
            startActivity(goBackToRecipes);

        });

        //handle next click
        nextPageButton.setOnClickListener((View v) ->
        {
            Intent addRecipeSteps = new Intent(AddRecipeActivity.this, RecipeStepsActivity.class);
            startActivity(addRecipeSteps);
        });

    }
}
