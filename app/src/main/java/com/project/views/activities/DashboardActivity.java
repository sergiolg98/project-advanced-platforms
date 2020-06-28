package com.project.views.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.R;

public class DashboardActivity extends AppCompatActivity {

    Context context;
    SharedPreferences pref;

    ImageView btSettings, btInfo, btProfile, btLogout;

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
                Toast.makeText(context, "En construcción ...", Toast.LENGTH_SHORT).show();
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
                                pref = getSharedPreferences("userAuthenticated", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("userState", "");
                                editor.putString("userName", "");
                                editor.putString("userLastname", "");
                                editor.apply();

                                /* Falta desactivar los servicios */

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
        btLogout = findViewById(R.id.bt_logout);
    }
}
