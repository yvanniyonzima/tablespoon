package com.example.tablespoon;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tablespoon.classes.FirebaseDatabaseHelper;
import com.example.tablespoon.classes.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsActivity extends AppCompatActivity
{
    private final String TAG = RecipeStepsActivity.class.getName();
    //variables
    private EditText mRecipeStepField;
    private Button mAddStepButton;
    private ListView mRecipeStepsView;
    private ArrayAdapter mStepsViewAdapter;
    private Button mCancelAddRecipe;
    private Button mPreviousPage;
    private Button mDoneAddingRecipe;
    private TextView mAddStepsLabel;
    private String mInstance;


    private Recipe mCompleteRecipe;
    private String mRecipeName = "";
    private ArrayList<String> mSteps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_steps_layout);

        //bind ui elements to variables
        mRecipeStepField = (EditText) findViewById(R.id.add_step_field);
        mAddStepButton = (Button) findViewById(R.id.add_step_button);
        mRecipeStepsView = (ListView) findViewById(R.id.steps_view);
        mStepsViewAdapter = new ArrayAdapter(RecipeStepsActivity.this, android.R.layout.simple_list_item_1, mSteps);
        mCancelAddRecipe = (Button) findViewById(R.id.cancel_add_recipe_button2);
        mPreviousPage = (Button) findViewById(R.id.add_recipe_first_page);
        mDoneAddingRecipe = (Button) findViewById(R.id.done_adding_recipe);
        mAddStepsLabel = (TextView) findViewById(R.id.add_steps_label);

        //get the intent type
        mInstance = getIntent().getStringExtra("INSTANCE");
        mRecipeName = getIntent().getStringExtra("RECIPE_NAME");

        //get the recipe from the intent
        mCompleteRecipe = new Recipe((Recipe) getIntent().getSerializableExtra("RECIPE"));

        if(mInstance.equals("edit_recipe"))
        {
            //change cancel to save
            mCancelAddRecipe.setText("Previous");

            //make previous button invisible
            mPreviousPage.setVisibility(View.INVISIBLE);

            //view the current steps
            mSteps.addAll(mCompleteRecipe.getSteps());
            mStepsViewAdapter.notifyDataSetChanged();
        }

        //set the text for the label
        mAddStepsLabel.setText("Add steps for recipe: " + mCompleteRecipe.getName());

        //handle cancel click
        mCancelAddRecipe.setOnClickListener((View v)->
        {
            if(mInstance.equals("edit_recipe"))
            {
                goBack();
            }
            else
            {
                new AlertDialog.Builder(RecipeStepsActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)                                             //choose the delete style dialog
                        .setTitle("Cancel Process")                                                             //add the title for the dialog
                        .setMessage("Are you sure you want to cancel adding this recipe?")                      //add the message
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {                 //add the yes button
                            @Override
                            public void onClick(DialogInterface dialog, int which)                              //what happens when the yes button is clicked
                            {
                                //got back to users recipe list
                                Intent cancelAddRecipe2 = new Intent(RecipeStepsActivity.this, MyRecipesActivity.class);
                                startActivity(cancelAddRecipe2);
                            }
                        })
                        .setNegativeButton("No",null)                                              //the no button does nothing
                        .show();                                                                                 //show the dialog
            }

        });

        //handle previous click
        mPreviousPage.setOnClickListener((View v)->
        {
            goBack();
        });


        //handle add ingredient
        mAddStepButton.setOnClickListener((View v) ->
        {
            String currentStep = "";
            if((currentStep = mRecipeStepField.getText().toString().trim()).length() > 0)
            {
                mSteps.add(currentStep);
                mRecipeStepField.setText("");
                mStepsViewAdapter.notifyDataSetChanged();

            }

        });

        //set the adapter for the listview
        mRecipeStepsView.setAdapter(mStepsViewAdapter);


        //handle deleting an ingredient
        mRecipeStepsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int removedStep = position;

                new AlertDialog.Builder(RecipeStepsActivity.this)
                        .setIcon(android.R.drawable.ic_delete)                                                      //choose the delete style dialog
                        .setTitle("Delete Item")                                                                    //add the title for the dialog
                        .setMessage("Are you sure you want to delete the step ' " + mSteps.get(removedStep) + "'")   //add the message
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {                       //add the yes button
                            @Override
                            public void onClick(DialogInterface dialog, int which)                                      //what happens when the yes button is clicked
                            {
                                //remove the item from the ingredients list
                                mSteps.remove(removedStep);

                                //notify the change
                                mStepsViewAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No",null)                                              //the no button does nothing
                        .show();                                                                                 //show the dialog

                return true;
            }
        });

        mDoneAddingRecipe.setOnClickListener((View v) ->
        {
            if(!validateRecipeSteps())
            {
                new AlertDialog.Builder(RecipeStepsActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)                                                  //choose the delete style dialog
                        .setTitle("No Recipe Steps Added.")                                                               //add the title for the dialog
                        .setMessage("Please add recipe steps and try again")
                        .setCancelable(true)
                        .show();
            }
            else
            {
                //add the recipe steps to the Recipe object
                mCompleteRecipe.setSteps(mSteps);

                if(mInstance.equals("add_new_recipe"))
                {
                    addNewRecipe(mCompleteRecipe.getName(),mCompleteRecipe);
                }
                else
                {
                    if(mRecipeName.equals(mCompleteRecipe.getName()))
                    {
                        updateRecipe(mCompleteRecipe.getName(),mCompleteRecipe);
                    }
                    else
                    {
                       deleteRecipe(mRecipeName);
                       addNewRecipe(mCompleteRecipe.getName(), mCompleteRecipe);
                    }

                }

                Intent goBackToRecipes = new Intent(RecipeStepsActivity.this, MyRecipesActivity.class);
                startActivity(goBackToRecipes);
            }
        });
    }

    private boolean validateRecipeSteps()
    {
        return mSteps.size() > 0;
    }

    private void goBack()
    {
        Intent previousAddRecipePage = new Intent(RecipeStepsActivity.this, AddRecipeActivity.class);
        previousAddRecipePage.putExtra("INSTANCE","edit_recipe");
        previousAddRecipePage.putExtra("RECIPE", mCompleteRecipe);
        previousAddRecipePage.putExtra("RECIPE_NAME", mRecipeName);
        startActivity(previousAddRecipePage);

    }

    private void addNewRecipe(String recipeKey, Recipe recipeToAdd)
    {
        new FirebaseDatabaseHelper("users/yvanniyonzima/recipes").addRecipe(recipeKey.toLowerCase(),recipeToAdd, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Recipe> recipes, List<String> keys) { }

            @Override
            public void DataIsInserted()
            {
                Toast.makeText(RecipeStepsActivity.this,"The recipe " + recipeToAdd.getName() + " has been successfully added", Toast.LENGTH_LONG).show();
            }
            @Override
            public void DataIsUpdated() {}

            @Override
            public void DataIsDeleted() {}

            @Override
            public void RecipeIsLoaded(Recipe recipe) {}
        } );

    }

    private void updateRecipe(String recipeKey, Recipe recipe)
    {
        new FirebaseDatabaseHelper("users/yvanniyonzima/recipes").updateRecipe(recipeKey.toLowerCase(), recipe, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Recipe> recipes, List<String> keys) {}

            @Override
            public void DataIsInserted() {}

            @Override
            public void DataIsUpdated()
            {
                Toast.makeText(RecipeStepsActivity.this,"Recipe Saved!",Toast.LENGTH_LONG).show();
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

                Log.i(TAG,"Recipe " + mRecipeName+ " deleted successfully");

            }

            @Override
            public void RecipeIsLoaded(Recipe recipe) {

            }
        });
    }
}
