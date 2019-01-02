package com.nambv.notification_interval;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.firebase.jobdispatcher.*;

@SuppressWarnings("deprecation")
class NotificationScheduler {

    private static final int REMINDER_REQUEST_CODE = 100;
    private static Job job;

    static Job getJob() {
        if (null == job) {
            job = MainApplication.getInstance().getDispatcher()
                    .newJobBuilder()
                    .setService(MyJobService.class) // the JobService that will be called
                    .setTag("my-unique-tag")// uniquely identifies the job
                    .setLifetime(Lifetime.FOREVER)
                    .setTrigger(Trigger.executionWindow(0, 60))
                    .setRecurring(true)
                    .setReplaceCurrent(false)
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();
        }

        return job;
    }

//    static void setReminder(Context context, Class<?> cls) {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MINUTE, 1);
//
//        // cancel already scheduled reminders
//        cancelReminder(context, cls);
//
//        // Enable a receiver
//
//        ComponentName receiver = new ComponentName(context, cls);
//        PackageManager pm = context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//
//
//        Intent intent1 = new Intent(context, cls);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
////        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000L, pendingIntent);
//    }
//
//    private static void cancelReminder(Context context, Class<?> cls) {
//        // Disable a receiver
//
//        ComponentName receiver = new ComponentName(context, cls);
//        PackageManager pm = context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);
//
//        Intent intent1 = new Intent(context, cls);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//        am.cancel(pendingIntent);
//        pendingIntent.cancel();
//    }

    static void showNotification(Context context, Class<?> cls, String title, String content) {

        Log.w("NotificationScheduler", "showNotification");

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String message = "Hello";
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            // Setup a NotificationChannel, Go crazy and make it public, urgent with lights, vibrations & sound.
            String myUrgentChannel = context.getPackageName();
            String channelName = "SushiHangover Urgent";

            NotificationChannel channel;
            channel = notificationManager.getNotificationChannel(myUrgentChannel);
            if (channel == null) {
                channel = new NotificationChannel(myUrgentChannel, channelName, NotificationManager.IMPORTANCE_HIGH);
                channel.enableVibration(true);
                channel.enableLights(true);
                channel.setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                        new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                );
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(channel);
            }

            notification = new Notification.Builder(context)
                    .setChannelId(myUrgentChannel)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .build();
        }

        notificationManager.notify(REMINDER_REQUEST_CODE, notification);
    }
}
