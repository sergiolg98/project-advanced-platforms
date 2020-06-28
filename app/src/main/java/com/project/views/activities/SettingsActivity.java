package com.project.views.activities;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.R;
import com.project.background.DataJobService;
import com.project.background.RaiseToWakeService;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private static final int JOB_ID = 123;
    private static final long REFRESH_INTERVAL  = 5 * 60 * 1000; // 5 seconds

    ImageView btAdjustRaseToWake;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initWidgets();
        btAdjustRaseToWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref = getSharedPreferences("automaticRaseToWake", Context.MODE_PRIVATE);
                String state = pref.getString("state", "");
                Log.i("SharedPref", "this:" + state);

                if(state.equals("")){

                    activateRaiseToWake();

                    /*Store Preferences*/
                    SharedPreferences.Editor editor = pref .edit();
                    editor.putString("state", "active");
                    editor.apply();

                    /* Activate JobService */
                    scheduleJob();
                    btAdjustRaseToWake.setImageResource(R.drawable.raise_on);
                }
                else{
                    if(state.equals("inactive")){

                        activateRaiseToWake();

                        /*Store Preferences*/
                        SharedPreferences.Editor editor = pref .edit();
                        editor.putString("state", "active");
                        editor.apply();

                        /* Activate JobService  */
                        scheduleJob();
                        btAdjustRaseToWake.setImageResource(R.drawable.raise_on);
                    }
                    else if(state.equals("active")){

                        stopRaiseToWake();

                        /*Store Preferences*/
                        SharedPreferences.Editor editor = pref .edit();
                        editor.putString("state", "inactive");
                        editor.apply();

                        /* Stop JobService */
                        cancelJob();
                        btAdjustRaseToWake.setImageResource(R.drawable.raise_off);
                    }

                }

            }
        });


    }

    private void initWidgets(){
        btAdjustRaseToWake = findViewById(R.id.bt_adjust_rase_to_wake);

        /* Init icon*/
        pref = getSharedPreferences("automaticRaseToWake", Context.MODE_PRIVATE);
        String state = pref.getString("state", "");
        if(state.equals("")){
            btAdjustRaseToWake.setImageResource(R.drawable.raise_off);
        }
        else {
            if (state.equals("inactive")) btAdjustRaseToWake.setImageResource(R.drawable.raise_off);
            else if(state.equals("active")) btAdjustRaseToWake.setImageResource(R.drawable.raise_on);
        }

    }

    public void scheduleJob(){
        ComponentName componentName = new ComponentName(this, DataJobService.class);
        JobInfo info;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            /** Para nougatt corre cada 15 minutos */
            info = new JobInfo.Builder(JOB_ID, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPersisted(true)
                    .setMinimumLatency(REFRESH_INTERVAL)
                    .build();
        } else {
            /** Dispositivos anteriores normal, funciona cada 5s */
            info = new JobInfo.Builder(JOB_ID, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPersisted(true)
                    .setPeriodic(5*1000)
                    .build();
        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /** Start Experimental */

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void activateRaiseToWake(){
        if (!isMyServiceRunning(RaiseToWakeService.class)) {
            Intent intent = new Intent(this, RaiseToWakeService.class);
            startService(intent);
        }
    }

    private void stopRaiseToWake(){
        if(isMyServiceRunning(RaiseToWakeService.class)){
            stopService(new Intent(this, RaiseToWakeService.class));
        }
    }

    /** End Experimental*/


    /***
     * Interfaces
        * Login / Register
        * Profile (Un card más en Dashboard) -> lleve a una vista donde se muestren sus datos
        * Dashboard (lleva  Settings y About)
            * About (Info sobre el desarrollo)
            * Settings (Pantalla normal con Shared Preferences para los Ajustes con Preferencias)
     * Desarrollo
         * Hacer que el Logout -> desactive los servicios si hay corriendo
         * Service que haga que la luz baje por detrás
         * Service para el Raise to Wake
         * Service que corra por detrás (JobScheduler) -> Enviar todos los datos de abajo
            * Detectar ubicación con GPS
            * Determinar orientación del dispositivo (Acelerometro)
            * Detectar movimiento (Acelerometro)
            * Bluetooth? (Ver donde se podría aplicar la comunicación)
     * Entregables
        * Subir a un Repositorio (Android & NodeJS)
        * Informe con capturas.
        * Flujo de como funciona la app (Opcional)
     */

}
