/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.example.android.classical;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewAnimator;
import com.example.android.bluetoothchat.R;
import com.example.android.bluetoothchat.TaskIntentService;
import com.example.android.common.activities.SampleActivityBase;
import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;
import com.example.android.model.EventType;
import com.example.android.model.GlobalVar;
import com.example.android.model.MessageType;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends SampleActivityBase {

    public static final String TAG = "MainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        GlobalVar.currentDeviceMacAddress = wm.getConnectionInfo().getMacAddress();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        registerReceiver(mReceiver, intentFilter);
        intentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiver, intentFilter);
        intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, intentFilter);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

        //new Thread(){
        //    @Override public void run() {
        //        while(true) {
        //            Intent intentService = new Intent(MainActivity.this, TaskIntentService.class);
        //            intentService.putExtra(TaskIntentService.MESSAGE_TYPE, MessageType.toInt(MessageType.BALL_EVENT));
        //            intentService.putExtra(TaskIntentService.EVENT_TYPE, EventType.BALL_CONNECTED);
        //            startService(intentService);
        //            SystemClock.sleep(2000);
        //        }
        //    }
        //}.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
                    output.setDisplayedChild(1);
                } else {
                    output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            Intent intentService;
            switch (intent.getAction()){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    intentService = new Intent(context, TaskIntentService.class);
                    intentService.putExtra(TaskIntentService.MESSAGE_TYPE, MessageType.toInt(
                            MessageType.BALL_EVENT));
                    intentService.putExtra(TaskIntentService.EVENT_TYPE, EventType.BALL_CONNECTED);
                    context.startService(intentService);
                    Log.d(TAG, "bluetoothdevice bluetooth connected");
                    break;

                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    intentService = new Intent(context, TaskIntentService.class);
                    intentService.putExtra(TaskIntentService.MESSAGE_TYPE, MessageType.toInt(
                            MessageType.BALL_EVENT));
                    intentService.putExtra(TaskIntentService.EVENT_TYPE, EventType.BALL_DISCONNECTED);
                    context.startService(intentService);
                    Log.d(TAG, "bluetoothdevice bluetooth disconnected");
                    break;

                case BluetoothDevice.ACTION_FOUND:
                    Log.d(TAG, "bluetoothdevice bluetooth found");
                    break;
            }
        }
    };
}
