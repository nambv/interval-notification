package com.nambv.notification_interval;

//public class AlarmReceiver extends BroadcastReceiver {
//
//    String TAG = "AlarmReceiver";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        Job myJob = NotificationScheduler.get3DaysJob();
//
//        if (intent.getAction() != null && context != null) {
//            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
//                // Set the alarm here.
//                Log.d(TAG, "onReceive: BOOT_COMPLETED");
//                MainApplication.getInstance().getDispatcher().mustSchedule(myJob);
//                return;
//            }
//        }
//
//        Log.d(TAG, "onReceive: ");
//
//        //Trigger the notification
//        MainApplication.getInstance().getDispatcher().mustSchedule(myJob);
//    }
//}
