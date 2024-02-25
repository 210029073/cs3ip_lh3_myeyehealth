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
import java.time.format.DateTimeFormatter;

import uk.ac.aston.ip.myeyehealth.R;

public class ReminderAlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderName = intent.getStringExtra("REMINDER_NAME");
        System.out.println("This is a Test...");
        System.out.println("You need to take " + reminderName);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String title = "";
        String content = "";
        if(reminderName != null) {
            title = reminderName + " \u23F0 " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            content = "You need to take your " + reminderName;
            NotificationChannel channel = new NotificationChannel("ReminderAlarmReciever", "Send reminders when app is running", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channel.getId()).setSmallIcon(R.drawable.medication_64)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(false);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(intent.hashCode(), notification.build());
        }
    }
}
