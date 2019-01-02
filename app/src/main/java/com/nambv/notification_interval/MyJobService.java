package com.nambv.notification_interval;

import android.util.Log;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {

        //Trigger the notification
        NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                "Line Puzzle Ultimate", "Let's play a game");

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}