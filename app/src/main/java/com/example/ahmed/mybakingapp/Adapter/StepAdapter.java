package com.example.ahmed.mybakingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.mybakingapp.Activity.DetailActivity;
import com.example.ahmed.mybakingapp.Model.Steps;
import com.example.ahmed.mybakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    public ArrayList<Steps> mStep;
    public int mID;
    Context context;


    public StepAdapter(ArrayList<Steps> mStep, int mID, Context context) {
        this.mStep = mStep;
        this.mID = mID;
        this.context = context;
    }

        public interface CallBack {

        public void OnItemSelected(Bundle bundle);

    }



    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_recycler_list, parent, false);

        StepAdapter.StepViewHolder Sview = new StepAdapter.StepViewHolder(view);

        return Sview;
    }


    @Override
    public void onBindViewHolder(StepViewHolder holder, final int position) {

        holder.DataStep(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Configuration config = context.getResources().getConfiguration();
                if (config.smallestScreenWidthDp >= 600) {

                    Bundle bundleSend = new Bundle();
                    bundleSend.putSerializable("Step",mStep);
                    bundleSend.putInt("StepID" , position);

                    ((CallBack) context).OnItemSelected(bundleSend);

                }
                else {


                    Intent detailIntent = new Intent(context, DetailActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable(DetailActivity.GET_STEP_ARRAY , mStep);
                    args.putInt(DetailActivity.GET_STEP_ID ,position );
                    detailIntent.putExtras(args);
                    context.startActivity(detailIntent);

                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return mStep.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_number)
        TextView stepNumber;
        @BindView(R.id.step_short_desc)
        TextView shortDescription;
        @BindView(R.id.step_thumbnail)
        ImageView imageThumb ;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void DataStep (int position) {

            int stepint = mStep.get(position).getId();
            String dot = ".";
            String empty = "No Description Found ";
            stepNumber.setText(Integer.toString(stepint) +dot);

            String shortDescriptionString = mStep.get(position).getShortDescription();

            if (!TextUtils.isEmpty(shortDescriptionString)) {
                shortDescription.setText(shortDescriptionString);
            }
            else {
                shortDescription.setText(empty);
            }

            if (!TextUtils.isEmpty(mStep.get(position).getThumbnailURL())) {
                Picasso.with(context)
                        .load(mStep.get(position).getThumbnailURL())
                        .placeholder(R.drawable.ph2)
                        .error(R.drawable.ph2)
                        .into(imageThumb);
            } else {
                Picasso.with(context)
                        .load(R.drawable.ph2)
                        .placeholder(R.drawable.ph2)
                        .error(R.drawable.ph2)
                        .into(imageThumb);
            }

        }
    }
}
