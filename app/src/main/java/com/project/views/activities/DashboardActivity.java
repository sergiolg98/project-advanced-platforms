package com.project.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.project.R;

public class DashboardActivity extends AppCompatActivity {

    ImageView btSettings, btInfo, btProfile, btLogout;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initWidgets();

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
            }
        });


        btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, InfoActivity.class));
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = getSharedPreferences("userAuthenticated", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userState", "");
                editor.putString("userName", "");
                editor.putString("userLastname", "");
                editor.apply();

                finish();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));

            }
        });
    }

    private void initWidgets(){
        btSettings = findViewById(R.id.bt_settings);
        btInfo = findViewById(R.id.bt_info);
        btProfile = findViewById(R.id.bt_profile);
        btLogout = findViewById(R.id.bt_logout);
    }
}
