package com.zepp.www.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.zepp.www.musicplayer.R;
import com.zepp.www.musicplayer.adapter.RecyclerViewAdapter;
import com.zepp.www.musicplayer.music.MusicContent;

public class MainActivity extends PlayerActivity {

    private View mCoverView;
    private View mTitleView;
    private View mTimeView;
    private View mDurationView;
    private View mProgressView;
    private View mFabView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        mCoverView = findViewById(R.id.cover);
        mTitleView = findViewById(R.id.title);
        mTimeView = findViewById(R.id.time);
        mDurationView = findViewById(R.id.duration);
        mProgressView = findViewById(R.id.progress);
        mFabView = findViewById(R.id.fab);

        // Set the recycler adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tracks);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(MusicContent.ITEMS));
    }

    public void onFabClick(View view) {
        //noinspection unchecked
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, new Pair<>(mCoverView, ViewCompat.getTransitionName(mCoverView)),
                                                                   new Pair<>(mTitleView, ViewCompat.getTransitionName(mTitleView)),
                                                                   new Pair<>(mTimeView, ViewCompat.getTransitionName(mTimeView)),
                                                                   new Pair<>(mDurationView, ViewCompat.getTransitionName(mDurationView)),
                                                                   new Pair<>(mProgressView, ViewCompat.getTransitionName(mProgressView)),
                                                                   new Pair<>(mFabView, ViewCompat.getTransitionName(mFabView)));
        ActivityCompat.startActivity(this, new Intent(this, DetailActivity.class), options.toBundle());
    }
}
