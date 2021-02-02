package com.example.tablespoon;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tablespoon.classes.Recipe;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity
{
    //ui variables
    private EditText recipeNameField;
    private Spinner recipeTypeChoice;
    private EditText servingsField;
    private EditText recipeDescriptionField;
    private EditText addIngredientField;
    private Button addIngredientButton;
    private ListView ingredientsView;
    private ArrayAdapter ingredientsViewAdapter;
    private Button nextPageButton;
    private Button cancelButton;

    //data
    private String recipeName = "";
    private String recipeType = "";
    private int recipeTypeIndex = 0;
    private String servings = "";
    private String recipeDescription = "";
    private ArrayList<String> ingredients = new ArrayList<>();
    private Recipe recipe;

    //finish implementing in onCreate
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        Log.i(AddRecipeActivity.class.getName(), "Loading Saved Instance");
        outState.putString("RECIPE_NAME", recipeName);
        outState.putInt("RECIPE_TYPE_INDEX", recipeTypeIndex);
        outState.putString("RECIPE_SERVINGS", servings);
        outState.putString("RECIPE_DESCRIPTION", recipeDescription);
        outState.putStringArrayList("INGREDIENTS", ingredients);

        super.onSaveInstanceState(outState);

    }

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

        //create adapter object for the ingredient list view
        ingredientsViewAdapter = new ArrayAdapter(AddRecipeActivity.this, android.R.layout.simple_list_item_1,ingredients);

        //add recipe choices to spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> recipeTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        recipeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        recipeTypeChoice.setAdapter(recipeTypeAdapter);

        //check which activity launched this one
        if(getIntent().getStringExtra("FROM").equals("RECIPE_STEPS_ACTIVITY"))
        {
            recipe = new Recipe((Recipe) getIntent().getSerializableExtra("RECIPE"));
            recipeNameField.setText(recipe.getName());
            servingsField.setText(recipe.getServing());
            recipeDescriptionField.setText(recipe.getDescription());
            ingredients.addAll(recipe.getIngredients());
            ingredientsViewAdapter.notifyDataSetChanged();
            int spinnerPosition = recipeTypeAdapter.getPosition(recipe.getType());
            recipeTypeChoice.setSelection(spinnerPosition);

        }

        //handle cancel adding new recipe
        cancelButton.setOnClickListener((View v) ->
        {
            Intent goBackToRecipes = new Intent(AddRecipeActivity.this,MyRecipes.class);
            startActivity(goBackToRecipes);

        });

        //handle next click
        nextPageButton.setOnClickListener((View v) ->
        {
            recipeName = recipeNameField.getText().toString().trim();
            servings = servingsField.getText().toString().trim();
            recipeDescription = recipeDescriptionField.getText().toString().trim();
            if(!validateRecipeName())
            {
                new AlertDialog.Builder(AddRecipeActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)                                                  //choose the delete style dialog
                        .setTitle("No recipe name provided")                                                               //add the title for the dialog
                        .setMessage("Please enter a recipe name and try again")
                        .setCancelable(true)
                        .show();
            }
            else if(!validateRecipeType())
            {
                new AlertDialog.Builder(AddRecipeActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)                                                  //choose the delete style dialog
                        .setTitle("No Recipe Type Selected.")                                                               //add the title for the dialog
                        .setMessage("Please select a recipe type and try again")
                        .setCancelable(true)
                        .show();
            }
            else if(!validateServings())
            {
                new AlertDialog.Builder(AddRecipeActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)                                                  //choose the delete style dialog
                        .setTitle("Wrong Input")                                                               //add the title for the dialog
                        .setMessage("Please enter a number")
                        .setCancelable(true)
                        .show();
            }
            else if(!validateIngredientsList())
            {
                new AlertDialog.Builder(AddRecipeActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)                                                  //choose the delete style dialog
                        .setTitle("No ingredients added")                                                               //add the title for the dialog
                        .setMessage("Please add ingredients and try again")
                        .setCancelable(true)
                        .show();
            }
            else
            {
                recipe = new Recipe();
                recipe.setName(recipeName);
                recipe.setType(recipeType);
                recipe.setServing(servings);
                recipe.setDescription(recipeDescription);
                recipe.setIngredients(ingredients);
                Intent addRecipeSteps = new Intent(AddRecipeActivity.this, RecipeStepsActivity.class);
                addRecipeSteps.putExtra("RECIPE",recipe);
                startActivity(addRecipeSteps);
            }

        });

        //handle getting recipe type from spinner
        recipeTypeChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                recipeType = parent.getItemAtPosition(position).toString().trim();
                recipeTypeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //handle add ingredient
        addIngredientButton.setOnClickListener((View v) ->
        {
            String currentIngredient = "";
            if((currentIngredient = addIngredientField.getText().toString().trim()).length() > 0)
            {
                ingredients.add(currentIngredient);
                addIngredientField.setText("");
                ingredientsViewAdapter.notifyDataSetChanged();

            }

        });

        //set the adapter for the listview
        ingredientsView.setAdapter(ingredientsViewAdapter);


        //handle deleting an ingredient
        ingredientsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int removedItem = position;

                new AlertDialog.Builder(AddRecipeActivity.this)
                        .setIcon(android.R.drawable.ic_delete)                                                  //choose the delete style dialog
                        .setTitle("Delete Item")                                                                //add the title for the dialog
                        .setMessage("Are you sure you want to delete " + ingredients.get(removedItem))          //add the message
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {                 //add the yes button
                            @Override
                            public void onClick(DialogInterface dialog, int which)                              //what happens when the yes button is clicked
                            {
                                //remove the item from the ingredients list
                                ingredients.remove(removedItem);

                                //notify the change
                                ingredientsViewAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No",null)                                              //the no button does nothing
                        .show();                                                                                 //show the dialog

                return true;
            }
        });
    }

    //validation of textnames
    private boolean validateRecipeName()
    {
        return recipeName.length() > 0;
    }

    private boolean validateIngredientsList()
    {
        return ingredients.size() > 0;
    }

    private boolean validateRecipeType()
    {
        return recipeType.length() > 0;
    }

    private boolean validateServings()
    {
        if(servings.length() > 0)
        {
            try
            {
                Integer.parseInt(servings);
                return true;
            }
            catch (NumberFormatException exception)
            {
                return false;
            }
        }
        return true;
    }
}
