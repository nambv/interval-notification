package com.nambv.notification_interval;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;

import java.util.Date;
import java.util.List;

public class MainApplication extends Application {

    static final String TAG_3_DAYS = "3-days-job";
    static final String TAG_7_DAYS = "7-days-job";
    static final String TAG_14_DAYS = "14-days-job";
    static final String TAG_RANDOM_MORNING = "random-morning-job";
    static final String TAG_RANDOM_AFTERNOON = "random-afternoon-job";
    static final String TAG_WEEKEND = "weekend-job";

    // Create a new dispatcher using the Google Play driver.
    FirebaseJobDispatcher dispatcher;
    private static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        registerActivityLifecycleCallbacks(new AppLifecycleTracker(this));
        Intent stickyService = new Intent(this, StickyService.class);
        startService(stickyService);
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public FirebaseJobDispatcher getDispatcher() {
        return dispatcher;
    }

    class AppLifecycleTracker implements Application.ActivityLifecycleCallbacks {

        private int numStarted = 0;
        private Context context;

        AppLifecycleTracker(Context context) {
            this.context = context;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (numStarted == 0) {
                Log.w("MainApplication", "App is foreground");
            }
            numStarted++;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            numStarted--;
            if (numStarted == 0) {
                Log.w("MainApplication", "Start reminder");

                // 3Days not play
                Job job3days = NotificationScheduler.get3DaysJob();

                // 7Days not play
                Job job7days = NotificationScheduler.get7DaysJob();

                // 14Days not play
                Job job14days = NotificationScheduler.get14DaysJob();

                // Once a week
                Job morningJob = NotificationScheduler.randomJob(
                        DateUtils.randomDateFromRange(
                                DateUtils.getStartDayOfWeek(),
                                DateUtils.getWeekEndDate()),
                        6, 12, TAG_RANDOM_MORNING
                );

                Job afternoonJob = NotificationScheduler.randomJob(
                        DateUtils.randomDateFromRange(
                                DateUtils.getStartDayOfWeek(),
                                DateUtils.getWeekEndDate()),
                        12, 17, TAG_RANDOM_AFTERNOON
                );

                Job weekendJob = NotificationScheduler.randomJob(
                        DateUtils.randomDateFromRange(
                                DateUtils.getPreviousDate(DateUtils.getWeekEndDate()),
                                DateUtils.getWeekEndDate()),
                        10, 11, TAG_WEEKEND
                );

                // 2 times a week
                List<Date> two_dates = DateUtils.randomDatesFromRange(DateUtils.getStartDayOfWeek(),
                        DateUtils.getWeekEndDate(), 2);
                for (int i = 0; i < two_dates.size(); i++) {
                    Date date = two_dates.get(i);
                    Job job = NotificationScheduler.randomJob(date, 18, 24, "RANDOM_MSG_2_TIMES_ " + i);
                    dispatcher.mustSchedule(job);
                }

                // 3 times a week
                List<Date> three_dates = DateUtils.randomDatesFromRange(DateUtils.getStartDayOfWeek(),
                        DateUtils.getWeekEndDate(), 3);
                for (int i = 0; i < three_dates.size(); i++) {
                    Date date = three_dates.get(i);
                    Job job = NotificationScheduler.randomJob(date, 6, 17, "RANDOM_MSG_3_TIMES_" + i);
                    dispatcher.mustSchedule(job);
                }

                dispatcher.mustSchedule(job3days);
                dispatcher.mustSchedule(job7days);
                dispatcher.mustSchedule(job14days);
                dispatcher.mustSchedule(morningJob);
                dispatcher.mustSchedule(afternoonJob);
                dispatcher.mustSchedule(weekendJob);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
