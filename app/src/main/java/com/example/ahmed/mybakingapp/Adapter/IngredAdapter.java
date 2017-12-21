package com.example.ahmed.mybakingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.mybakingapp.Activity.StepActivity;
import com.example.ahmed.mybakingapp.Model.Ingredients;
import com.example.ahmed.mybakingapp.Model.Steps;
import com.example.ahmed.mybakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredAdapter extends RecyclerView.Adapter<IngredAdapter.IngredViewHolder> {

    public ArrayList<Ingredients> mIngredients;
    public ArrayList<Steps> mSteps;

    Context context ;

    public IngredAdapter(ArrayList<Ingredients> mIngredients, ArrayList<Steps> mSteps,Context context) {
        this.mIngredients = mIngredients;
        this.context = context;
        this.mSteps = mSteps;
    }


    @Override
    public IngredViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingred_recycler_list, parent, false);

        IngredAdapter.IngredViewHolder Iview = new IngredAdapter.IngredViewHolder(view);
        return Iview;

    }

    @Override
    public void onBindViewHolder(IngredViewHolder holder, final int position) {

        holder.DataIngred(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int stepID = mSteps.get(position).getId();

                Intent stepIntent = new Intent(context, StepActivity.class);
                stepIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle args = new Bundle();
                args.putSerializable(StepActivity.GET_STEP_ARRAYLIST, mSteps);
                stepIntent.putExtras(args);
                stepIntent.putExtra(StepActivity.GET_STEP_ID, position);
                context.startActivity(stepIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mIngredients.size() >= mSteps.size()){
            return mSteps.size();
        }
        return mIngredients.size();
    }



    public class IngredViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ingred_Quantity)
        TextView ingred_Quantity;
        @BindView(R.id.ingred_measure)
        TextView ingred_measure;
        @BindView(R.id.ingred_ingred)
        TextView ingred_ingred;


        public IngredViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void DataIngred (int position) {
            Float quantityFloat = mIngredients.get(position).getQuantity();
            String quantityString;

            if(quantityFloat != null){
                quantityString = Float.toString(quantityFloat);
                ingred_Quantity.setText(quantityString);
            }else {
                ingred_Quantity.setText("No found");
            }

            ingred_measure.setText(mIngredients.get(position).getMeasure());
            ingred_ingred.setText(mIngredients.get(position).getIngredient());
        }

     }
    }

