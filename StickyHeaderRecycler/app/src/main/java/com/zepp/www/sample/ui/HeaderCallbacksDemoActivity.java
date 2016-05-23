package com.zepp.www.sample.ui;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.zepp.www.sample.adapters.SimpleDemoAdapter;
import com.zepp.www.stickyheaderrecyclerview.StickyHeaderLayoutManager;

public class HeaderCallbacksDemoActivity extends DemoActivity {

    private static final String TAG = HeaderCallbacksDemoActivity.class.getSimpleName();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
        recyclerView.setLayoutManager(stickyHeaderLayoutManager);

        // set a header position callback to set elevation on sticky headers, because why not
        stickyHeaderLayoutManager.setHeaderPositionChangedCallback(
                new StickyHeaderLayoutManager.HeaderPositionChangedCallback() {
                    @Override public void onHeaderPositionChanged(
                            int sectionIndex, View header,
                            StickyHeaderLayoutManager.HeaderPosition oldPosition,
                            StickyHeaderLayoutManager.HeaderPosition newPosition) {
                        Log.i(TAG, "onHeaderPositionChanged: section: "
                                + sectionIndex
                                + " -> old: "
                                + oldPosition.name()
                                + " new: "
                                + newPosition.name());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            boolean elevated =
                                    newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
                            header.setElevation(elevated ? 8 : 0);
                        }
                    }
                });

        recyclerView.setLayoutManager(stickyHeaderLayoutManager);
        recyclerView.setAdapter(new SimpleDemoAdapter(5, 5));
    }
}
