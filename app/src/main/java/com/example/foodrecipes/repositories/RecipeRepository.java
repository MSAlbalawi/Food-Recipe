package com.example.foodrecipes.repositories;

import androidx.lifecycle.LiveData;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.requests.RecipeApiClint;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private String mQuery;
    private int mPageNumber;

//    private MutableLiveData<List<Recipe>> mRecipes;
    private RecipeApiClint mRecipeApiClint;


    public static RecipeRepository getInstance(){
        if (instance == null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository(){
        mRecipeApiClint = RecipeApiClint.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeApiClint.getRecipes();
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeApiClint.getRecipe();
    }

    public void searchRecipesApi(String query, String pageNumber){
        if (Integer.parseInt(pageNumber) == 0){
            pageNumber = "1";
        }
        mQuery = query;
        mPageNumber = Integer.parseInt(pageNumber);
        mRecipeApiClint.searchRecipesApi(query,pageNumber);
    }

    public void searchRecipeById(String recipeId){
        mRecipeApiClint.searchRecipeById(recipeId);
    }
    public void searchNextPage(){
        searchRecipesApi(mQuery,String.valueOf(mPageNumber+1) );
    }

    public void cancelRequest(){
            mRecipeApiClint.cancelRequest();

    }
}
