package com.example.binea.hooktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HookedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hooked);
    }
}
