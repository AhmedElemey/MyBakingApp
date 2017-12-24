package com.example.ahmed.mybakingapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ahmed.mybakingapp.Fragment.DetailFragment;
import com.example.ahmed.mybakingapp.Model.Steps;
import com.example.ahmed.mybakingapp.R;
import com.example.ahmed.mybakingapp.SimpleIdlingResource;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public final static String GET_STEP_ARRAY = "GET_STEP_ARRAY";
    public final static String GET_STEP_ID = "GET_STEP_ID";

    public ArrayList<Steps> mStep;
    public int mID;

    private SimpleIdlingResource simpleIdlingResource = new SimpleIdlingResource();

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (simpleIdlingResource == null) {
            simpleIdlingResource = new SimpleIdlingResource();
        }
        return simpleIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mStep = (ArrayList<Steps>) DetailActivity.this.getIntent().getExtras().getSerializable(GET_STEP_ARRAY);
        mID = DetailActivity.this.getIntent().getIntExtra(GET_STEP_ID , 0);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Step",mStep);
        bundle.putInt("StepID" , mID);

        DetailFragment df = new DetailFragment();

        if (savedInstanceState == null) {
            df.setArguments(bundle);
            FragmentManager manger = getSupportFragmentManager();
            manger.beginTransaction().add(R.id.DetailContainer, df).commit();
        }

        getIdlingResource();

    }


    @Override
    public void onBackPressed() {

        Intent intBack = new Intent(DetailActivity.this , StepActivity.class);
        intBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle args = new Bundle();
        args.putSerializable(StepActivity.GET_STEP_ARRAYLIST, mStep);
        intBack.putExtras(args);
        intBack.putExtra(StepActivity.GET_STEP_ID, mID);
        DetailActivity.this.startActivity(intBack);

    }

}
