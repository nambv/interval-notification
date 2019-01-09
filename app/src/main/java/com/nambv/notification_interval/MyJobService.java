package com.nambv.notification_interval;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.Random;

import static com.nambv.notification_interval.MainApplication.*;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {

        String[] strings = getApplicationContext().getResources().getStringArray(R.array.notification_messages);
        String message = randomMessage(strings, 9);
        String[] stringsTwice = getApplicationContext().getResources().getStringArray(R.array.notification_Twice_messgaes);
        String message1 = randomMessage(stringsTwice, 4);

        switch (job.getTag()) {
            case TAG_3_DAYS:
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Circle Charm", "You haven't play CIRCLE CHARM SAGA for 3 Day!!! Would you like to do it now?", job.getTag());
                break;
            case TAG_RANDOM_MORNING:
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Circle Charm", "Good morning bro! Play amazing game for amazing day", job.getTag());
                break;
            case TAG_RANDOM_AFTERNOON:
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Circle Charm", "Good afternoon bro! Play amazing game for amazing day", job.getTag());
                break;
            case TAG_WEEKEND:
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Circle Charm", message, job.getTag());
                break;
            case TAG_7_DAYS:
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Circle Charm", "You haven't play CIRCLE CHARM SAGA for 7 Day!!! Would you like to do it now?", job.getTag());
                break;
            case "RANDOM_MSG_2_TIMES_0":
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Circle Charm", message1, job.getTag());
                break;
            case "RANDOM_MSG_2_TIMES_1":
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Circle Charm", message1, job.getTag());
                break;
            case "RANDOM_MSG_3_TIMES_0":
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Circle Charm", message, job.getTag());
                break;
            case "RANDOM_MSG_3_TIMES_1":
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Circle Charm", message, job.getTag());
                break;
            case "RANDOM_MSG_3_TIMES_2":
                NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                        "Circle Charm", message, job.getTag());
                break;
                default:
                    NotificationScheduler.showNotification(getApplicationContext(), MainActivity.class,
                            "Circle Charm", "You haven't play CIRCLE CHARM SAGA for 14 Day!!! Would you like to do it now?", job.getTag());

        }

        return false;
    }

    private String randomMessage(String[] messages, int bound) {
        Random rand = new Random();
        int ran = rand.nextInt(9);
        return messages[ran -1];
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}