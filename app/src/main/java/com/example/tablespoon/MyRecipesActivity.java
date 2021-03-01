package com.example.tablespoon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablespoon.classes.FirebaseDatabaseHelper;
import com.example.tablespoon.classes.ParseJSONResults;
import com.example.tablespoon.classes.RapidAPIClient;
import com.example.tablespoon.classes.Recipe;
import com.example.tablespoon.classes.myRecipeRecycler.RecipeCardItem;
import com.example.tablespoon.classes.myRecipeRecycler.RecyclerAdapter;
import com.example.tablespoon.classes.recipeSearchRecycler.RecipeSearchActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyRecipesActivity extends AppCompatActivity
{
    private static final String TAG = MyRecipesActivity.class.getName();
    private SearchView mRecipeSearchBar;
    private RecyclerView mRecipesRecyclerView;
    private RecyclerAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private ImageButton mNewRecipe;
    private LinearLayout recipesLayout;

    ArrayList<RecipeCardItem> recipeRecyclerItems;
    ArrayList<Recipe> mRecipes;

    //class to hold recipe objects
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_recipes_layout);

        //inflate searchbar and newRecipe button
        mRecipeSearchBar =(SearchView) findViewById(R.id.search_recipe);
        mNewRecipe = (ImageButton) findViewById(R.id.add_recipe);

        //read the data from firebase
        readDataFromFirebase();

        //add a new recipe
        mNewRecipe.setOnClickListener((View V) ->
        {
            //Creating the instance of PopupMenu
            PopupMenu addOptionsMenu = new PopupMenu(MyRecipesActivity.this,mNewRecipe);
            //Inflating the Popup using xml file
            addOptionsMenu.getMenuInflater().inflate(R.menu.add_recipe_options,addOptionsMenu.getMenu());
            //registering popup with OnMenuItemClickListener
            addOptionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.add_recipe_manually)
                    {
                        Intent newRecipeIntent = new Intent(MyRecipesActivity.this,AddRecipeActivity.class);
                        newRecipeIntent.putExtra("INSTANCE","add_new_recipe");
                        startActivity(newRecipeIntent);
                    }
                    else
                    {
                        Intent searchRecipeIntent = new Intent(MyRecipesActivity.this, RecipeSearchActivity.class);
                        startActivity(searchRecipeIntent);
                    }
                    return true;
                }
            });

            addOptionsMenu.show();

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
            public void onItemClick(int position) {
                //get the recipe clicked on
                Recipe recipeToView = mRecipes.get(position);

                Log.i("MyRecipeActivity",recipeToView.toString());

                //create an intent to the recipe view
                Intent toRecipeView = new Intent(MyRecipesActivity.this, ViewRecipeActivity.class);
                toRecipeView.putExtra("RECIPE",recipeToView);
                startActivity(toRecipeView);
            }

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
                                //get the recipe name to access the key in firebase
                                    //the key is the name of the recipe in lowercase
                                String name = mRecipes.get(position).getName();
                                String key = name.toLowerCase();

                                //remove the item from the recycler view
                                removeItem(position);

                                //remove the item from firebase
                                new FirebaseDatabaseHelper("users/yvanniyonzima/recipes").deleteRecipe(key, new FirebaseDatabaseHelper.DataStatus() {
                                    @Override
                                    public void DataIsLoaded(List<Recipe> recipes, List<String> keys) {}

                                    @Override
                                    public void DataIsInserted() {

                                    }

                                    @Override
                                    public void DataIsUpdated() {

                                    }

                                    @Override
                                    public void DataIsDeleted()
                                    {
                                        Toast.makeText(MyRecipesActivity.this,"The recipe " + name + " has been successfully deleted", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void RecipeIsLoaded(Recipe recipe) {

                                    }
                                });
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

                //populate the recipes arrayList
                mRecipes = new ArrayList<>();
                mRecipes.addAll(recipes);

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
            @Override
            public void RecipeIsLoaded(Recipe recipe) {}
        });

    }

}
