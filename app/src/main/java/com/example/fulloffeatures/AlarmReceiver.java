package com.example.fulloffeatures;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.example.fulloffeatures.fragments.AlarmFragment;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int RINGTONE_PLAY_TIME = 30;

    @Override
    public void onReceive(Context context, Intent intent) {
        int alarmCount = getAlarmCount(context);
        if (alarmCount > 0) {
            Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            final Ringtone ringtone = RingtoneManager.getRingtone(context, notificationUri);
            ringtone.play();
            increaseAlarmCount(context);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ringtone.isPlaying())
                        ringtone.stop();
                }
            }, RINGTONE_PLAY_TIME * 1000);
        } else {
            resetAlarmCount(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent alarmIntent;
            int alarmId = Objects.requireNonNull(intent.getExtras()).getInt("alarmId");
            alarmIntent = PendingIntent.getBroadcast(context, alarmId, new Intent(context, AlarmReceiver.class), 0);

            assert alarmManager != null;
            alarmManager.cancel(alarmIntent);
        }
    }

    private void increaseAlarmCount(Context context) {
        SharedPreferences preference = Objects.requireNonNull(context.getSharedPreferences("alarm", Activity.MODE_PRIVATE));
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("ALARM_COUNT", preference.getInt("ALARM_COUNT", 0) - 1);
        Log.e("ALARM", "increase");
        editor.apply();
    }

    private void resetAlarmCount(Context context) {
        SharedPreferences preference = Objects.requireNonNull(context.getSharedPreferences("alarm", Activity.MODE_PRIVATE));
        SharedPreferences.Editor editor = preference.edit();
        Log.e("ALARM", "reset");
        editor.putInt("ALARM_COUNT", AlarmFragment.ALARM_REPEAT_COUNT);
        editor.apply();
    }

    private int getAlarmCount(Context context) {
        SharedPreferences preference = Objects.requireNonNull(context.getSharedPreferences("alarm", Activity.MODE_PRIVATE));
        int alarm_count = preference.getInt("ALARM_COUNT", 0);
        Log.e("ALARM", "getAlarmCount: " + alarm_count);
        return alarm_count;
    }
}
