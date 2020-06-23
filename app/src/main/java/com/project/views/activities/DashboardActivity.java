package com.project.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.R;

import org.apache.log4j.chainsaw.Main;

public class DashboardActivity extends AppCompatActivity {

    ImageView btSettings, btInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initWidgets();

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            }
        });


        btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, InfoActivity.class));
            }
        });
    }

    private void initWidgets(){
        btSettings = findViewById(R.id.bt_settings);
        btInfo = findViewById(R.id.bt_info);
    }
}
