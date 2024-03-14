package uk.ac.aston.ip.myeyehealth.reminders.threads;

import android.content.Context;

import androidx.room.Room;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

/**
 * AddReminderThread adds a reminder using Runnable Instance, which is a Runnable thread.
 * @author Ibrahim Ahmad
 * @version 1.1.0
 * */
public class AddReminderThread extends AbstractRemindersThread implements Runnable{


    private Reminders reminder;

    private Context context;

    /**
     * Constructs AddReminderThread
     * @param context The current context.
     * @param reminder The reminder object that we wish to store.
     * */
    public AddReminderThread(Reminders reminder, Context context) {
        super.reminder = reminder;
        super.context = context;
    }

    /**
     * This appends the reminder to the database.
     * */
    @Override
    public void run() {
        //this causes application to crash as it cannot find the database from this scope
//        MyEyeHealthDatabase database = Room.databaseBuilder(super.context.getApplicationContext(), MyEyeHealthDatabase.class, "myeyehealth.db")
//                .build();
        //fetch database from current context
        MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(super.context.getApplicationContext());
        database.remindersDAO().addReminder(super.reminder);
    }
}
