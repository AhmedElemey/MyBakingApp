package com.example.ahmed.mybakingapp.Adapter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.mybakingapp.Activity.IngredActivity;
import com.example.ahmed.mybakingapp.Model.Ingredients;
import com.example.ahmed.mybakingapp.Model.Recipe;
import com.example.ahmed.mybakingapp.Model.Steps;
import com.example.ahmed.mybakingapp.R;
import com.example.ahmed.mybakingapp.Widget.IngredientWidgetProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import static com.example.ahmed.mybakingapp.Widget.IngredientWidgetProvider.RECIPE_INDEX_EXTRA;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {


    private ArrayList<Recipe> mRecipes;
    private Context context ;
    private RecipeAdapter.onRecipClick recipClick;

    public RecipeAdapter (ArrayList<Recipe> mRecipes , Context context ){

        this. mRecipes= mRecipes;
        this.context=context;
    }

    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_list, parent, false);

        RecipeViewHolder Rview = new RecipeViewHolder(view);

        return Rview;

    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeViewHolder holder, final int position) {

        holder.Data(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Ingredients> ingredients = mRecipes.get(position).getIngredients();
                ArrayList<Steps> steps = mRecipes.get(position).getSteps();
                int recipId = mRecipes.get(position).getId() ;

                Intent ingredIntent = new Intent(context, IngredActivity.class);
                Bundle args = new Bundle();
                args.putSerializable(IngredActivity.INGREDIENT_DATA, ingredients);
                args.putSerializable(IngredActivity.STEP_DATA, steps);

                ingredIntent.putExtra(RECIPE_INDEX_EXTRA, recipId);
                ingredIntent.putExtras(args);
                updateWidget(recipId);
                context.startActivity(ingredIntent);
                mRecipes.clear();

            }
        });

        holder.recipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipClick.onRecip(position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.recipe_name)
        TextView recipeName;
//        @BindView(R.id.recipe_image)
        ImageView recipeImage;


        public RecipeViewHolder(View itemView) {
            super(itemView);

//            ButterKnife.bind(this, itemView);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_name);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_image);
        }

        void Data(int position) {
            recipeName.setText(mRecipes.get(position).getName());

            if (!TextUtils.isEmpty(mRecipes.get(position).getImage())) {
                Picasso.with(context)
                        .load(mRecipes.get(position).getImage())
                        .placeholder(R.drawable.ph)
                        .error(R.drawable.ph)
                        .into(recipeImage);
            } else {
                Picasso.with(context)
                        .load(R.drawable.ph)
                        .placeholder(R.drawable.ph)
                        .error(R.drawable.ph)
                        .into(recipeImage);
            }
        }
    }

    private void updateWidget(int recipe_id) {
        // save the recipe_id on shared preference to access in widget
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(context).edit();
        preferences.putInt(RECIPE_INDEX_EXTRA, recipe_id);
        preferences.apply();

        // update the widget to show ingredients corresponding to recipe id
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName widgetComponent = new ComponentName(context, IngredientWidgetProvider.class);
        int[] widgetIds = widgetManager.getAppWidgetIds(widgetComponent);
        Intent update = new Intent();
        update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(update);
    }
    public void setListener(RecipeAdapter.onRecipClick l){

        recipClick = l;
    }
    public interface onRecipClick{
        void onRecip(int position);
    }
}