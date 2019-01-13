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

import java.util.Calendar;
import java.util.Date;

import static com.nambv.notification_interval.MainApplication.*;

@SuppressWarnings("deprecation")
class NotificationScheduler {

    private static final int REMINDER_21_DAYS = 100;
    private static final int REMINDER_7_DAYS = 200;
    private static final int REMINDER_14_DAYS = 300;
    private static final int REMINDER_MORNING = 400;
    private static final int REMINDER_AFTERNOON = 500;
    private static final int REMINDER_WEEKEND = 600;

    private static final int VALUE_1_DAYS = 60 * 60 * 24;
    private static final int VALUE_21_DAYS = VALUE_1_DAYS * 21;
    private static final int VALUE_14_DAYS = VALUE_1_DAYS * 14;
    private static final int VALUE_7_DAYS = VALUE_1_DAYS * 7;

    private static Job job21Days;
    private static Job job7Days;
    private static Job job14Days;
    private static Job randomJob;

    static Job get21DaysJob() {
        if (null == job21Days) {
            job21Days = MainApplication.getInstance().getDispatcher()
                    .newJobBuilder()
                    .setService(MyJobService.class) // the JobService that will be called
                    .setTag(TAG_21_DAYS)// uniquely identifies the job21Days
                    .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                    .setTrigger(Trigger.executionWindow(VALUE_7_DAYS - 60, VALUE_7_DAYS))
                    .setRecurring(true)
                    .setReplaceCurrent(false)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();
        }

        return job21Days;
    }

    static Job get7DaysJob() {
        if (null == job7Days) {
            job7Days = MainApplication.getInstance().getDispatcher()
                    .newJobBuilder()
                    .setService(MyJobService.class) // the JobService that will be called
                    .setTag(TAG_7_DAYS)// uniquely identifies the job21Days
                    .setTrigger(Trigger.executionWindow(VALUE_14_DAYS - 60, VALUE_14_DAYS))
                    .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                    .setRecurring(true)
                    .setReplaceCurrent(false)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();
        }

        return job7Days;
    }

    static Job get14DaysJob() {
        if (null == job14Days) {
            job14Days = MainApplication.getInstance().getDispatcher()
                    .newJobBuilder()
                    .setService(MyJobService.class) // the JobService that will be called
                    .setTag(TAG_14_DAYS)// uniquely identifies the job21Days
                    .setTrigger(Trigger.executionWindow(VALUE_21_DAYS - 60, VALUE_21_DAYS))
                    .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                    .setRecurring(true)
                    .setReplaceCurrent(false)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();
        }

        return job14Days;
    }

    static Job randomJob(Date randomDate, int startHour, int endHour, String tag) {
        if (null == randomJob) {

            Calendar now = Calendar.getInstance();
            long randomLocalTime = DateUtils.randomLocalTime(randomDate, startHour, endHour);
            long diff = randomLocalTime - now.getTimeInMillis();

            int startSeconds = (int) (diff / 1000); // tell the start seconds
            int endSeconds = startSeconds + 120; // within Five minutes

            randomJob = MainApplication.getInstance().getDispatcher()
                    .newJobBuilder()
                    .setService(MyJobService.class) // the JobService that will be called
                    .setTag(tag)// uniquely identifies the job21Days
                    .setTrigger(Trigger.executionWindow(startSeconds, endSeconds))
                    .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                    .setRecurring(true)
                    .setReplaceCurrent(false)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();
        }

        return randomJob;
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
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_21_DAYS, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
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
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_21_DAYS, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
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
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(REMINDER_21_DAYS, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (null == notificationManager) return;

        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setStyle(new Notification.BigTextStyle().bigText(content))
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
                    .setStyle(new Notification.BigTextStyle().bigText(content))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .build();
        }

        switch (tag) {
            case TAG_21_DAYS:
                notificationManager.notify(REMINDER_21_DAYS, notification);
                break;
            case TAG_7_DAYS:
                notificationManager.notify(REMINDER_7_DAYS, notification);
                break;
            case TAG_14_DAYS:
                notificationManager.notify(REMINDER_14_DAYS, notification);
                break;
            case TAG_RANDOM_MORNING:
                notificationManager.notify(REMINDER_MORNING, notification);
                break;
            case TAG_RANDOM_AFTERNOON:
                notificationManager.notify(REMINDER_AFTERNOON, notification);
                break;
            case TAG_WEEKEND:
                notificationManager.notify(REMINDER_WEEKEND, notification);
                break;
        }
    }
}
