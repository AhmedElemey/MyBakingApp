package com.example.ahmed.mybakingapp.Fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahmed.mybakingapp.Model.Ingredients;
import com.example.ahmed.mybakingapp.Model.Steps;
import com.example.ahmed.mybakingapp.Provider.RecipeContract;
import com.example.ahmed.mybakingapp.R;
import com.example.ahmed.mybakingapp.Adapter.RecipeAdapter;
import com.example.ahmed.mybakingapp.Retrofit.ApiClient;
import com.example.ahmed.mybakingapp.Retrofit.ApiInterface;
import com.example.ahmed.mybakingapp.Model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment implements RecipeAdapter.onRecipClick {


    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    public RecipeAdapter recipeAdapter;
    private ApiInterface mService;
    private ArrayList<Recipe> mRecipe;
    MainFragment.callback callback;

    public static int positionIndex = 0;


    @Override
    public void onRecip(int position) {
        callback.onItemSelected(mRecipe.get(position));
    }


    public void setListener(MainFragment.callback call) {

        callback = call;
    }

    public interface callback {
        void onItemSelected(Recipe recipe);
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            //positionIndex=-1;
            Log.e("testing", positionIndex+"");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        positionIndex = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        FetchRecipe();
    }

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecipe = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.baking_recycler_view);
        mService = ApiClient.getService();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recipeAdapter = new RecipeAdapter(mRecipe, getActivity());
        recipeAdapter.setListener(this);
        recyclerView.setAdapter(recipeAdapter);


        return view;
    }


    public void FetchRecipe() {

        mService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        Toast.makeText(getActivity(), "ERRO FETCH DATA", Toast.LENGTH_SHORT).show();

                    } else {

                        for (Recipe recipe : response.body()) {
                            mRecipe.add(recipe);
                        }
                        UpdateRecycle(mRecipe);
                        clearData();
                        recipeAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(positionIndex);
                        insertRecipesIntoDatabase(response.body());
                    }

                } else {
                    Toast.makeText(getActivity(), "ERRO FETCH DATA", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), " Failed ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void UpdateRecycle(ArrayList<Recipe> recipeArrayList) {

        if (recipeArrayList.size() > 0) {

            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recipeAdapter = new RecipeAdapter(recipeArrayList, getActivity());
            recyclerView.setAdapter(recipeAdapter);


        }
    }


    ///////////////////////////////////////////////////


    private void insertRecipesIntoDatabase(List<Recipe> body) {
        ContentValues[] recipeContentValue = new ContentValues[body.size()];
        for (int i = 0; i < body.size(); i++) {
            recipeContentValue[i] = new ContentValues();
            recipeContentValue[i].put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, body.get(i).getId());
            recipeContentValue[i].put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, body.get(i).getName());
            recipeContentValue[i].put(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS, body.get(i).getServings());
            recipeContentValue[i].put(RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE, body.get(i).getImage());

            // insert recipe's step and ingredients into database
            insertIngredientsIntoDatabase(body.get(i).getIngredients(), body.get(i).getId());
            insertStepsIntoDatabase(body.get(i).getSteps(), body.get(i).getId());

        }
        getActivity().getContentResolver().bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI, recipeContentValue);

    }

    private void insertStepsIntoDatabase(List<Steps> steps, int recipeId) {
        ContentValues[] stepContentValues = new ContentValues[steps.size()];
        for (int i = 0; i < steps.size(); i++) {
            stepContentValues[i] = new ContentValues();
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_RECIPE_ID, recipeId);
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_STEP_ID, steps.get(i).getId());
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_SHORT_DESCRIPTION, steps.get(i).getShortDescription());
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_DESCRIPTION, steps.get(i).getDescription());
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_VIDEO_URL, steps.get(i).getVideoURL());
            stepContentValues[i].put(RecipeContract.StepEntry.COLUMN_THUMBNAIL_URL, steps.get(i).getThumbnailURL());
        }
        getActivity().getContentResolver().bulkInsert(RecipeContract.StepEntry.CONTENT_URI, stepContentValues);
    }

    private void insertIngredientsIntoDatabase(List<Ingredients> ingredients, int recipeId) {
        ContentValues[] ingredientContentValues = new ContentValues[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientContentValues[i] = new ContentValues();
            ingredientContentValues[i].put(RecipeContract.IngredientEntry.COLUMN_RECIPE_ID, recipeId);
            ingredientContentValues[i].put(RecipeContract.IngredientEntry.COLUMN_QUALITY, ingredients.get(i).getQuantity());
            ingredientContentValues[i].put(RecipeContract.IngredientEntry.COLUMN_MEASURE, ingredients.get(i).getMeasure());
            ingredientContentValues[i].put(RecipeContract.IngredientEntry.COLUMN_INGREDIENT, ingredients.get(i).getIngredient());
        }
        getActivity().getContentResolver().bulkInsert(RecipeContract.IngredientEntry.CONTENT_URI, ingredientContentValues);
    }

    // Used only for debug stage
    // clear the database before fetching data
    private void clearData() {
        getActivity().getContentResolver().delete(RecipeContract.RecipeEntry.CONTENT_URI, null, null);
        getActivity().getContentResolver().delete(RecipeContract.IngredientEntry.CONTENT_URI, null, null);
        getActivity().getContentResolver().delete(RecipeContract.StepEntry.CONTENT_URI, null, null);
    }


}
