package com.project.views.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.R;
import com.project.background.BrightAdjustService;
import com.project.background.RaiseToWakeService;

public class DashboardActivity extends AppCompatActivity {

    Context context;
    SharedPreferences pref;

    ImageView btSettings, btInfo, btProfile, btInstaPhoto, btExplore, btLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        context = this;
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

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
            }
        });

        btInstaPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, CameraActivity.class));
            }
        });

        btExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, NewsFeedActivity.class));
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Cerrar Sesión")
                        .setMessage("¿Estás seguro de salir? Los servicios se desactivarán automáticamente.")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /* Resetear preferencias */
                                pref = getSharedPreferences("userAuthenticated", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("userState", "");
                                editor.putString("userName", "");
                                editor.putString("userLastname", "");
                                editor.putString("userDateRegistered", "");
                                editor.putString("username_short", "");
                                editor.putString("userId", "");
                                editor.apply();

                                pref = getSharedPreferences("automaticBright", Context.MODE_PRIVATE);
                                editor = pref.edit();
                                editor.putString("state", "inactive");
                                editor.apply();

                                pref = getSharedPreferences("automaticRaseToWake", Context.MODE_PRIVATE);
                                editor = pref.edit();
                                editor.putString("state", "inactive");
                                editor.apply();

                                /* Desactivar los servicios */
                                if(isMyServiceRunning(RaiseToWakeService.class)){
                                    stopService(new Intent(DashboardActivity.this, RaiseToWakeService.class));
                                }
                                if(isMyServiceRunning(BrightAdjustService.class)){
                                    stopService(new Intent(DashboardActivity.this, BrightAdjustService.class));
                                }

                                finish();
                                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                            }
                        })
                        .show();
            }
        });
    }

    private void initWidgets(){
        btSettings = findViewById(R.id.bt_settings);
        btInfo = findViewById(R.id.bt_info);
        btProfile = findViewById(R.id.bt_profile);
        btInstaPhoto = findViewById(R.id.bt_instaphoto);
        btExplore = findViewById(R.id.bt_explore);
        btLogout = findViewById(R.id.bt_logout);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
