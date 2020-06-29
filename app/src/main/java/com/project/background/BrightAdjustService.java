package com.project.background;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.project.R;
import com.project.views.activities.SettingsActivity;

import static android.content.ContentValues.TAG;


public class BrightAdjustService extends Service implements SensorEventListener {

    private PowerManager pm;
    private SensorManager mSensorManager;

    private Sensor lightSensor;
    private float maxValue;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        Log.i(TAG, "AutomaticBrightAdjust started!");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.System.canWrite(this)){
                Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                i.setData(Uri.parse("package:" +  getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }

        Intent main_intent = new Intent(this, SettingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, main_intent, 0);

        /* Solo para revisar que en dispositivos superiores a Oreo funcione bien */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String NOTIFICATION_CHANNEL_ID = "com.project";
            String channelName = "AutomaticBrightAdjust";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder
                    .setOngoing(true)
                    .setContentTitle("Automatic Bright Adjust")
                    .setContentText("Servicio ejecutándose en background.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setTicker("AutomaticBrightAdjust")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(456, notification);
        }

        else {
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Automatic Bright Adjust")
                    .setContentText("Servicio ejecutándose en background.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setTicker("AutomaticBrightAdjust")
                    .setOngoing(true)
                    .build();

            notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;
            startForeground(456, notification);

        }

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);

        return Service.START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        maxValue = lightSensor.getMaximumRange();
        Log.i("MAX VALUE SENSOR: ",String.valueOf(maxValue));
        float value = sensorEvent.values[0];
        // between 0 and 255
        //int newValue = (int) (255f * value / maxValue);
        //root.setBackgroundColor(Color.rgb(newValue, newValue, newValue));

        int displayBright = (int) (227f * value/ (maxValue/50));
        Log.i("BRIGHT TO SHOW", String.valueOf(displayBright));


        if(value > 40 && value < 60){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 10);
        }
        else if(value > 60 && value < 80){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 20);
        }
        else if(value > 80 && value < 100){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 40);
        }
        else if(value > 100 && value < 120){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 60);
        }
        else if(value > 120 && value < 140){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 80);
        }
        else if(value > 140 && value < 160){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 100);
        }
        else if(displayBright > 160 && displayBright < 180){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 120);
        }
        else if(displayBright > 180 && displayBright < 200){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 140);
        }
        else if(displayBright > 200 && displayBright < 220){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 160);

        }
        else if(displayBright > 220 && displayBright < 240){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 180);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("AutomaticBrightAdjust", "Stopped after onDestroy");
    }
}
