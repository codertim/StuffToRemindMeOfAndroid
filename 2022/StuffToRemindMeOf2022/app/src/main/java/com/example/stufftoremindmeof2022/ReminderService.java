package com.example.stufftoremindmeof2022;


import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;


public class ReminderService extends Service {


    @Override
    public void onCreate() {
        Log.d("ReminderService", "onCreate - starting ...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // new StartServiceAsyncTask().execute(extras.get(MainActivity.MESSAGE_KEY), getBaseContext());
        Log.d("ReminderService", "onStartCommand - Starting ...");
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = setupIntentForReceiver(intent);

        // using current time for PendingIntent request code - hack for setting
        // unique id so we can have multiple reminders without the latest one
        // overwriting the previous one
        PendingIntent intentForAlarmBroadcast =
                PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), alarmIntent, PendingIntent.FLAG_MUTABLE);
        // PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        triggerAlarmInFuture(alarmManager, intentForAlarmBroadcast);

        ///return 0;  TODO: old android version code, check if should delete
        return Service.START_STICKY;
    }

    private Intent setupIntentForReceiver(Intent intent) {
        String ALARM_ACTION = ReminderReceiver.ACTION_DO_REMINDER;
        Intent intentForReceiver = new Intent(ALARM_ACTION);
        Bundle extras = intent.getExtras();
        Log.d("ReminderService", "setupIntentForReceiver - extras bundle = " + extras);
        String msgFromBundle = (String) extras.getString(MainActivity.MESSAGE_KEY);
        Log.d("ReminderService", "setupIntentForReceiver - msgFromBundle = " + msgFromBundle);
        intentForReceiver.putExtra(MainActivity.MESSAGE_KEY, msgFromBundle);

        return(intentForReceiver);
    }

    private void triggerAlarmInFuture(@NonNull AlarmManager alarmManager, PendingIntent intentForAlarmBroadcast) {
        // set alarm to trigger in future
        int minutes = getMinutesFromPreferences();
        Log.d("ReminderService", "triggerAlarmInFuture - minutes = " + minutes);
        Date t = new Date();
        t.setTime(java.lang.System.currentTimeMillis() + minutes*60*1000);
        alarmManager.set(AlarmManager.RTC_WAKEUP, t.getTime(), intentForAlarmBroadcast);
        //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, t.getTime(), intentForAlarmBroadcast);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ReminderReceiver.ACTION_DO_REMINDER);
        Context context = getApplicationContext();
        context.registerReceiver(new ReminderReceiver(), intentFilter);



        Log.d("ReminderService", "triggerAlarmInFuture - end of method");
    }

    // retrieve the number of minutes in the future to fire our reminder alert
    // minutes are saved in SharedPreferences when use click on minutes value in dialog pop-up
    private int getMinutesFromPreferences() {
        int minutes = 0;

        // get time in minutes from preferences
        Context context = getApplicationContext();
        SharedPreferences mySharedPreferences = context.getApplicationContext().getSharedPreferences(MainActivity.MY_PREFS, Activity.MODE_PRIVATE);
        int whichTimeSelected = mySharedPreferences.getInt("whichTimeSelected", -1);
        Log.d("ReminderService", "getMinutesFromPrefs - SharedPreferences - whichTimeSelected = " + whichTimeSelected);
        String [] minutesArray = context.getResources().getStringArray(R.array.minutes_array);
        String minutesText = minutesArray[whichTimeSelected];
        minutes = Integer.parseInt(minutesText);
        Log.d("ReminderService", "getMinutesFromPrefs - minutes (int) = " + minutes);

        return minutes;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
