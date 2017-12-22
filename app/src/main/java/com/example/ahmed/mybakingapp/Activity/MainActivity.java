package com.example.ahmed.mybakingapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ahmed.mybakingapp.Fragment.DetailFragment;
import com.example.ahmed.mybakingapp.Fragment.MainFragment;
import com.example.ahmed.mybakingapp.Model.Recipe;
import com.example.ahmed.mybakingapp.R;
import com.example.ahmed.mybakingapp.SimpleIdlingResource;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity implements MainFragment.callback {
    private boolean mTwoPane = false;
    FragmentManager manager;
    FragmentTransaction transaction;

    MainFragment mainFragment;


    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

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
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        mainFragment.setListener(this);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.MainContainer,mainFragment);
        transaction.commit();

        if (findViewById(R.id.MainContainer) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainContainer, new MainFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        if(savedInstanceState!=null){
            mainFragment.positionIndex = savedInstanceState.getInt(BUNDLE_RECYCLER_LAYOUT );
        }

        getIdlingResource();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mainFragment != null) {
            outState.putInt(BUNDLE_RECYCLER_LAYOUT, mainFragment.positionIndex);
        }

    }


    @Override
    public void onItemSelected(Recipe recipe) {

        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("result",recipe);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.DetailContainer, detailFragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra("result", (Serializable) recipe);
            startActivity(intent);
        }
    }
}
