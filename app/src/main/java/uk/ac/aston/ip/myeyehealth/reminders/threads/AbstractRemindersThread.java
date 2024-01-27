package uk.ac.aston.ip.myeyehealth.reminders.threads;

import android.content.Context;

import androidx.room.Room;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

public  abstract class AbstractRemindersThread implements Runnable {
    Reminders reminder;

    Context context;
}
