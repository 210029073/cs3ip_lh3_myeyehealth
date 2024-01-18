package uk.ac.aston.ip.myeyehealth.reminders;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.Vector;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

public class GetReminderThread extends AbstractRemindersThread{

    private Vector<Reminders> reminders;

    public GetReminderThread(Context context) {
        super.context = context;
        this.reminders = new Vector<>();
    }

    @Override
    public void run() {
        MyEyeHealthDatabase database = Room.databaseBuilder(context.getApplicationContext(), MyEyeHealthDatabase.class, "myeyehealth.db")
                .build();
        reminders.addAll(database.remindersDAO().getAll());
        database.close();
    }


    public synchronized Vector<Reminders> getReminders() {
        return reminders;
    }
}
