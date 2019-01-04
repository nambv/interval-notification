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

public class MainApplication extends Application {

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
        Log.w("MainApplication", "onCreate");
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
                Job job3days = NotificationScheduler.get3DaysJob();
                Job job7days = NotificationScheduler.get7DaysJob();
                dispatcher.mustSchedule(job3days);
                dispatcher.mustSchedule(job7days);
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
