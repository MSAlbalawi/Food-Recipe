package com.example.foodrecipes.requests;

import com.example.foodrecipes.requests.responses.RecipeResponse;
import com.example.foodrecipes.requests.responses.RecipeSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    // Search
    @GET("api/v2/recipes")
    Call<RecipeSearchResponse> searchResponse(
            @Query("q") String query,
            @Query("page") String page
    );

    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("rId") String recipeId
    );
}
