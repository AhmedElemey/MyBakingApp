package com.example.ahmed.mybakingapp.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ahmed.mybakingapp.Activity.DetailActivity;
import com.example.ahmed.mybakingapp.Model.Steps;
import com.example.ahmed.mybakingapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;


public class DetailFragment extends Fragment {

    public final static String TAG = DetailActivity.class.getName();

    public ArrayList<Steps> mStep;
    public int mID;

    TextView mVideoDesc;
    TextView mBack;
    TextView mNext;
    RelativeLayout mNoVideo;
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    private DataSource.Factory mediaDataSourceFactory;
    Handler mainHandler;

    Uri videoURI;

    long position;

    private DefaultTrackSelector mTrackSelector;
    private boolean mShouldAutoPlay;
    private BandwidthMeter mBandwidthMeter;
    private int mLastStep;
    String mediaUrl;
    String POSITION_KEY = "POSITION";

    public interface CallBackAction {

        public void OnClickActionNext(Bundle bundle);

        public void OnClickActionBack(Bundle bundle);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        position = C.TIME_UNSET;

        if (savedInstanceState != null) {
            position = savedInstanceState.getLong(POSITION_KEY, C.TIME_UNSET);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View Dview = inflater.inflate(R.layout.fragment_detail, container, false);
        exoPlayerView = Dview.findViewById(R.id.step_video);
        mVideoDesc = (TextView) Dview.findViewById(R.id.step_desc);
        mBack = (TextView) Dview.findViewById(R.id.previous_step);
        mNext = (TextView) Dview.findViewById(R.id.next_step);
        mNoVideo = (RelativeLayout) Dview.findViewById(R.id.layout_no_media);


        mShouldAutoPlay = true;
        mBandwidthMeter = new DefaultBandwidthMeter();
        Bundle b = getArguments();

        mStep = (ArrayList<Steps>) b.getSerializable("Step");
        mID = b.getInt("StepID", 0);

        mVideoDesc.setText(mStep.get(mID).getDescription());

        if (mStep.get(mID).getVideoURL().length() != 0) {
            if (exoPlayerView.getVisibility() == View.GONE)
                exoPlayerView.setVisibility(View.VISIBLE);

            mediaUrl = mStep.get(mID).getVideoURL();

        } else {
            if (mNoVideo.getVisibility() == View.GONE)
                mNoVideo.setVisibility(View.VISIBLE);
            if (exoPlayerView.getVisibility() == View.VISIBLE)
                exoPlayerView.setVisibility(View.GONE);
        }

        mLastStep = mStep.size() - 1;
        if (mID >= mLastStep) {
            mNext.setClickable(false);
            mNext.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grayColor));
        } else {
            mNext.setClickable(true);
            mNext.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blueColor));
        }


        if (mNext.isClickable()) {
            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Configuration config = getResources().getConfiguration();
                    if (config.smallestScreenWidthDp >= 600) {

                        Bundle bundleSendDF = new Bundle();
                        bundleSendDF.putSerializable("Step", mStep);
                        bundleSendDF.putInt("StepID", mID + 1);

                        ((CallBackAction) getActivity()).OnClickActionNext(bundleSendDF);


                    } else {

                        Intent nextStep = new Intent(getActivity(), DetailActivity.class);
                        Bundle args = new Bundle();
                        args.putParcelableArrayList(DetailActivity.GET_STEP_ARRAY, mStep);
                        nextStep.putExtras(args);
                        nextStep.putExtra(DetailActivity.GET_STEP_ID, mID + 1);
                        getActivity().startActivity(nextStep);
                        getActivity().finish();

                    }


                }
            });
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mID <= 0) {
                    getActivity().finish();
                } else {
                    Configuration config = getResources().getConfiguration();
                    if (config.smallestScreenWidthDp >= 600) {

                        Bundle bundleSendDF = new Bundle();
                        bundleSendDF.putSerializable("Step", mStep);
                        bundleSendDF.putInt("StepID", mID - 1);

                        ((CallBackAction) getActivity()).OnClickActionBack(bundleSendDF);

                    } else {

                        Intent nextStep = new Intent(getActivity(), DetailActivity.class);
                        Bundle args = new Bundle();
                        args.putParcelableArrayList(DetailActivity.GET_STEP_ARRAY, mStep);
                        nextStep.putExtras(args);
                        nextStep.putExtra(DetailActivity.GET_STEP_ID, mID - 1);
                        getActivity().startActivity(nextStep);
                        getActivity().finish();

                    }
                }

            }
        });


        //      }


        return Dview;

    }


    /*private void setupVideoExoPlayer() {
        long position = 0;
        mStepVideo.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(mBandwidthMeter);
        mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), mTrackSelector);
        mStepVideo.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(mShouldAutoPlay);


        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        String mediaUrl = new String();
        mediaUrl = mStep.get(mID).getVideoURL();

        Log.e(TAG, mediaUrl);

        if (!TextUtils.isEmpty(mediaUrl)) {
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mediaUrl),
                    mMediaDataSourceFactory, extractorsFactory, null, null);

            mPlayer.prepare(mediaSource);


        } else {
            mStepVideo.setVisibility(View.GONE);
            mNoVideo.setVisibility(View.VISIBLE);
            releasePlayer();
        }
    }*/

    private void showPlayer(String url) {

        try {
            mainHandler = new Handler();
            mediaDataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), null));
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            if (url.length() > 0) {

                videoURI = Uri.parse(url);

                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

                exoPlayerView.setPlayer(exoPlayer);
                Log.e(TAG, "position ***////****///***///**//**/ " + position);
                if (position != C.TIME_UNSET) {

                    exoPlayer.seekTo(position);
                }
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);

            } else {

            }


        } catch (Exception e) {

            Log.e("MainAcvtivity", " exoplayer error " + e.toString());

        }
    }

   /* private void releasePlayer() {
        if (mPlayer != null) {
            mShouldAutoPlay = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
            mTrackSelector = null;
        }
    }*/

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            // setupVideoExoPlayer();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // if ((Util.SDK_INT <= 23 || mPlayer == null)) {
//            setupVideoExoPlayer();
        if (mediaUrl != null)
            if (mediaUrl.length() != 0)
                showPlayer(mediaUrl);
        // }
    }

    @Override
    public void onPause() {
        super.onPause();
       /* if (Util.SDK_INT <= 23) {
            releasePlayer();
        }*/
        if (exoPlayer != null) {
            position = exoPlayer.getCurrentPosition();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
       /* if (Util.SDK_INT > 23) {
            releasePlayer();
        }*/

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POSITION_KEY, position);
    }
}
