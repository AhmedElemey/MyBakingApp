package com.example.ahmed.mybakingapp.Activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.ahmed.mybakingapp.Adapter.IngredAdapter;
import com.example.ahmed.mybakingapp.Model.Ingredients;
import com.example.ahmed.mybakingapp.Model.Steps;
import com.example.ahmed.mybakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredActivity extends AppCompatActivity {


    public final static String INGREDIENT_DATA = "INGREDIENT_DATA";
    public final static String STEP_DATA = "STEP_DATA";

    @BindView(R.id.ingred_Recycle)
    RecyclerView recyclerView;

    ArrayList<Ingredients> mIngredients;
    ArrayList<Steps> mSteps;
    LinearLayoutManager mLayoutManager;
    IngredAdapter mIngredAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingred);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mIngredients = (ArrayList<Ingredients>) IngredActivity.this.getIntent().getExtras().getSerializable(INGREDIENT_DATA);
        mSteps = (ArrayList<Steps>) IngredActivity.this.getIntent().getExtras().getSerializable(STEP_DATA);


        UpdateRecycle(mIngredients ,mSteps);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UpdateRecycle (ArrayList<Ingredients> mIngred , ArrayList<Steps> mStep) {

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(IngredActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);


        mIngredAdapter = new IngredAdapter(mIngred , mStep,IngredActivity.this);
        recyclerView.setAdapter(mIngredAdapter);

    }


}
