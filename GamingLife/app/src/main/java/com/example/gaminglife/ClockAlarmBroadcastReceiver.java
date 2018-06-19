package com.example.gaminglife;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

import static android.app.Notification.VISIBILITY_PRIVATE;
import static android.app.Notification.VISIBILITY_PUBLIC;

public class ClockAlarmBroadcastReceiver extends BroadcastReceiver {
    public ClockAlarmBroadcastReceiver() {
    }

    private NotificationManager clockNotificationManager;
    NotificationCompat.Builder notify;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(intent.getAction().equals("CLOCK_ALARM")){
            final Context context1=context;
            final String data=intent.getStringExtra("display_clock_context");
            final String detailClock=intent.getStringExtra("display_clock_detail_context");
            final int clockId=intent.getIntExtra("clock_ID",0);
            Intent alarmClockIntent=new Intent(context,ClockContent.class);
            alarmClockIntent.putExtra("notification_clock_data",detailClock);
            clockNotificationManager=(NotificationManager)context.getSystemService(
                    Context.NOTIFICATION_SERVICE);
            final PendingIntent pengingIntent=PendingIntent.getActivity(context1,clockId,
                    alarmClockIntent,0);
            notify=new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.icon)
                    .setAutoCancel(true)
                    .setContentTitle("你有一项重要的事情待完成")
                    .setContentText(data)
                    .setContentIntent(pengingIntent);
            notify.setVisibility(VISIBILITY_PUBLIC);
            notify.setDefaults(Notification.DEFAULT_VIBRATE);
            notify.setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_ALL);
            notify.setFullScreenIntent(pengingIntent,true);
            clockNotificationManager.notify(clockId,notify.build());

            TimerTask task=new TimerTask(){
                @Override
                public void run() {
                    clockNotificationManager.cancel(clockId);
                    NotificationCompat.Builder notify2=new NotificationCompat.Builder(context1)
                            .setSmallIcon(R.drawable.icon)
                            .setAutoCancel(true)
                            .setContentTitle("你有一项重要的事情待完成")
                            .setContentText(data)
                            .setContentIntent(pengingIntent);
                    notify2.setVisibility(VISIBILITY_PUBLIC);
                    notify2.setDefaults(Notification.DEFAULT_VIBRATE);
                    notify2.setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_ALL);
                    clockNotificationManager.notify(clockId,notify2.build());
                }
            };
            Timer timer=new Timer();
            timer.schedule(task,3000);
        }
    }
}
