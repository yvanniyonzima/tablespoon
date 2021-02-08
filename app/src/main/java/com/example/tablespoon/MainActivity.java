package com.example.tablespoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.tablespoon.classes.FirebaseDatabaseHelper;
import com.example.tablespoon.classes.Recipe;
import com.example.tablespoon.classes.RecipeCardItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //read the data from firebase
        new FirebaseDatabaseHelper("users/yvanniyonzima/recipes").readRecipe(new FirebaseDatabaseHelper.DataStatus() {

            @Override
            public void DataIsLoaded(List<Recipe> recipes, List<String> keys) {

                //create list to pass to MyRecipeActivity
                ArrayList<Recipe> userRecipes = new ArrayList<>();
                boolean copyData = userRecipes.addAll(recipes);

                for(Recipe recipe : userRecipes)
                {
                    Log.d("MAIN",recipe.toString());
                }

                if(copyData)
                {
                    Intent toRecipeActivity = new Intent(MainActivity.this, MyRecipesActivity.class);
                    toRecipeActivity.putExtra("RECIPES",userRecipes);
                    startActivity(toRecipeActivity);
                }

            }

            @Override
            public void DataIsInserted() {}

            @Override
            public void DataIsUpdated() {}

            @Override
            public void DataIsDeleted() {}

            @Override
            public void RecipeIsLoaded(Recipe recipe) {

            }
        });
    }
}