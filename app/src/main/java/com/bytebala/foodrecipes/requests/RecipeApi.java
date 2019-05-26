package com.bytebala.foodrecipes.requests;

import com.bytebala.foodrecipes.requests.responses.RecipeResponse;
import com.bytebala.foodrecipes.requests.responses.RecipeSearchResonse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//Inside this all requests for this API
public interface RecipeApi {

    // SEARCH
    @GET("api/search")
    Call<RecipeSearchResonse> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );

    // GET RECIPE REQUEST
    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );
}
