package com.bytebala.foodrecipes.requests.responses;

// Making a single recipe request
// A response retriveing  when making get request by referencing recipe id

import com.bytebala.foodrecipes.models.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeResponse {

    //To serialize and deserialize use serializedname and expose
    @SerializedName("recipe")
    @Expose()
    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipe=" + recipe +
                '}';
    }
}
