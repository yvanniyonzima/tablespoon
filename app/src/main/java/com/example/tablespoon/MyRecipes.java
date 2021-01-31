package com.example.tablespoon;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.tablespoon.classes.FirebaseDatabaseHelper;
import com.example.tablespoon.classes.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRecipes extends AppCompatActivity
{
    private SearchView recipeSearchBar;
    private ScrollView recipesView;
    private ImageButton newRecipe;
    private LinearLayout recipesLayout;

    //class to hold recipe objects
    //private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_recipes);

        //bind UI elements to variables
        recipeSearchBar =(SearchView) findViewById(R.id.search_recipe);
        recipesView = (ScrollView) findViewById(R.id.recipe_page_scroll);
        newRecipe = (ImageButton) findViewById(R.id.add_recipe);
        recipesLayout = (LinearLayout) findViewById(R.id.recipes_lin_layout);

        //add a new recipe
        newRecipe.setOnClickListener((View V) ->
        {
            Intent newRecipeIntent = new Intent(MyRecipes.this,AddRecipeActivity.class);
            startActivity(newRecipeIntent);
        });

        //read the data
        new FirebaseDatabaseHelper().readRecipe(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Recipe> recipes, List<String> keys) {
                //System.out.println(recipe);
                Log.d("RECIPE",recipes.get(0).toString());
                displayRecipes(recipes);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });



    }

    //method to display all the recipes
    void displayRecipes(List<Recipe> recipes)
    {
        //loop through the recipes
        for (int i = 0;i<recipes.size();i++)
        {
            //for the image and the text views
            LinearLayout linearLayoutHoriz = new LinearLayout(recipesLayout.getContext());
            linearLayoutHoriz.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout linearLayoutVer = new LinearLayout(linearLayoutHoriz.getContext());
            linearLayoutVer.setOrientation(LinearLayout.VERTICAL);

            TextView textView = new TextView(linearLayoutVer.getContext());
            TextView textView2 = new TextView(linearLayoutVer.getContext());
            //get the name of the recipe
            textView.setText(recipes.get(i).getName());
            textView.setAutoSizeTextTypeUniformWithConfiguration(18,40,1, TypedValue.COMPLEX_UNIT_DIP);
            textView.setPadding(30,20,0,0);


            //get the type of the course
            textView2.setText(recipes.get(i).getType());
            textView2.setPadding(30,50,0,0);

            //using cardview to round corners
            CardView cardView = new CardView(linearLayoutHoriz.getContext());
            cardView.setRadius(25);

            ImageView imageView = new ImageView(cardView.getContext());
            imageView.setImageResource(R.drawable.burger);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setAdjustViewBounds(true);
            imageView.setMaxHeight(200);

            cardView.addView(imageView);

            //add text views to vertical linear layout
            linearLayoutVer.addView(textView);
            linearLayoutVer.addView(textView2);

            //add the image and vertical linear layout to horizontal linear layout
            linearLayoutHoriz.addView(cardView);
            linearLayoutHoriz.addView(linearLayoutVer);

            //add styling to horizontal linear layout
            linearLayoutHoriz.setBackground(getDrawable(R.drawable.recipe_search_bar));
            linearLayoutHoriz.setClickable(true);

            //add horizontal linear layout to scrollview vertical linear layout
            recipesLayout.addView(linearLayoutHoriz);
        }

    }


}




//to test the view
//        for (int i = 0;i<20;i++)
//        {
//
//
//            //for the image and the text views
//            LinearLayout linearLayoutHoriz = new LinearLayout(recipesLayout.getContext());
//            linearLayoutHoriz.setOrientation(LinearLayout.HORIZONTAL);
//
//            LinearLayout linearLayoutVer = new LinearLayout(linearLayoutHoriz.getContext());
//            linearLayoutVer.setOrientation(LinearLayout.VERTICAL);
//
//            TextView textView = new TextView(linearLayoutVer.getContext());
//            TextView textView2 = new TextView(linearLayoutVer.getContext());
//            textView.setText("Hello");
//            textView.setTextSize(40);
//            textView2.setText("World");
//
//            ImageView imageView = new ImageView(linearLayoutHoriz.getContext());
//            imageView.setImageResource(R.drawable.burger);
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            imageView.setAdjustViewBounds(true);
//            imageView.setMaxHeight(200);
//            //add text views to vertical linear layout
//            linearLayoutVer.addView(textView);
//            linearLayoutVer.addView(textView2);
//
//            //add the image and vertical linear layout to horizontal linear layout
//            linearLayoutHoriz.addView(imageView);
//            linearLayoutHoriz.addView(linearLayoutVer);
//
//            //add styling to horizontal linear layout
//            linearLayoutHoriz.setBackground(getDrawable(R.drawable.recipe_search_bar));
//            linearLayoutHoriz.setClickable(true);
//
//            //add horizontal linear layout to scrollview vertical linear layout
//            recipesLayout.addView(linearLayoutHoriz);
//        }