package com.example.foodrecipes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.viewmodels.RecipeViewModel;


public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;
    private ScrollView mScrollView;
    private RecipeViewModel mRecipeViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mRecipeImage = findViewById(R.id.recipe_details_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score_detils);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        showProgressBar(true);
        subscribeObserver();
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if (getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: --> " + recipe.getTitle()+" <--  ID ->" + recipe.getId());
            mRecipeViewModel.searchRecipeById(recipe.getId());
        }


    }

    private void subscribeObserver(){
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null){

                    // TODO: why the recipe.getID() came with null !! ?
                    Log.d(TAG, "##############invok the ID from obj##############");
                    Log.d(TAG, "THE ID IS: "+mRecipeViewModel.getRecipeId() +" THE COMES OBJ Recipe ID = "+recipe.getId());
                    Log.d(TAG, "اطبع المديول  "+ recipe.toString());
//                    setRecipeProperties(recipe);
                    if (recipe.getId().equals(mRecipeViewModel.getRecipeId())){

                        Log.d(TAG, "=========================Before Sent ====================");
                        Log.d(TAG, "recipe image url is: " + recipe.getImageUrl());
                        Log.d(TAG, "recipe rank is: " + recipe.getSocialUrl());
                        setRecipeProperties(recipe);
                    }

                }
            }
        });
    }

    private void setRecipeProperties(Recipe recipe){
        if (recipe != null){

            Log.d(TAG, "===================== After sent: ===============");
            Log.d(TAG, "recipe title is: " + recipe.getTitle());
            Log.d(TAG, "recipe image url is: " + recipe.getImageUrl());
            Log.d(TAG, "recipe rank is: " + recipe.getSocialUrl());
            for (String ingredients: recipe.getIngredients()){
                Log.d(TAG, "recipe ingredient is: "+ ingredients);
            }
            Log.d(TAG, "============================================================");


            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.getImageUrl())
                    .into(mRecipeImage);

            mRecipeTitle.setText(recipe.getTitle());
            mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocialUrl())));

            // for fill-in the ingredient ..
            mRecipeIngredientsContainer.removeAllViews();

            for (String ingredient: recipe.getIngredients()){

                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                mRecipeIngredientsContainer.addView(textView);
            }
        }
        showParent();
        showProgressBar(false);
    }

    private void showParent(){
        mScrollView.setVisibility(View.VISIBLE);
    }
}

