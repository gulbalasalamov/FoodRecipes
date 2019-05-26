package com.bytebala.foodrecipes.requests;

import com.bytebala.foodrecipes.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    //Retrofit instance
    private static Retrofit retrofit = retrofitBuilder.build();

    //Them Retrofit instance is used to create and instantiate an object instance of API
    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    //Get access to retrofit instance, indeed Api instance that has a reference to retrofit insatnce
    public static RecipeApi getRecipeApi(){
        return recipeApi;
    }
}
