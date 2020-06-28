package com.project.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
