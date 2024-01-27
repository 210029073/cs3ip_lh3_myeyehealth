package uk.ac.aston.ip.myeyehealth.reminders.threads;

import android.content.Context;

import androidx.room.Room;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

public class AddReminderThread extends AbstractRemindersThread implements Runnable{

    private Reminders reminder;

    private Context context;

    public AddReminderThread(Reminders reminder, Context context) {
        super.reminder = reminder;
        super.context = context;
    }

    @Override
    public void run() {
        MyEyeHealthDatabase database = Room.databaseBuilder(super.context.getApplicationContext(), MyEyeHealthDatabase.class, "myeyehealth.db")
                .build();
        database.remindersDAO().addReminder(super.reminder);
    }
}
