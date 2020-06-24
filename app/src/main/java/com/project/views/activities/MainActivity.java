package com.project.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;

import com.project.R;
import com.project.background.DataJobService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText userName;
    EditText password;
    TextView dataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (EditText) findViewById(R.id.userNameInput);
        password = (EditText) findViewById(R.id.passwordInput);
        dataView = (TextView) findViewById(R.id.dataTextView);

    }

    public void scheduleJob(View v){
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

    public void cancelJob(View v){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.i(TAG, "Job Cancelled");
    }


    // Lesson 64
    //Save login info
    public void saveData(View view){
        SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginData.edit();
        editor.putString("userName", userName.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.apply();

        Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();
    }

    public void getData(View view){
        SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name = loginData.getString("userName", "");
        String pw = loginData.getString("password","");
        String msg = "Saved User Name: " + name + "\nSaved Password: " + pw;
        dataView.setText(msg);
    }

    /***
     * Interfaces
        * Login / Register
        *
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
     * Entregables
        * Subir a un Repositorio (Android & NodeJS)
        * Informe con capturas.
        * Flujo de como funciona la app (Opcional)
     */

}
