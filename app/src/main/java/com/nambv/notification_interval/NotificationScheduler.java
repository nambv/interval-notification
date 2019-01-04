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
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

@SuppressWarnings("deprecation")
class NotificationScheduler {

    static final String TAG_3_DAYS = "3-days-job";
    static final String TAG_7_DAYS = "7-days-job";

    private static final int REMINDER_3_DAYS = 100;
    private static final int REMINDER_7_DAYS = 200;

    private static Job job3Days;
    private static Job job7Days;

    static Job get3DaysJob() {
        if (null == job3Days) {
            job3Days = MainApplication.getInstance().getDispatcher()
                    .newJobBuilder()
                    .setService(MyJobService.class) // the JobService that will be called
                    .setTag(TAG_3_DAYS)// uniquely identifies the job3Days
                    .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                    .setTrigger(Trigger.executionWindow(60 - 10, 60))
                    .setRecurring(true)
                    .setReplaceCurrent(false)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();
        }

        return job3Days;
    }

    static Job get7DaysJob() {
        if (null == job7Days) {
            job7Days = MainApplication.getInstance().getDispatcher()
                    .newJobBuilder()
                    .setService(MyJobService.class) // the JobService that will be called
                    .setTag(TAG_7_DAYS)// uniquely identifies the job3Days
                    .setTrigger(Trigger.executionWindow(120, 180))
                    .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                    .setRecurring(true)
                    .setReplaceCurrent(false)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();
        }

        return job7Days;
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
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_3_DAYS, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
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
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_3_DAYS, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//        am.cancel(pendingIntent);
//        pendingIntent.cancel();
//    }

    static void showNotification(Context context, Class<?> cls, String title, String content, String tag) {

        Log.w("NotificationScheduler", "showNotification");

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(REMINDER_3_DAYS, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (null == notificationManager) return;

        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(content)
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
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .build();
        }

        if (tag.equals(TAG_3_DAYS))
            notificationManager.notify(REMINDER_3_DAYS, notification);
        else
            notificationManager.notify(REMINDER_7_DAYS, notification);
    }
}
