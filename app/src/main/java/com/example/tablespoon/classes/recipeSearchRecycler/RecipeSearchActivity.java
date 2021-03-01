package com.example.tablespoon.classes.recipeSearchRecycler;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablespoon.AddRecipeActivity;
import com.example.tablespoon.R;
import com.example.tablespoon.classes.ParseJSONResults;
import com.example.tablespoon.classes.RapidAPIClient;
import com.example.tablespoon.classes.RapidAPIEndpoints;
import com.example.tablespoon.classes.Recipe;
import com.example.tablespoon.classes.myRecipeRecycler.RecyclerAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class RecipeSearchActivity extends AppCompatActivity
{
    private static final String TAG = RecipeSearchActivity.class.getName();

    private SearchView mRecipeSearchBar;
    private RecyclerView mRecipeSearchRecyclerView;
    private RecipeSearchRecyclerAdapter mRecyclerSearchViewAdapter;
    private RecyclerView.LayoutManager mRecyclerSearchViewLayoutManager;
    private Button mSearchRecipe;
    private String mRecipeSearchJsonResult = "";

    ArrayList<RecipeSearch> mRecipeSearches;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_search_view_layout);

        //inflate ui elements
        mRecipeSearchBar = (SearchView) findViewById(R.id.search_for_recipes);
        mSearchRecipe = (Button) findViewById(R.id.search_recipes_button);

        mSearchRecipe.setOnClickListener((View v)->
        {
            Toast.makeText(this,"Button Clicked", Toast.LENGTH_LONG).show();
            Log.i(TAG,"Button Clicked ");

            Thread searchRecipesThread = new Thread(){
                @Override
                public void run() {
                    searchButtonOnclick();
                }
            };

            Thread parseRecipeSearchThread = new Thread()
            {
                @Override
                public void run() {
                    //initialize list
                    mRecipeSearches = new ArrayList<>();

                    //call parse method
                    mRecipeSearches = ParseJSONResults.parseSearchRecipeResults(mRecipeSearchJsonResult);

                    //log the recipes to see if get works
                    for(RecipeSearch recipe: mRecipeSearches)
                    {
                        Log.i(TAG, recipe.toString());
                    }
                }
            };

            Thread prepareRecycler = new Thread()
            {
                @Override
                public void run() {
                    //log the recipes to see if get works
                    buildRecipeSearchRecyclerView();

                }
            };


            //start and join threads
            searchRecipesThread.start();

            //join searchRecipe
            try {
                searchRecipesThread.join();
            } catch (InterruptedException e) {
                Log.i(TAG,e.toString());
            }

            parseRecipeSearchThread.start();

            //join parseRecipeThread
            try {
                parseRecipeSearchThread.join();
            } catch (InterruptedException e) {
                Log.i(TAG,e.toString());
            }

            //start building the recycler
            prepareRecycler.start();


        });
    }


    private void searchButtonOnclick()
    {
        String recipeToSearch = mRecipeSearchBar.getQuery().toString().trim();
        Log.i(TAG,"Searching for: " + recipeToSearch);

        //url currently = "search?query="
        String url = RapidAPIEndpoints.SEARCH;


        String[] querries = recipeToSearch.split(" ");
        ArrayList<String> querriesArray= new ArrayList<>(Arrays.asList(querries));

        if(querriesArray.size() > 1)
        {
            for(int i = 0; i < querriesArray.size();i++)
            {
                if(i == (querriesArray.size() - 1))
                {
                    url += querriesArray.get(i);
                }
                else
                {
                    url += querriesArray.get(i) + "%20";
                }
            }
        }

        url += "&number=20";
        Log.i(TAG,"Using URL: " + url);

        //Toast.makeText(this,"Using URL: " + url, Toast.LENGTH_LONG).show();

        RapidAPIClient client = new RapidAPIClient();

        try {
            mRecipeSearchJsonResult = client.searchRecipe(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRecipeInfo(String recipeResult, String urlParameter) throws InterruptedException {
        final String[] recipeResutFinal = {recipeResult};
        Thread recipeInfo = new Thread()
        {
            @Override
            public void run()
            {

                try
                {
                    //create new client
                    RapidAPIClient client = new RapidAPIClient();
                    recipeResutFinal[0] = client.getRecipe(urlParameter);
                }
                catch (IOException e) {
                    Log.i(TAG, e.toString());
                }
            }
        };

        Thread parseRecipeInfo = new Thread()
        {
            @Override
            public void run()
            {

                //Create recipe from parsed result using copy constructor
                Recipe parsedRecipe = null;

                parsedRecipe = new Recipe((Recipe) ParseJSONResults.parseExtractRecipe(recipeResutFinal[0]));

                //log the searched recipe
                Log.i(TAG, "Chosen Recipe: " + parsedRecipe.toString());

                //print the results

                //create an intent to Add New Recipe with INSTANCE = "add_recipe_search"
                Intent intent = new Intent(RecipeSearchActivity.this, AddRecipeActivity.class);
                intent.putExtra("INSTANCE","add_recipe_search");
                intent.putExtra("RECIPE",parsedRecipe);

                startActivity(intent);
            }
        };

        //start getting recipeInfo thread
        recipeInfo.start();
        //join it
        recipeInfo.join();
        //start parsing recipe thread
        parseRecipeInfo.start();

    }



    public void buildRecipeSearchRecyclerView()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                mRecipeSearchRecyclerView = (RecyclerView) findViewById(R.id.recipe_search_recyclerView);
                mRecipeSearchRecyclerView.setHasFixedSize(true);
                mRecyclerSearchViewLayoutManager = new LinearLayoutManager(RecipeSearchActivity.this);
                mRecyclerSearchViewAdapter = new RecipeSearchRecyclerAdapter(mRecipeSearches);
                mRecipeSearchRecyclerView.setLayoutManager(mRecyclerSearchViewLayoutManager);
                mRecipeSearchRecyclerView.setAdapter(mRecyclerSearchViewAdapter);
                mRecyclerSearchViewAdapter.notifyDataSetChanged();

                //onClick listener implementation for recipe search recycler view
                mRecyclerSearchViewAdapter.setOnItemClickListener(new RecipeSearchRecyclerAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(int position)  {

                        String getRecipeResult = "";
                        //get url of recipe clicked on
                        String recipeUrl = mRecipeSearches.get(position).getURL();

                        //BUILD URL TO PASS TO RapidAPIClient.getRecipe
                        String urlParameter = "";

                        //make url object
                        try
                        {
                            URL urlObject = new URL(recipeUrl);

                            //get the protocol
                            String protocol = urlObject.getProtocol();
                            if(protocol.equals("http"))
                            {
                                urlParameter += RapidAPIEndpoints.EXTRACT_HTTP;
                            }
                            else
                            {
                                urlParameter += RapidAPIEndpoints.EXTRACT_HTTPS;
                            }
                            //get the host
                            urlParameter += urlObject.getHost() + "%2f";

                            //get the path
                            String path = urlObject.getPath();
                            //split the path by "/"
                            String[] pathSplit = path.split("/");
                            ArrayList<String> pathSplitArray = new ArrayList<>(Arrays.asList(pathSplit));
                            if(pathSplitArray.size() > 0)
                            {
                                for(int i=0;i<pathSplitArray.size();i++)
                                {
                                    if(i == (pathSplitArray.size() - 1))
                                    {
                                        urlParameter += pathSplitArray.get(i);
                                    }
                                    else
                                    {
                                        urlParameter += pathSplitArray.get(i) +"%2F";
                                    }
                                }
                            }


                        }catch (MalformedURLException exception)
                        {
                            Log.i(TAG,exception.toString());
                        }

                        try
                        {
                            getRecipeInfo(getRecipeResult,urlParameter);

                        }catch (InterruptedException e)
                        {
                            Log.i(TAG, e.toString());
                        }

                    }
                });

            }
        });

    }


}
//extract?url=https%3A%2F%2Fwww.bbcgoodfood.com%2Frecipes%2F1507675%2Fsummer-vegetable-curry"
//https://www.bbcgoodfood.com/recipes/1507675/summer-vegetable-curry
//https://lmld.org/slow-cooker-bbq-chicken/
//https://www.julieseatsandtreats.com/crock-pot-smokey-bbq-shredded-chicken-sandwich-recipe/
