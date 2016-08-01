package com.zepp.www.musicplayer.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.TextView;
import com.zepp.www.musicplayer.R;
import com.zepp.www.musicplayer.music.PlayerService;
import com.zepp.www.musicplayer.widget.ProgressView;

public class PlayerActivity extends AppCompatActivity {

    private PlayerService mService;
    private boolean mBound = false;
    private TextView mTimeView;
    private TextView mDurationView;
    private ProgressView mProgressView;
    private final Handler mUpdateProgressHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            final int position = mService.getPosition();
            final int duration = mService.getDuration();
            onUpdateProgress(position, duration);
            sendEmptyMessageDelayed(0, DateUtils.SECOND_IN_MILLIS);
        }
    };

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to PlayerService, cast the IBinder and get PlayerService instance
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            onBind();
        }

        @Override public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            onUnbind();
        }
    };

    private void onUpdateProgress(int position, int duration) {
        if (mTimeView != null) {
            mTimeView.setText(DateUtils.formatElapsedTime(position));
        }
        if (mDurationView != null) {
            mDurationView.setText(DateUtils.formatElapsedTime(duration));
        }
        if (mProgressView != null) {
            mProgressView.setProgress(position);
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mTimeView = (TextView) findViewById(R.id.time);
        mDurationView = (TextView) findViewById(R.id.duration);
        mProgressView = (ProgressView) findViewById(R.id.progress);
    }

    @Override protected void onDestroy() {
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onDestroy();
    }

    private void onBind() {
        mUpdateProgressHandler.sendEmptyMessage(0);
    }

    private void onUnbind() {
        mUpdateProgressHandler.removeMessages(0);
    }

    public void play() {
        mService.play();
    }

    public void pause() {
        mService.pause();
    }
}
