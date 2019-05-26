package com.bytebala.foodrecipes.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bytebala.foodrecipes.R;
import com.bytebala.foodrecipes.models.Recipe;
import com.bytebala.foodrecipes.util.Constants;

import java.util.ArrayList;
import java.util.List;

//We have multiple view types displayed in RecyclerView.
// 1- RecipeListItems, 2- Categories 3- Online Loading animation 4- Offline Loading Animation
// There are 4 different ViewHolder types
// So we specify adapter generic RecyclerView.ViewHolder
// And later we build custom ViewHolders extending generic

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;


    private List<Recipe> mRecipes;
    private OnRecipeListener mOnRecipeListener;

    public RecipeRecyclerAdapter(OnRecipeListener mOnRecipeListener) {
        this.mOnRecipeListener = mOnRecipeListener;
    }

    @NonNull
    @Override
    //Inflate whatever different ViewHolder needed
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Add some cases for different viewsholders that possibly use
        View view = null;
        //int i will denote view type
        switch (i) {
            case RECIPE_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, mOnRecipeListener);
            }

            case LOADING_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            }

            case EXHAUSTED_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_search_exhausted, viewGroup, false);
                return new SearchExhaustedViewHolder(view);
            }

            case CATEGORY_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_category_list_item, viewGroup, false);
                return new CategoryViewHolder(view, mOnRecipeListener);
            }


            default: {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, mOnRecipeListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        //Determine what the view type is
        //We dont need to set anything for loading view type
        int itemViewType = getItemViewType(i);

        if (itemViewType == RECIPE_TYPE) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(viewHolder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(mRecipes.get(i).getImage_url())
                    .into(((RecipeViewHolder) viewHolder).image);

            ((RecipeViewHolder) viewHolder).title.setText(mRecipes.get(i).getTitle());
            ((RecipeViewHolder) viewHolder).publisher.setText(mRecipes.get(i).getPublisher());
            ((RecipeViewHolder) viewHolder).socialScoer.setText(String.valueOf(Math.round(mRecipes.get(i).getSocial_rank())));
        } else if (itemViewType == CATEGORY_TYPE) {

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            //Reference drawables by uri parse how reference strings to drawables paths
            Uri path = Uri.parse("android.resource://com.bytebala.foodrecipes/drawable/" + mRecipes.get(i).getImage_url());

            Glide.with(viewHolder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(path)
                    .into(((CategoryViewHolder) viewHolder).categoryImage);

            ((CategoryViewHolder) viewHolder).categoryTitle.setText(mRecipes.get(i).getTitle());


        }
    }

    //This is where the application knows which category types is.
    @Override
    public int getItemViewType(int position) {

        if (mRecipes.get(position).getSocial_rank() == -1) {
            return CATEGORY_TYPE;
        } else if (mRecipes.get(position).getTitle().equals("LOADING...")) {
            return LOADING_TYPE;
        } else if (mRecipes.get(position).getTitle().equals("EXHAUSTED...")) {
            return EXHAUSTED_TYPE;
        } else if (position == mRecipes.size() - 1
                && position != 0
                && !mRecipes.get(position).equals("EXHAUSTED..")) {
            return LOADING_TYPE;
        } else {
            return RECIPE_TYPE;
        }
    }

    public void setQueryExhausted(){
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setTitle("EXHAUSTED...");
        mRecipes.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    private void hideLoading(){
        if (isLoading()){
            for(Recipe recipe: mRecipes){
                if (recipe.getTitle().equals("LOADING...")){
                    mRecipes.remove(recipe);
                }
            }
            notifyDataSetChanged();
        }
    }

    /*
    so only in recipe in the entire list is a recipe titled LOADING
    actual list is set to loadinglist
    . So it gonna look at list, create single recipe, it gonna add recipe to loading list, and it gonna set overall list to loadingList
      So only ;ist tem gonna be list item.
     */
    public void displayLoading() {
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            mRecipes = loadingList;
            notifyDataSetChanged();
        }
    }

    public void displaySearchCategories() {
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1); //so the listitem will know its category type
            categories.add(recipe);
        }

        mRecipes = categories;
        notifyDataSetChanged();
    }

    private boolean isLoading() {

        if (mRecipes != null) {
            if (mRecipes.size() > 0) {
                //Checking if last item in loading mode
                if (mRecipes.get(mRecipes.size() - 1).getTitle().equals("LOADING...")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (mRecipes != null) {
            return mRecipes.size();
        }
        return 0;
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public Recipe getSelectedRecipe(int position) {
        if (mRecipes != null) {
            if (mRecipes.size() > 0) {
                return mRecipes.get(position);
            }
        }
        return null;
    }
}

//You are no longer able to retrieve resources from http requests by default.
//You have to specify a network config settings file.
//https fine, but http specification needed.
