package com.example.ahmed.mybakingapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.mybakingapp.Adapter.StepAdapter;
import com.example.ahmed.mybakingapp.Model.Steps;
import com.example.ahmed.mybakingapp.R;

import java.util.ArrayList;

public class StepFragment extends Fragment {

    public ArrayList<Steps> mStep;
    int mID;

    RecyclerView recyclerView;

    LinearLayoutManager mLayoutManager;
    StepAdapter stepAdapter;
    public static int positionIndex = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View Sview = inflater.inflate(R.layout.fragment_step, container, false);


        recyclerView = (RecyclerView) Sview.findViewById(R.id.step_Recycle);
        Bundle b = getArguments();
        mStep = (ArrayList<Steps>) b.getSerializable("Step");
        mID = b.getInt("StepID",0);

        UpdateRecycle(mStep ,mID);


        return Sview ;
    }



    public void UpdateRecycle (ArrayList<Steps> mStep , int mID) {

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        stepAdapter = new StepAdapter(mStep , mID, getActivity());
        recyclerView.setAdapter(stepAdapter);

    }


    @Override
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
        positionIndex= ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
