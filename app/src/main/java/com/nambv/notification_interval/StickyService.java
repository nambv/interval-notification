package com.nambv.notification_interval;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.firebase.jobdispatcher.Job;

public class StickyService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.w("MainApplication", "Start reminder service");
        Job myJob = NotificationScheduler.get3DaysJob();
        MainApplication.getInstance().getDispatcher().mustSchedule(myJob);
    }
}