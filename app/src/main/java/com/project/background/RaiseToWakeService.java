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
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.project.R;
import com.project.views.activities.SettingsActivity;

import static android.content.ContentValues.TAG;


public class RaiseToWakeService extends Service implements SensorEventListener {

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private PowerManager pm;
    private SensorManager mSensorManager;
    private boolean phone_was_horizontal = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private float getInclinationAngle(SensorManager mSensorManager) {
        // Rotation matrix based on current readings from accelerometer and magnetometer.
        mSensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading);
        // Express the updated rotation matrix as three orientation angles.
        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        //Log.i(TAG, "z: " + mOrientationAngles[0] + " x: " + mOrientationAngles[1] + " y: " + mOrientationAngles[2]);
        return mOrientationAngles[1]; //Pitch angle in radians
    }

    private boolean isPhoneFaceUp() {
        return mOrientationAngles[2] > -1.5;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        Log.i(TAG, "Raisetowake service started!");
        Intent main_intent = new Intent(this, SettingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, main_intent, 0);

        /* Solo para revisar que en dispositivos superiores a Oreo funcione bien */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String NOTIFICATION_CHANNEL_ID = "com.project";
            String channelName = "RaiseToWakeService";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder
                    .setOngoing(true)
                    .setContentTitle("Raise To Wake")
                    .setContentText("Servicio ejecutándose en background.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setTicker("RaiseToWake")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(123, notification);
        }

        else {
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Raise To Wake")
                    .setContentText("Servicio ejecutándose en background.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setTicker("RaiseToWake")
                    .setOngoing(true)
                    .build();

            notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;
            startForeground(123, notification);

        }

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI, new Handler());
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI, new Handler());

        return Service.START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }

        if (!pm.isInteractive()) { //check if the screen is turned off
            //check position
            float angle = getInclinationAngle(mSensorManager);

            if (phone_was_horizontal && isPhoneFaceUp() && ((angle >= -1) && (angle < -0.5))) {
                //wake up
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock screenLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Raiso");
                screenLock.acquire(1); //automatically release it
                phone_was_horizontal = false;
            } else if (Math.abs(angle) < 0.5){
                phone_was_horizontal = true;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("RaiseToWake", "Stopped after onDestroy");
    }
}