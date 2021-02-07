package com.example.tablespoon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablespoon.classes.FirebaseDatabaseHelper;
import com.example.tablespoon.classes.Recipe;
import com.example.tablespoon.classes.RecipeCardItem;
import com.example.tablespoon.classes.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyRecipesActivity extends AppCompatActivity
{
    private SearchView mRecipeSearchBar;
    private RecyclerView mRecipesRecyclerView;
    private RecyclerAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private ImageButton mNewRecipe;
    private LinearLayout recipesLayout;

    ArrayList<RecipeCardItem> recipeRecyclerItems;


    //class to hold recipe objects
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_recipes);


        //inflate searchbar and newRecipe button
        mRecipeSearchBar =(SearchView) findViewById(R.id.search_recipe);
        mNewRecipe = (ImageButton) findViewById(R.id.add_recipe);

        //read the data from firebase
        readDataFromFirebase();

        //add a new recipe
        mNewRecipe.setOnClickListener((View V) ->
        {
            Intent newRecipeIntent = new Intent(MyRecipesActivity.this,AddRecipeActivity.class);
            newRecipeIntent.putExtra("FROM","MY_RECIPES_ACTIVITY");
            startActivity(newRecipeIntent);
        });

    }

    public void removeItem(int position) {
        recipeRecyclerItems.remove(position);
        mRecyclerViewAdapter.notifyItemRemoved(position);
    }

    public void buildRecyclerView()
    {
        mRecipesRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerView);
        mRecipesRecyclerView.setHasFixedSize(true);
        mRecyclerViewLayoutManager = new LinearLayoutManager(MyRecipesActivity.this);
        mRecyclerViewAdapter = new RecyclerAdapter(recipeRecyclerItems);
        mRecipesRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mRecipesRecyclerView.setAdapter(mRecyclerViewAdapter);

        //on click listener for the recycler view adapter
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {

            @Override
            public void onDeleteClick(int position) {
                new AlertDialog.Builder(MyRecipesActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Recipe")
                        .setMessage("Are you sure you want to delete this recipe?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //delete the recipe and remove the item from the recycler view
                                removeItem(position);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
    }

    public void addRecyclerViewItems(List<Recipe> recipes)
    {
        //initialize the arraylist for the recycler view items
        recipeRecyclerItems = new ArrayList<>();
        for(Recipe currRecipe: recipes)
        {
            RecipeCardItem item = new RecipeCardItem(R.drawable.ic_android,currRecipe.getName(), currRecipe.getType(),currRecipe.getDescription());
            recipeRecyclerItems.add(item);
            Log.i("RECIPE", item.toString());

        }
    }

    public void readDataFromFirebase()
    {
        //read the data from firebase
        new FirebaseDatabaseHelper("users/yvanniyonzima/recipes").readRecipe(new FirebaseDatabaseHelper.DataStatus() {

            @Override
            public void DataIsLoaded(List<Recipe> recipes, List<String> keys) {
                //System.out.println(recipe);
                Log.d("RECIPE",recipes.get(0).toString());

                //populate the card items array list
                addRecyclerViewItems(recipes);

                //build the recycler view
                buildRecyclerView();

                //test to see if recipe card items are present
                Log.i("RECIPE", "size of recipeRecyclerItems 1 = " + recipeRecyclerItems.size());
            }

            @Override
            public void DataIsInserted() {}
            @Override
            public void DataIsUpdated() {}
            @Override
            public void DataIsDeleted() {}
        });

    }
}
