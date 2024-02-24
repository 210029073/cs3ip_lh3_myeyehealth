package uk.ac.aston.ip.myeyehealth.reminders.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import uk.ac.aston.ip.myeyehealth.entities.Reminders;

public class ReminderAlarmScheduler {
    private AlarmManager alarmManager;
    private Context context;

    public ReminderAlarmScheduler(Context context) {
        this.context = context;
        this.alarmManager = context.getSystemService(AlarmManager.class);
    }

    public void onSchedule(Reminders reminder) {
        Intent intent = new Intent(context, ReminderAlarmReciever.class);
        context.sendBroadcast(intent);
        intent.putExtra("REMINDER_NAME", reminder.reminderName);
        LocalTime localTime = LocalTime.ofNanoOfDay(reminder.time);
        LocalDateTime dateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(dateTimeNow.getYear(), dateTimeNow.getMonthValue(), dateTimeNow.getDayOfMonth(), localTime.getHour(), localTime.getMinute());
        System.out.println(localDateTime);
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                PendingIntent.getBroadcast(
                        context,
                        reminder.hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                )

        );
    }
}
