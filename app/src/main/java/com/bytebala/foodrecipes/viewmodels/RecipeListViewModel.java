package com.bytebala.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.bytebala.foodrecipes.models.Recipe;
import com.bytebala.foodrecipes.repositories.RecipeRepository;

import java.util.List;

/*
So what is this ViewModel going to do?
This ViewModel in particular is responsible for retrieving, holding and displaying recipes that are being displayed in the app.
Keeping updated list
Note: name after activity or fragment it modeling. So RecipeListViewModel
 */
public class RecipeListViewModel extends ViewModel {


    private RecipeRepository mRecipeRepository;
    //if display categories or not
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;


    public RecipeListViewModel() {
        mIsPerformingQuery = false;
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public LiveData<Boolean> isQueryExhausted() {
        return mRecipeRepository.isQueryExhausted();
    }

    public void searchRecipeApi(String query, int pageNumber) {
        mIsPerformingQuery = true;
        mIsViewingRecipes = true;
        mRecipeRepository.searchRecipeApi(query, pageNumber);
    }

    public void searchNextPage() {
        if (!mIsPerformingQuery
                && mIsViewingRecipes
                && !isQueryExhausted().getValue()
        ) {
            mRecipeRepository.searchNextPage();
        }
    }

    public boolean isViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes) {
        mIsViewingRecipes = isViewingRecipes;
    }

    public void setIsPerformingQuery(Boolean isPerformingQuery) {
        mIsPerformingQuery = isPerformingQuery;
    }

    public boolean isPerformingQuery() {
        return mIsPerformingQuery;
    }

    //Every widget has focus - cursor on it or highlighted
    //Remove the focus in searchview before pressing back button


    public boolean onBackPressed() {
        if (mIsPerformingQuery) {
            //cancel the query
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
        //if they are view recipes
        if (mIsViewingRecipes) {
            mIsViewingRecipes = false;
            return false;
        }
        //if they are not viewing recipes, they are viewing categories
        return true;
    }
}
// LiveData for now inside ViewModel.
// Later we have a repository and query, tell repositpry to get the data query and send back to activity