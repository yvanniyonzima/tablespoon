package com.example.tablespoon.classes;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.*;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

//implementation idea from : https://loopj.com/android-async-http/
public class RapidAPIClient
{
    private static final String TAG = RapidAPIClient.class.getName();
    private static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/";
    private static final String KEY_NAME = "x-rapidapi-key";
    private static final String HOST_NAME = "x-rapidapi-host";
    private static final String KEY = "5bfa8ade7dmshd0ca52168f37411p1f1024jsnecb598fbbc8e";
    private static final String HOST = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
    private OkHttpClient okHttpClient = new OkHttpClient();

    private Request request;
    private Response response;

    private String mRelativeURL;
    private ArrayList<String> mParams;


    public RapidAPIClient() {}

    public String searchRecipe(String url) throws IOException {
        request = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .get()
                .addHeader(KEY_NAME, KEY)
                .addHeader(HOST_NAME, HOST)
                .build();

        //get the reponse object
        response = okHttpClient.newCall(request).execute();

        //convert reposne to json string
        String responseToString = response.body().string();

        Log.i(TAG,"Response String: " + responseToString);

        //return response
        return responseToString;

    }

    public String getRecipe(String url) throws IOException
    {
        String responseString = "";
        request = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .get()
                .addHeader(KEY_NAME, KEY)
                .addHeader(HOST_NAME, HOST)
                .build();

        response = okHttpClient.newCall(request).execute();

        responseString = response.body().string();

        //Log.i(TAG,"getRecipeResponse: " + responseString);

//        int maxLogSize = 1000;
//        for(int i = 0; i <= responseString.length() / maxLogSize; i++) {
//            int start = i * maxLogSize;
//            int end = (i+1) * maxLogSize;
//            end = end > responseString.length() ? responseString.length() : end;
//            Log.i(TAG,"getRecipeResponse part " + i + ": " + responseString.substring(start, end));
//        }


        return responseString;
    }

    private String getAbsoluteUrl(String relativeUrl)
    {
        return BASE_URL + relativeUrl;
    }
}

