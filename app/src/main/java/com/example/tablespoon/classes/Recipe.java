package com.example.tablespoon.classes;

import android.media.Image;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {

    private String description;
    private String name;
    private List<String> ingredients;
    private String serving;
    private List<String> steps;
    private String type;
    //private Image recipePicture;


    public Recipe() {}

    public Recipe(String name,String description, List<String> ingredients, String serving, List<String> steps,String type)
    {
        this.description = description;
        this.ingredients = ingredients;
        this.serving = serving;
        this.steps = steps;
        this.name = name;
        this.type = type;
    }

    public Recipe(Recipe otherRecipe)
    {
        this(otherRecipe.getName(), otherRecipe.getDescription(), otherRecipe.getIngredients(), otherRecipe.getServing(), otherRecipe.getSteps(), otherRecipe.getType());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getServing() {
        return serving;
    }

    public String getType() {
        return type;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "description='" + description + "\n" +
                ", name='" + name + "\n" +
                ", ingredients=" + ingredients + "\n" +
                ", serving='" + serving + "\n" +
                ", steps=" + steps +
                '}';
    }
}
