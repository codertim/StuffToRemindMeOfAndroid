package com.example.stufftoremindmeof2022;


import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;


public class ReminderReceiver extends BroadcastReceiver {

    public static final String ACTION_DO_REMINDER = "com.example.stufftoremindmeof2022.ACTION_DO_REMINDER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ReminderReceiver", "onReceive - starting ...");
        Bundle extras = intent.getExtras();
        String msg = (String)extras.get(MainActivity.MESSAGE_KEY);
        Log.d("ReminderReceiver", "onReceive - message = " + msg);
        sendNotification(msg, context);
        /// TODO: try toast - Toast.makeText(context, string1, Toast.LENGTH_LONG).show()
        Toast.makeText(context, "Toast123", Toast.LENGTH_LONG).show();
    }


    private void sendNotification(String msg, Context context) {
        Log.d("ReminderReceiver", "sendNotification - starting ...");
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        // notification = new Notification.Builder(context).setContentTitle("Reminder").setContentText(msg).setSmallIcon(android.R.drawable.stat_sys_warning).build();   // required api 11
        // notification.icon = android.R.drawable.stat_notify_sync;
        notification.icon = android.R.drawable.stat_sys_warning;
        notification.tickerText = "Reminder";
        notification.when = System.currentTimeMillis();
        // notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        Log.d("ReminderReceiver", "sendNotification - notification tickerText = " + notification.tickerText);
        //  notification.defaults |= Notification.DEFAULT_VIBRATE;
        // notification.vibrate = new long[]{100, 500, 100, 500, 100};

        // #ff1900 (dark orange)
        // #ff3300 (web-safe orange)
        // #ff2d00 (medium orange)
        // #ff4000 (medium orange)
        // #ff5300 (light orange)
        notification.ledARGB = Color.parseColor("#FF4000");
        notification.ledOffMS = 50;
        notification.ledOnMS = 500;

        // Intent intent = new Intent(this, MainActivity.class);
        /* TODO: check deprecation for setLatestEventInfo
        notification.setLatestEventInfo(context, "Reminder", msg,
                PendingIntent.getActivity(context, 0, new Intent(), 0));
         */




        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), "notify_001");
        ///mBuilder.setContentIntent(())
        mBuilder.setSmallIcon(R.drawable.ic_mine_w_alarm);
        mBuilder.setContentTitle("Stuff to Remind Me of");
        mBuilder.setContentText(msg);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        //mBuilder.setStyle();
        String channelId = "my_channel_id";
        NotificationChannel channel = new NotificationChannel(
                channelId,
                "Channel1",
                NotificationManager.IMPORTANCE_HIGH);
        mNotificationManager.createNotificationChannel(channel);
        mBuilder.setChannelId(channelId);

        Log.d("ReminderReceiver", "sendNotification - calling notify ...");


        // PendingIntent.getActivity(context, 0, null, 0));
        // PendingIntent.getActivity(this, 1, intent, 0));
        ///mNotificationManager.notify("test" + System.currentTimeMillis(), 100, notification);
        mNotificationManager.notify(0, mBuilder.build());

        Log.d("ReminderReceiver", "sendNotification - checking if vibrate");

        // Vibrate - phone may vibrate on notification even if vibrate not set in app
        if(isVibrateSet(context)) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            // vibrator.vibrate(1000);   TODO: find deprecation replacement
        }

        Log.d("ReminderReceiver", "sendNotification - end method");
    }


    // check if vibration set in app settings
    private boolean isVibrateSet(Context context) {
        Log.i("ReminderReceiver", "isVibrateSet - starting ...");
        boolean isVibrate = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean vibratePref = prefs.getBoolean("vibrate", false);
        Log.i("ReminderReceiver", "isVibrateSet - vibrate pref=" + vibratePref);

        if(vibratePref == true) {
            isVibrate = true;
        }

        return isVibrate;
    }
}
