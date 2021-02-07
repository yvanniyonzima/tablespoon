package com.example.tablespoon.classes;

//source for code : https://www.youtube.com/watch?v=eCfJMseN0-8
import android.app.Application;
import android.security.keystore.KeyNotYetValidException;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Recipe> recipes = new ArrayList<>();



    public interface DataStatus
    {
        //the methods in this interface are to be implemented for CRUD operations
        //they are overloaded depending on the class they are used fin

        //FOR MyRecipes Class
        void DataIsLoaded(List<Recipe> recipes, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }

    public FirebaseDatabaseHelper(String reference)
    {
        mDatabase = FirebaseDatabase.getInstance();
        //mDatabase.setPersistenceEnabled(true);
        mReference = mDatabase.getReference(reference);
    }

    //functions for CRUD operations for recipes
    public void readRecipe(final DataStatus dataStatus)
    {
        //every time the recipe is update, this listener will call the below onDataChange method
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
//                recipes.clear();
//                List<String> keys = new ArrayList<>();
//                for(DataSnapshot keyNode : dataSnapshot.getChildren())
//                {
//                    keys.add(keyNode.getKey());
//                    Recipe recipe = keyNode.getValue(Recipe.class);
//                    recipes.add(recipe);
//                }
//                dataStatus.DataIsLoaded(recipes,keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipes.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren())
                {
                    keys.add(keyNode.getKey());
                    Recipe recipe = keyNode.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                dataStatus.DataIsLoaded(recipes,keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addRecipe(Recipe recipe, final DataStatus dataStatus, String key)
    {
        //DataStatus is the interface
        //String key = mReference.push().getKey(); //this makes a child node and gives it a unique key
        mReference.child(key.toLowerCase()).setValue(recipe)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        //display success message
                        dataStatus.DataIsInserted();
                    }
                });
    }
}
