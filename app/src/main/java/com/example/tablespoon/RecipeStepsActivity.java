package com.example.tablespoon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tablespoon.classes.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsActivity extends AppCompatActivity
{
    //varibales
    private EditText recipeStepField;
    private Button addStepButton;
    private ListView recipeStepsView;
    private ArrayAdapter stepsViewAdapter;
    private Button cancelAddRecipe;
    private Button previousPage;
    private Button doneAddingRecipe;
    private TextView addStepsLabel;


    private Recipe completeRecipe;
    private ArrayList<String> steps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_steps_layout);

        //bind ui elements to variables
        recipeStepField = (EditText) findViewById(R.id.add_step_field);
        addStepButton = (Button) findViewById(R.id.add_step_button);
        recipeStepsView = (ListView) findViewById(R.id.steps_view);
        stepsViewAdapter = new ArrayAdapter(RecipeStepsActivity.this, android.R.layout.simple_list_item_1,steps);
        cancelAddRecipe = (Button) findViewById(R.id.cancel_add_recipe_button2);
        previousPage = (Button) findViewById(R.id.add_recipe_first_page);
        doneAddingRecipe = (Button) findViewById(R.id.done_adding_recipe);
        addStepsLabel = (TextView) findViewById(R.id.add_steps_label);

        //get the intent from the AddRecipeActivity
        completeRecipe = new Recipe((Recipe) getIntent().getSerializableExtra("RECIPE"));

        //set the text for the lable
        addStepsLabel.setText("Add steps for recipe: " + completeRecipe.getName());

        //handle cancel click
        cancelAddRecipe.setOnClickListener((View v)->
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
                            Intent cancelAddRecipe2 = new Intent(RecipeStepsActivity.this, MyRecipes.class);
                            startActivity(cancelAddRecipe2);
                        }
                    })
                    .setNegativeButton("No",null)                                              //the no button does nothing
                    .show();                                                                                 //show the dialog
        });

        //handle previous click
        previousPage.setOnClickListener((View v)->
        {
            Intent previousAddRecipePage = new Intent(RecipeStepsActivity.this, AddRecipeActivity.class);
            previousAddRecipePage.putExtra("FROM","RECIPE_STEPS_ACTIVITY");
            previousAddRecipePage.putExtra("RECIPE",completeRecipe);
            startActivity(previousAddRecipePage);
        });

        //handle add ingredient
        addStepButton.setOnClickListener((View v) ->
        {
            String currentStep = "";
            if((currentStep = recipeStepField.getText().toString().trim()).length() > 0)
            {
                steps.add(currentStep);
                recipeStepField.setText("");
                stepsViewAdapter.notifyDataSetChanged();

            }

        });

        //set the adapter for the listview
        recipeStepsView.setAdapter(stepsViewAdapter);


        //handle deleting an ingredient
        recipeStepsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int removedStep = position;

                new AlertDialog.Builder(RecipeStepsActivity.this)
                        .setIcon(android.R.drawable.ic_delete)                                                      //choose the delete style dialog
                        .setTitle("Delete Item")                                                                    //add the title for the dialog
                        .setMessage("Are you sure you want to delete the step ' " + steps.get(removedStep) + "'")   //add the message
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {                       //add the yes button
                            @Override
                            public void onClick(DialogInterface dialog, int which)                                      //what happens when the yes button is clicked
                            {
                                //remove the item from the ingredients list
                                steps.remove(removedStep);

                                //notify the change
                                stepsViewAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No",null)                                              //the no button does nothing
                        .show();                                                                                 //show the dialog

                return true;
            }
        });

        doneAddingRecipe.setOnClickListener((View v) ->
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
        });
    }

    private boolean validateRecipeSteps()
    {
        return steps.size() > 0;
    }
}
