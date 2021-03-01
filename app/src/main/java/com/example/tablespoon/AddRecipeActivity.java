package com.example.tablespoon;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tablespoon.classes.FirebaseDatabaseHelper;
import com.example.tablespoon.classes.Recipe;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity
{
    private static String TAG = AddRecipeActivity.class.getName();
    //ui variables
    private EditText mRecipeNameField;
    private Spinner mRecipeTypeChoice;
    private EditText mServingsField;
    private EditText mRecipeDescriptionField;
    private EditText mAddIngredientField;
    private Button mAddIngredientButton;
    private ListView mIngredientsView;
    private ArrayAdapter mIngredientsViewAdapter;
    private Button mNextPageButton;
    private Button mCancelButton;
    //data
    private String mRecipeName = "";
    private String mRecipeType = "";
    private int mRecipeTypeIndex = 0;
    private String mServings = "";
    private String mRecipeDescription = "";
    private ArrayList<String> mIngredients = new ArrayList<>();
    private Recipe mRecipe;

    private String mInstance = "";
    private String mCurrRecipeName = "";



    //finish implementing in onCreate
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        Log.i(AddRecipeActivity.class.getName(), "Loading Saved Instance");
        outState.putString("RECIPE_NAME", mRecipeName);
        outState.putInt("RECIPE_TYPE_INDEX", mRecipeTypeIndex);
        outState.putString("RECIPE_SERVINGS", mServings);
        outState.putString("RECIPE_DESCRIPTION", mRecipeDescription);
        outState.putStringArrayList("INGREDIENTS", mIngredients);

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_layout);

        //bind ui elements to variables
        mRecipeNameField = (EditText) findViewById(R.id.recipe_name_field);
        mRecipeTypeChoice = (Spinner) findViewById(R.id.recipe_type_choice);
        mServingsField = (EditText) findViewById(R.id.servings);
        mRecipeDescriptionField = (EditText) findViewById(R.id.description_field);
        mAddIngredientField = (EditText) findViewById(R.id.ingredient_field);
        mAddIngredientButton = (Button) findViewById(R.id.add_ingredient_button);
        mIngredientsView = (ListView) findViewById(R.id.ingredient_listView);
        mNextPageButton = (Button) findViewById(R.id.next_button);
        mCancelButton = (Button) findViewById(R.id.cancel_add_recipe_button);

        //create adapter object for the ingredient list view
        mIngredientsViewAdapter = new ArrayAdapter(AddRecipeActivity.this, android.R.layout.simple_list_item_1, mIngredients);

        //add recipe choices to spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> recipeTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        recipeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mRecipeTypeChoice.setAdapter(recipeTypeAdapter);

        //set the instance type:either new recipe or edit recipe
        mInstance = getIntent().getStringExtra("INSTANCE");
        mCurrRecipeName = getIntent().getStringExtra("RECIPE_NAME");
        Log.i(TAG,"Instance: " + mInstance);

        //check which activity launched this one
        if(mInstance.equals("edit_recipe") || mInstance.equals("add_recipe_search"))
        {

            //get the recipe
            mRecipe = new Recipe((Recipe) getIntent().getSerializableExtra("RECIPE"));

            //set the text in textboxes
            mRecipeNameField.setText(mRecipe.getName());
            mServingsField.setText(mRecipe.getServing());

            if(!mRecipe.getDescription().isEmpty() || mRecipe.getDescription() != null)
            {
                mRecipeDescriptionField.setText(mRecipe.getDescription());
            }
            mIngredients.addAll(mRecipe.getIngredients());
            mIngredientsViewAdapter.notifyDataSetChanged();
            if(!mRecipe.getName().isEmpty())
            {
                int spinnerPosition = recipeTypeAdapter.getPosition(mRecipe.getType());
                mRecipeTypeChoice.setSelection(spinnerPosition);
            }

        }
        else
        {

        }

        //handle cancel/save click
        if(mInstance.equals("edit_recipe"))
        {
            //change the text of cancel button to save
            mCancelButton.setText("Save");

            //cancel button is now save button
            mCancelButton.setOnClickListener((View v) ->
            {
                getValuesFromFields();
                if(validateAll())
                {
                    setRecipeVariables();
                    if(mCurrRecipeName.equals(mRecipe.getName()))
                    {
                        updateRecipe(mRecipe.getName());
                    }
                    else
                    {
                        deleteRecipe(mCurrRecipeName);
                        addNewRecipe(mRecipe.getName(), mRecipe);
                    }

                    Intent goToRecipe = new Intent(AddRecipeActivity.this, ViewRecipeActivity.class);
                    goToRecipe.putExtra("RECIPE",mRecipe);
                    startActivity(goToRecipe);
                }

            });
        }
        else
        {
            mCancelButton.setOnClickListener((View v) ->
            {
                Intent goBackToRecipes = new Intent(AddRecipeActivity.this, MyRecipesActivity.class);
                startActivity(goBackToRecipes);

            });

        }


        //handle next click
        mNextPageButton.setOnClickListener((View v) ->
        {
            getValuesFromFields();
            if(validateAll())
            {
                if(mInstance.equals("add_new_recipe"))
                {
                    mRecipe = new Recipe();
                }

                setRecipeVariables();

                Intent addRecipeSteps = new Intent(AddRecipeActivity.this, RecipeStepsActivity.class);
                addRecipeSteps.putExtra("INSTANCE",mInstance);
                addRecipeSteps.putExtra("RECIPE", mRecipe);
                addRecipeSteps.putExtra("RECIPE_NAME", mRecipe.getName());
                startActivity(addRecipeSteps);
            }

        });

        //handle getting recipe type from spinner
        mRecipeTypeChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                mRecipeType = parent.getItemAtPosition(position).toString().trim();
                mRecipeTypeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //handle add ingredient
        mAddIngredientButton.setOnClickListener((View v) ->
        {
            String currentIngredient = "";
            if((currentIngredient = mAddIngredientField.getText().toString().trim()).length() > 0)
            {
                mIngredients.add(currentIngredient);
                mAddIngredientField.setText("");
                mIngredientsViewAdapter.notifyDataSetChanged();

            }

        });

        //set the adapter for the listview
        mIngredientsView.setAdapter(mIngredientsViewAdapter);


        //handle deleting an ingredient
        mIngredientsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int removedItem = position;

                new AlertDialog.Builder(AddRecipeActivity.this)
                        .setIcon(android.R.drawable.ic_delete)                                                  //choose the delete style dialog
                        .setTitle("Delete Item")                                                                //add the title for the dialog
                        .setMessage("Are you sure you want to delete " + mIngredients.get(removedItem))          //add the message
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {                 //add the yes button
                            @Override
                            public void onClick(DialogInterface dialog, int which)                              //what happens when the yes button is clicked
                            {
                                //remove the item from the ingredients list
                                mIngredients.remove(removedItem);

                                //notify the change
                                mIngredientsViewAdapter.notifyDataSetChanged();

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
        return mRecipeName.length() > 0;
    }

    private boolean validateIngredientsList()
    {
        return mIngredients.size() > 0;
    }

    private boolean validateRecipeType()
    {
        return mRecipeType.length() > 0;
    }

    private boolean validateServings()
    {
        if(mServings.length() > 0)
        {
            try
            {
                Integer.parseInt(mServings);
                return true;
            }
            catch (NumberFormatException exception)
            {
                return false;
            }
        }
        return true;
    }

    private void getValuesFromFields()
    {
        mRecipeName = mRecipeNameField.getText().toString().trim();
        mServings = mServingsField.getText().toString().trim();
        mRecipeDescription = mRecipeDescriptionField.getText().toString().trim();
    }

    private void setRecipeVariables()
    {
        mRecipe.setName(mRecipeName);
        mRecipe.setType(mRecipeType);
        mRecipe.setServing(mServings);
        mRecipe.setDescription(mRecipeDescription);
        mRecipe.setIngredients(mIngredients);
    }

    private boolean validateAll()
    {
        if(!validateRecipeName())
        {
            new AlertDialog.Builder(AddRecipeActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)                                                  //choose the delete style dialog
                    .setTitle("No recipe name provided")                                                               //add the title for the dialog
                    .setMessage("Please enter a recipe name and try again")
                    .setCancelable(true)
                    .show();
            return false;
        }
        else if(!validateRecipeType())
        {
            new AlertDialog.Builder(AddRecipeActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)                                                  //choose the delete style dialog
                    .setTitle("No Recipe Type Selected.")                                                               //add the title for the dialog
                    .setMessage("Please select a recipe type and try again")
                    .setCancelable(true)
                    .show();
            return false;
        }
        else if(!validateServings())
        {
            new AlertDialog.Builder(AddRecipeActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)                                                  //choose the delete style dialog
                    .setTitle("Wrong Input")                                                               //add the title for the dialog
                    .setMessage("Please enter a number")
                    .setCancelable(true)
                    .show();
            return false;
        }
        else if(!validateIngredientsList())
        {
            new AlertDialog.Builder(AddRecipeActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)                                                  //choose the delete style dialog
                    .setTitle("No ingredients added")                                                               //add the title for the dialog
                    .setMessage("Please add ingredients and try again")
                    .setCancelable(true)
                    .show();
            return false;
        }
        else
        {
            return true;
        }

    }


    private void addNewRecipe(String recipeKey, Recipe recipeToAdd)
    {
        new FirebaseDatabaseHelper("users/yvanniyonzima/recipes").addRecipe(recipeKey.toLowerCase(),recipeToAdd, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Recipe> recipes, List<String> keys) { }

            @Override
            public void DataIsInserted()
            {
                Toast.makeText(AddRecipeActivity.this,"The recipe " + recipeToAdd.getName() + " has been successfully added", Toast.LENGTH_LONG).show();
            }
            @Override
            public void DataIsUpdated() {}

            @Override
            public void DataIsDeleted() {}

            @Override
            public void RecipeIsLoaded(Recipe recipe) {}
        } );

    }

    private void updateRecipe(String recipeKey)
    {
        new FirebaseDatabaseHelper("users/yvanniyonzima/recipes").updateRecipe(recipeKey.toLowerCase(), mRecipe, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Recipe> recipes, List<String> keys) {}

            @Override
            public void DataIsInserted() {}

            @Override
            public void DataIsUpdated()
            {
                Toast.makeText(AddRecipeActivity.this,"Recipe Saved!",Toast.LENGTH_LONG).show();
                Log.i(TAG,"Recipe updated successfully!");
            }

            @Override
            public void DataIsDeleted() {}

            @Override
            public void RecipeIsLoaded(Recipe recipe) {}
        });
    }

    private void deleteRecipe(String recipeKey)
    {
        new FirebaseDatabaseHelper("users/yvanniyonzima/recipes").deleteRecipe(recipeKey.toLowerCase(), new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Recipe> recipes, List<String> keys) {

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

                Log.i(TAG,"Recipe " + mCurrRecipeName + " deleted successfully");

            }

            @Override
            public void RecipeIsLoaded(Recipe recipe) {

            }
        });
    }
}
