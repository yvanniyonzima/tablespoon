package com.example.tablespoon.classes.recipeSearchRecycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablespoon.R;


import java.util.ArrayList;

public class RecipeSearchRecyclerAdapter extends RecyclerView.Adapter<RecipeSearchRecyclerAdapter.RecyclerViewHolderSearch>
{
    private ArrayList<RecipeSearch> mRecipeSearchCardList;
    private OnItemClickListener mSearchListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mSearchListener = listener;
    }



    public static class RecyclerViewHolderSearch extends  RecyclerView.ViewHolder
    {
        public ImageView mRecipeCardImageView;
        public TextView mRecipeCardName;
        public TextView mRecipeCardServings;
        public TextView mRecipeCardCookTime;

        public RecyclerViewHolderSearch(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mRecipeCardImageView = (ImageView) itemView.findViewById(R.id.recipe_search_card_image);
            mRecipeCardName = (TextView) itemView.findViewById(R.id.recipe_search_card_name);
            mRecipeCardServings = (TextView) itemView.findViewById(R.id.recipe_search_card_servings);
            mRecipeCardCookTime = (TextView) itemView.findViewById(R.id.recipe_search_cooktime_card);

            //for viewing a recipe
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecipeSearchRecyclerAdapter(ArrayList<RecipeSearch> recipeSearchList)
    {
        mRecipeSearchCardList = new ArrayList<>();
        mRecipeSearchCardList = recipeSearchList;
    }
    @NonNull
    @Override
    public RecyclerViewHolderSearch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_search_card_item, parent, false);
        RecyclerViewHolderSearch viewHolderSearch = new RecyclerViewHolderSearch(view, mSearchListener);
        return  viewHolderSearch;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderSearch holder, int position) {
        RecipeSearch currentRecipe = mRecipeSearchCardList.get(position);

        holder.mRecipeCardName.setText(currentRecipe.getRecipeName());
        holder.mRecipeCardCookTime.setText("Cook Time: " + currentRecipe.getRecipeCookTime() + " minutes");
        holder.mRecipeCardServings.setText("Serves: " + currentRecipe.getServings());
        holder.mRecipeCardImageView.setImageResource(R.drawable.ic_android);
    }

    @Override
    public int getItemCount() {
        return mRecipeSearchCardList.size();
    }
}
