package com.nambv.notification_interval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DateUtils {

    /**
     * Random time between beginTime and endTime
     *
     * @param startHour
     * @param endHour
     * @return
     */
    public static long randomLocalTime(Date randomDate, int startHour, int endHour) {

        long beginTime = setTimeToDate(randomDate, startHour).getTime();
        long endTime = setTimeToDate(randomDate, endHour).getTime();
        long diff = endTime - beginTime + 1;
        return beginTime + (long) (Math.random() * diff);
    }

    private static Date setTimeToDate(Date randomDate, int time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(randomDate);
        calendar.set(Calendar.HOUR_OF_DAY, time);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date randomDateFromRange(Date startDate, Date endDate) {

        boolean validate = false;
        Date randomDate = null;

        while (!validate) {
            randomDate = new Date(ThreadLocalRandom.current()
                    .nextLong(startDate.getTime(), endDate.getTime()));
            if (randomDate.after(Calendar.getInstance().getTime())) {
                validate = true;
            }
        }

        return randomDate;
    }

    public static List<Date> randomDatesFromRange(Date startDate, Date endDate, int count) {

        List<Date> dates = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dates.add(randomDateFromRange(startDate, endDate));
        }

        return dates;
    }

    static Date getStartDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    static Date getWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int i = 0; i < 6; i++) {
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.getTime();
    }

    static Date getPreviousDate(Date inputDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getWeekEndDate());
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
}
