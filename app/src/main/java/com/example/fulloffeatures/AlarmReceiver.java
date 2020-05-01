package com.example.fulloffeatures;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int RINGTONE_PLAY_TIME = 30;

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        final Ringtone ringtone = RingtoneManager.getRingtone(context, notificationUri);
        ringtone.play();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ringtone.isPlaying())
                    ringtone.stop();
            }
        }, RINGTONE_PLAY_TIME * 1000);
        ringtone.stop();
    }
}
