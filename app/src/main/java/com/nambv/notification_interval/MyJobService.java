package com.nambv.notification_interval;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {

        switch (job.getTag()) {
            case NotificationScheduler.TAG_3_DAYS:
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Line Puzzle Ultimate", "3 days message", job.getTag());
                break;
            default:
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Line Puzzle Ultimate", "7 days message", job.getTag());
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}