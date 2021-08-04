package com.example.foodrecipes.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodrecipes.AppExecutors;
import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.requests.responses.RecipeResponse;
import com.example.foodrecipes.requests.responses.RecipeSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.foodrecipes.util.Constant.NETWORK_TIME;

public class RecipeApiClint {

    private static final String TAG = "RecipeApiClint";

    private static RecipeApiClint instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;
    private MutableLiveData<Recipe> mRecipe;
    private RetrieveRecipeRunnable mRetrieveRecipeRunnable;



    public static RecipeApiClint getInstance(){
        if (instance == null){
            instance = new RecipeApiClint();
        }
        return instance;
    }

    public RecipeApiClint() {
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
    }
    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }
    public LiveData<Recipe> getRecipe(){
        return mRecipe;
    }


    // TODO: review .. !!
    public void searchRecipesApi(String query, String pageNumber){

        if (mRetrieveRecipesRunnable != null ){
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query,pageNumber);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // to inform an user if it is timeout
                handler.cancel(true);
            }
        }, NETWORK_TIME, TimeUnit.MILLISECONDS);

    }

    public void searchRecipeById(String recipeId){
        if (mRetrieveRecipeRunnable != null ){
            mRetrieveRecipeRunnable = null;
        }

        mRetrieveRecipeRunnable = new RetrieveRecipeRunnable(recipeId);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipeRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, NETWORK_TIME,TimeUnit.MILLISECONDS);

    }

    // TODO: review .. !!
    private class RetrieveRecipesRunnable implements Runnable {

        private String query;
        private String pageNumber;
        boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, String pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query,pageNumber).execute();

                if (cancelRequest){
                    return;
                }
                if (response.code() == 200){
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                    if ( Integer.parseInt(pageNumber) == 1){
                        mRecipes.postValue(list);
                    }
                    else {
                        // to appending a new recipes
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }
                }
                else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error );
                    //why null? next u will be know
                    mRecipes.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        private Call<RecipeSearchResponse> getRecipes(String query, String pageNumber) {
            return ServiceGenerator.getRecipeApi().searchResponse(query, pageNumber);
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: the searching request has been canceled ");
            cancelRequest = true;
        }
    }

    private class RetrieveRecipeRunnable implements Runnable {

        private String recipeId;
        boolean cancelRequest;

        public RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(recipeId).execute();

                if (cancelRequest){
                    return;
                }
                if (response.code() == 200){
                    Recipe recipe = ((RecipeResponse)response.body()).getRecipe();
                    mRecipe.postValue(recipe);
                }
                else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error );
                    //why null? next u will be know
                    mRecipe.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mRecipe.postValue(null);
            }

        }

        private Call<RecipeResponse> getRecipes(String recipeId) {
            return ServiceGenerator.getRecipeApi().getRecipe(recipeId);
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: the searching request has been canceled ");
            cancelRequest = true;
        }
    }

    public void cancelRequest(){
        if (mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable.cancelRequest();
        }
        if (mRetrieveRecipeRunnable != null){
            mRetrieveRecipeRunnable.cancelRequest();
        }
    }
}
