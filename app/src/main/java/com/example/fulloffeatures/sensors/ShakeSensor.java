package com.example.fulloffeatures.sensors;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fulloffeatures.MainActivity;
import com.example.fulloffeatures.R;
import com.example.fulloffeatures.fragments.ShakeItFragment;

import static android.content.Context.SENSOR_SERVICE;

public class ShakeSensor implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor acceleratorSensor;
    Context context;
    private long lastUpdate;
    private static  int SHAKE_THRESHOLD = 20;
    private  Vibrator vibrator;

    private final int NOTIFICATION_ID = 121382;


    public ShakeSensor(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        acceleratorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        this.vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 100) {
            lastUpdate = curTime;
            float[] values = event.values;
            double x = Math.pow(values[SensorManager.DATA_X], 2);
            double y = Math.pow(values[SensorManager.DATA_Y], 2);
            double z = Math.pow(values[SensorManager.DATA_Z], 2);
            double acceleration = Math.sqrt(x+y+z);

            if (acceleration > SHAKE_THRESHOLD) {
                Log.d("sensor", "shake detected acceleration: " + acceleration + " m/s^2");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(500);
                }
                notif();
            }
        }
    }

    private void notif() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);


        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void stopListening() {
        sensorManager.unregisterListener(this);
    }


    public void startListening() {
        sensorManager.registerListener(this, acceleratorSensor,  SensorManager.SENSOR_DELAY_NORMAL);
    }
}
