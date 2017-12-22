package com.example.ahmed.mybakingapp.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.ahmed.mybakingapp.Adapter.StepAdapter;
import com.example.ahmed.mybakingapp.Fragment.DetailFragment;
import com.example.ahmed.mybakingapp.Fragment.StepFragment;
import com.example.ahmed.mybakingapp.Model.Steps;
import com.example.ahmed.mybakingapp.R;

import java.util.ArrayList;


public class StepActivity extends AppCompatActivity implements StepAdapter.CallBack , DetailFragment.CallBackAction{

    public final static String GET_STEP_ARRAYLIST = "GET_STEP_ARRAYLIST";
    public final static String GET_STEP_ID = "GET_STEP_ID";

    public static boolean isTwoPane;
    private static final String STEP_FRAGMENT_TAG = "SFTAG";
    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";

    public ArrayList<Steps> mStep;
    int mID;

    StepFragment fragment;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        mStep = (ArrayList<Steps>) StepActivity.this.getIntent().getExtras().getSerializable(GET_STEP_ARRAYLIST);
        mID =  StepActivity.this.getIntent().getIntExtra(GET_STEP_ID , 0);

        Bundle bundleD = new Bundle();
        bundleD.putSerializable("Step",mStep);
        bundleD.putInt("StepID" , mID);
        StepFragment sf = new StepFragment();
        if (savedInstanceState == null) {
            sf.setArguments(bundleD);
            FragmentManager manger = getSupportFragmentManager();
            manger.beginTransaction().replace(R.id.Step_Container, sf, STEP_FRAGMENT_TAG).commit();
        }

        FrameLayout fm = (FrameLayout) findViewById(R.id.DetailContainer);
        if (fm != null){
            isTwoPane = true;
        }else {
            isTwoPane = false;
        }

        if(savedInstanceState!=null){
            fragment.positionIndex = savedInstanceState.getInt(BUNDLE_RECYCLER_LAYOUT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(fragment!=null){
            outState.putInt(BUNDLE_RECYCLER_LAYOUT, fragment.positionIndex);
        }




    }


    @Override
    public void OnItemSelected(Bundle bundle ) {

        if(isTwoPane ){

            DetailFragment f = new DetailFragment();
            f.setArguments(bundle);
            FragmentManager manger = getSupportFragmentManager();
            FragmentTransaction transaction = manger.beginTransaction();
            transaction.replace(R.id.DetailContainer, f, DETAIL_FRAGMENT_TAG);
            transaction.commit();

        }
        else {


            Intent i = new Intent(StepActivity.this,DetailActivity.class);
            i.putExtras(bundle);
            startActivity(i);
        }

    }

    @Override
    public void OnClickActionNext(Bundle bundle) {
        if(isTwoPane  ){

            DetailFragment f = new DetailFragment();
            f.setArguments(bundle);
            FragmentManager manger = getSupportFragmentManager();
            FragmentTransaction transaction = manger.beginTransaction();
            transaction.replace(R.id.DetailContainer, f, DETAIL_FRAGMENT_TAG);
            transaction.commit();

        }
        else {

            Intent i = new Intent(StepActivity.this,DetailActivity.class);
            i.putExtras(bundle);
            startActivity(i);
        }

    }

    @Override
    public void OnClickActionBack(Bundle bundle) {

        if(isTwoPane){

            DetailFragment f = new DetailFragment();
            f.setArguments(bundle);
            FragmentManager manger = getSupportFragmentManager();
            FragmentTransaction transaction = manger.beginTransaction();
            transaction.replace(R.id.DetailContainer, f, DETAIL_FRAGMENT_TAG);
            transaction.commit();

        }
        else {

            Intent i = new Intent(StepActivity.this,DetailActivity.class);
            i.putExtras(bundle);
            startActivity(i);

        }
        }
}
