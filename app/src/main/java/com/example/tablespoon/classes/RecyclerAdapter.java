package com.example.tablespoon.classes;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablespoon.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>
{

    private ArrayList<RecipeCardItem> mRecipeCardList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mRecipeCardImageView;
        public TextView mRecipeCardTextView1;
        public TextView mRecipeCardTextView2;
        public TextView mRecipeDescriptionTextView2;
        public ImageView mDeleteRecyclerItem;
        public RecyclerViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mRecipeCardImageView = (ImageView) itemView.findViewById(R.id.recipe_card_image);
            mRecipeCardTextView1 = (TextView) itemView.findViewById(R.id.recipe_card_text_1);
            mRecipeCardTextView2 = (TextView) itemView.findViewById(R.id.recipe_card_text_2);
            mRecipeDescriptionTextView2 = (TextView) itemView.findViewById(R.id.recipe_description_card);
            mDeleteRecyclerItem = (ImageView) itemView.findViewById(R.id.delete_recycler_item);


            //for deleting a recycler item
            mDeleteRecyclerItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecyclerAdapter(ArrayList<RecipeCardItem> itemList)
    {
        mRecipeCardList = new ArrayList<>();
        mRecipeCardList = itemList;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_item, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view, mListener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecipeCardItem currentItem = mRecipeCardList.get(position);

        holder.mRecipeCardImageView.setImageResource(currentItem.getImageResource());
        holder.mRecipeCardTextView1.setText(currentItem.getRecipeCardText1());
        holder.mRecipeCardTextView2.setText(currentItem.getRecipeCardText2());
        holder.mRecipeDescriptionTextView2.setText(currentItem.getRecipeDescriptionText());

    }

    @Override
    public int getItemCount() {
        return mRecipeCardList.size();
    }



}