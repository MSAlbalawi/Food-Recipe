package com.example.foodrecipes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.repositories.RecipeRepository;

import java.util.List;


public class RecipeListViewModel extends ViewModel {


    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsPerformingQuery = false;

    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, String pageNumber){
        mIsViewingRecipes = true;
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipesApi(query,pageNumber);
    }

    public void searchNextPage(){
        if (!mIsPerformingQuery && mIsViewingRecipes){
            mRecipeRepository.searchNextPage();
        }
    }

    public boolean isViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean mIsViewingRecipes) {
        this.mIsViewingRecipes = mIsViewingRecipes;
    }

    public boolean IsPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void setIsPerformingQuery(boolean mIsPerformingQuery) {
        this.mIsPerformingQuery = mIsPerformingQuery;
    }

    public boolean onBackPressed(){
        if (mIsPerformingQuery){
            // Cancel the retrofit request
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
        else if (mIsViewingRecipes){
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }
}
