package uk.ac.aston.ip.myeyehealth.reminders.alarms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalTime;

import uk.ac.aston.ip.myeyehealth.R;

public class ReminderAlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderName = intent.getStringExtra("REMINDER_NAME");
        System.out.println("This is a Test...");
        System.out.println("You need to take " + reminderName);
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel channel = new NotificationChannel("ReminderAlarmReciever", "Send Reminders at Exact Time", NotificationManager.IMPORTANCE_HIGH);
//        notificationManager.createNotificationChannel(channel);
//
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channel.getId()).setSmallIcon(R.drawable.medication_64)
//                .setContentTitle(reminderName + " \u23F0 " + LocalTime.now().toString())
//                .setContentText("You need to take your " + reminderName)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true)
//                .setOnlyAlertOnce(true);
//        notificationManager.notify(1000, notification.build());
    }
}
