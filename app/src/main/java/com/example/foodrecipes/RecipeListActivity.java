package com.example.foodrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrecipes.adapters.OnRecipeListener;
import com.example.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.example.foodrecipes.models.Recipe;
import com.example.foodrecipes.requests.RecipeApi;
import com.example.foodrecipes.requests.ServiceGenerator;
import com.example.foodrecipes.requests.responses.RecipeResponse;
import com.example.foodrecipes.requests.responses.RecipeSearchResponse;
import com.example.foodrecipes.util.VerticalSpacingDecorator;
import com.example.foodrecipes.viewmodels.RecipeListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        initRecyclerView();
        subscribeObserves();
        initSearchView();
        if (!mRecipeListViewModel.isViewingRecipes()){
            displaySearchCategories();
        }

        // to associate the menu with a costume tool bar rather than the default one
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    private void subscribeObserves(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null){
                    if (mRecipeListViewModel.isViewingRecipes()){
                        for (Recipe recipe: recipes){
                            Log.d(TAG, "onRecipeList | the title is: ~~~>: " + recipe.getTitle());
                        }
                        mRecipeListViewModel.setIsPerformingQuery(false);
                        mAdapter.setRecipes(recipes);

                    }

                }
            }
        });
    }

    private void initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingDecorator item = new VerticalSpacingDecorator(20);
        mRecyclerView.addItemDecoration(item);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!mRecyclerView.canScrollVertically(1)){
                    // TODO: search the next page
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });
    }

    private void searchRecipesApi(String query, String pageNumber){
        mRecipeListViewModel.searchRecipesApi(query,pageNumber);
    }

    private void initSearchView(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.displayingLoading();
                mRecipeListViewModel.searchRecipesApi(query,"1");
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void testRetrofitRequest(){
        searchRecipesApi("beef","1");
    }
    private void testingSearchRecipes(){
        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

        Call<RecipeSearchResponse> recipeSearchResponseCall = recipeApi
                .searchResponse("beef", "1");

        recipeSearchResponseCall.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                Log.d(TAG, "onResponse: service response: ==>"+ response.toString());
                if (response.code() == 200){
                    Log.d(TAG,"onResponse: >>>>> "+ response.body().toString());
                    List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
                    for (Recipe recipe: recipes){
                        Log.d(TAG,"onResponse -->" + recipe.getTitle());
                    }

                }else {
                    try {
                        Log.d(TAG,"onResponse -->" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
                Log.d(TAG, "onResponse######: service response: ==>"+ t.toString());
            }
        });

    }
    private void testingOneRecipe() {
        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

//        Call<RecipeSearchResponse> recipeSearchResponseCall = recipeApi
//                .searchResponse("chicken", "1");
//
//        recipeSearchResponseCall.enqueue(new Callback<RecipeSearchResponse>() {
//            @Override
//            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
//                Log.d(TAG, "onResponse: service response: ==>"+ response.toString());
//                if (response.code() == 200){
//                    Log.d(TAG,"onResponse: >>>>> "+ response.body().toString());
//                    List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
//                    for (Recipe recipe: recipes){
//                        Log.d(TAG,"onResponse -->" + recipe.getTitle());
//                    }
//
//                }else {
//                    try {
//                        Log.d(TAG,"onResponse -->" + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
//                Log.d(TAG, "onResponse######: service response: ==>"+ t.toString());
//            }
//        });

        Call<RecipeResponse> recipeSearchResponseCall = recipeApi
                .getRecipe("41470");

        recipeSearchResponseCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                Log.d(TAG, "onResponse: service response: ==>" + response.toString());
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: >>>>> " + response.body().toString());
                    Recipe recipe = response.body().getRecipe();
                    Log.d(TAG, "onResponse: Retreived Recpies  >>>>> " + recipe.toString());

                } else {
                    try {
                        Log.d(TAG, "onResponse -->" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.d(TAG, "onResponse######: service response WTtTF: ==>" + t.toString());
            }
        });


    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayingLoading();
        mRecipeListViewModel.searchRecipesApi(category,"1");
        mSearchView.clearFocus();
    }

    private void displaySearchCategories(){
        mRecipeListViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.onBackPressed()){
            super.onBackPressed();
        }else {
            displaySearchCategories();
        }

    }

    // To create and inflate the menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // once the menu option selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_categories){
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }
}

/*



        findViewById(R.id.button_recipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testingOneRecipe();
            }
        });
        findViewById(R.id.button_search_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                testingSearchRecipes();
//                if (mProgressBar.getVisibility() == View.VISIBLE){
//                    showProgressBar(false);
//                }else {
//                    showProgressBar(true);
//                }

            }
        });
 */