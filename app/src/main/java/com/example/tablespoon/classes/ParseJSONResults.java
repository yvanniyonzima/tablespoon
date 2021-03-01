package com.example.tablespoon.classes;

import android.util.Log;

import com.example.tablespoon.R;
import com.example.tablespoon.classes.recipeSearchRecycler.RecipeSearch;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ParseJSONResults {
    private static final String TAG = ParseJSONResults.class.getName();

    public static ArrayList<RecipeSearch> parseSearchRecipeResults(String JSONString) {
        ArrayList<RecipeSearch> recipeSearches = new ArrayList<>();
        String imageBaseURI = "";

        try {
            JSONObject jsonObject = new JSONObject(JSONString);
            imageBaseURI = jsonObject.getString("baseUri");
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject recipeResult = results.getJSONObject(i);
                String recipeName = recipeResult.getString("title");
                String recipeServings = recipeResult.getString("servings");
                String recipeTime = recipeResult.getString("readyInMinutes");
                String recipeUrl = recipeResult.getString("sourceUrl");
                String recipeImageUrl = imageBaseURI + recipeResult.getString("image");

                RecipeSearch currRecipe = new RecipeSearch(recipeName, recipeTime, recipeServings, recipeUrl, recipeImageUrl);
                recipeSearches.add(currRecipe);

                Log.i(TAG, "Name: " + recipeName + "\n"
                        + "Servings: " + recipeServings + "\n"
                        + "Cook Time: " + recipeTime + "\n\n");
            }


        } catch (JSONException jsonException) {
            Log.i(TAG, jsonException.toString());
        }

        return recipeSearches;
    }

    public static Recipe parseExtractRecipe(String extractedJSONString)
    {
        Recipe recipe;
        String name = "";
        String recipeDescription = "";
        List<String> ingredients = new ArrayList<>();
        List<String> steps = new ArrayList<>();
        String serving = "";
        String type = "";


        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(extractedJSONString);
        } catch (JSONException e) {
           Log.i(TAG, e.toString());
        }
        try {
            name = jsonObject.getString("title");
        } catch (JSONException e) {
            Log.i(TAG, e.toString());
        }

        try {
            serving = jsonObject.getString("servings");
        } catch (JSONException e) {
            Log.i(TAG, e.toString());
        }

        try {
            recipeDescription = jsonObject.getString("summary");
        } catch (JSONException e) {
            Log.i(TAG, e.toString());
        }

        //get the dish type array

        try {
            type = jsonObject.getJSONArray("dishTypes").getString(0);
        } catch (JSONException e) {
            Log.i(TAG, e.toString());
        }


        JSONArray extendedIngredients = null;
        try {
            extendedIngredients = jsonObject.getJSONArray("extendedIngredients");
            for (int i = 0; i < extendedIngredients.length(); i++)
            {
                JSONObject currIngredientInfo = extendedIngredients.getJSONObject(i);
                //Log.i(TAG, "Addint ingredient: " + currIngredientInfo.getString("step"));
                ingredients.add(currIngredientInfo.getString("original"));
            }
        } catch (JSONException e) {
            Log.i(TAG, e.toString());
        }


        //extract the steps
        JSONArray analyzedSteps = null;
        try {
            analyzedSteps = jsonObject.getJSONArray("analyzedInstructions").getJSONObject(0).getJSONArray("steps");
            Log.i(TAG, "analyzedSteps: " + analyzedSteps.toString());
            for (int i = 0; i < analyzedSteps.length(); i++)
            {
                JSONObject currInstructionInfo = analyzedSteps.getJSONObject(i);
                Log.i(TAG, "Addint step: " + currInstructionInfo.getString("step"));
                steps.add(currInstructionInfo.getString("step"));
            }
        } catch (JSONException e) {
            Log.i(TAG, e.toString());
        }



        recipe = new Recipe(name, recipeDescription, ingredients, serving, steps, type);


        return recipe;
    }

}
