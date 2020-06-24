package com.project.views.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.R;
import com.project.background.DataJobService;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    ImageView btAdjustBright;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        pref = getSharedPreferences("automaticBright", Context.MODE_PRIVATE);

        initWidgets();
        btAdjustBright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String state = pref.getString("state", "");
                Log.i("SharedPref", "this:" + state);

                if(state.equals("")){
                    /* Activate JobService */
                    SharedPreferences.Editor editor = pref .edit();
                    editor.putString("state", "active"); editor.apply();
                    scheduleJob();
                    btAdjustBright.setImageResource(R.drawable.light_setting_on);
                }
                else{
                    if(state.equals("inactive")){
                        /* Activate JobService  */
                        SharedPreferences.Editor editor = pref .edit();
                        editor.putString("state", "active"); editor.apply();
                        scheduleJob();
                        btAdjustBright.setImageResource(R.drawable.light_setting_on);
                    }
                    else if(state.equals("active")){
                        /* Stop JobService */
                        SharedPreferences.Editor editor = pref .edit();
                        editor.putString("state", "inactive"); editor.apply();
                        cancelJob();
                        btAdjustBright.setImageResource(R.drawable.light_setting_off);
                    }

                }

            }
        });


    }

    private void initWidgets(){
        btAdjustBright = findViewById(R.id.bt_adjust_bright);

        /* Init icon*/
        String state = pref.getString("state", "");
        if(state.equals("")){
            btAdjustBright.setImageResource(R.drawable.light_setting_off);
        }
        else {
            if (state.equals("inactive")) btAdjustBright.setImageResource(R.drawable.light_setting_off);
            else if(state.equals("active")) btAdjustBright.setImageResource(R.drawable.light_setting_on);
        }

    }

    public void scheduleJob(){
        ComponentName componentName = new ComponentName(this, DataJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(5 * 1000)
                .build();

        JobScheduler scheduler =  (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.i(TAG, "Job Scheduled");
        }
        else{
            Log.i(TAG, "Job Scheduling Failed");
        }
    }

    public void cancelJob(){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.i(TAG, "Job Cancelled");
    }


    /***
     * Interfaces
        * Login / Register
        * Profile (Un card más en Dashboard) -> lleve a una vista donde se muestren sus datos
        * Dashboard (lleva  Settings y About)
            * About (Info sobre el desarrollo)
            * Settings (Pantalla normal con Shared Preferences para los Ajustes con Preferencias)
     * Desarrollo
         * Service que corra por detrás (JobScheduler) -> Enviar todos los datos de abajo
            * Detectar ubicación con GPS
            * Determinar orientación del dispositivo (Acelerometro)
            * Detectar movimiento (Acelerometro)
            * Bluetooth? (Ver donde se podría aplicar la comunicación)
         * Service que haga que la luz baje por detrás
         * Service para el Raise to Wake
     * Entregables
        * Subir a un Repositorio (Android & NodeJS)
        * Informe con capturas.
        * Flujo de como funciona la app (Opcional)
     */

}
