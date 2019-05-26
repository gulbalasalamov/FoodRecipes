package com.bytebala.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.bytebala.foodrecipes.adapters.OnRecipeListener;
import com.bytebala.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.bytebala.foodrecipes.models.Recipe;

import com.bytebala.foodrecipes.util.Testing;
import com.bytebala.foodrecipes.util.VerticalSpacingItemDecorator;
import com.bytebala.foodrecipes.viewmodels.RecipeListViewModel;
import com.bytebala.foodrecipes.viewmodels.RecipeViewModel;

import java.util.List;


// Responsible for displaying list of recipes
public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.search_view);

        //Decleration and instantiation of ViewModel
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        initSearchView();
        //If they are viewing categories i.e. if they don't see the recipes
        if (!mRecipeListViewModel.isViewingRecipes()) {
            //Display search categories
            displaySearchCategories();
        }

        //As custom toolbar used, and not used default support action bar, we need to associate our toolbar with support action bar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

    }

    //Observe LiveData object. If changes, update Activity
    private void subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    if (mRecipeListViewModel.isViewingRecipes()) {
                        Testing.printRecipes(recipes, "recipes test");
                        //that means query completed,
                        mRecipeListViewModel.setIsPerformingQuery(false);
                        mAdapter.setRecipes(recipes);
                    }
                }
            }
        });

        mRecipeListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    mAdapter.setQueryExhausted();
                }
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!mRecyclerView.canScrollVertically(1)){
                    //search the next page
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                mAdapter.displayLoading();
                mRecipeListViewModel.searchRecipeApi(s, 1);
                mSearchView.clearFocus(); // so it doesnt consume click anymore
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this,RecipeActivity.class);
        intent.putExtra("recipe",mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClicked(String category) {
        mAdapter.displayLoading();
        mRecipeListViewModel.searchRecipeApi(category, 1);
        mSearchView.clearFocus();
    }

    private void displaySearchCategories() {
        mRecipeListViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.onBackPressed()) {
            super.onBackPressed();
        } else
            displaySearchCategories();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_categories)
            displaySearchCategories();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Determine when to bottom of page reached and retrieve next peach.
    //Solution: use onScrollListener and query more result

    //Load animation for pagination to show and execute query

    //How to query list items.
}




/*

        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

        //Do search using Retrofit
//        Call<RecipeSearchResonse> responceCall =
//                recipeApi.searchRecipe(Constants.API_KEY, "chicken breast", "1");
//
//        responceCall.enqueue(
//                new Callback<RecipeSearchResonse>() {
//                    @Override
//                    public void onResponse(
//                            Call<RecipeSearchResonse> call, Response<RecipeSearchResonse> response) {
//                        Log.d(TAG, "onResponse: server response: " + response.toString());
//
//                        if (response.code() == 200) {
//                            // that will print a raw data
//                            Log.d(TAG, "onResponse: " + response.body().toString());
//
//                            // Cast responses to list object types
//                            List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
//
//                            for (Recipe recipe : recipes) {
//                                Log.d(TAG, "onResponse: " + recipe.getTitle());
//                            }
//                        } else {
//                            try {
//                                Log.d(TAG, "onResponse: " + response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<RecipeSearchResonse> call, Throwable t) {
//                    }
//                });

        // Do get using Retrofit
        Call<RecipeResponse> responceCall =
                recipeApi.getRecipe(Constants.API_KEY,
                        "8c0314");

        responceCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                //We are retrieving a single recipe object. Not List.
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: Server Response" + response.body().toString());
                    Recipe recipe = response.body().getRecipe();
                    Log.d(TAG, "onResponse: RETRIVED RECIPE: " + recipe.toString());
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ERROR: " + t.getMessage());
            }
        });
    }
 */