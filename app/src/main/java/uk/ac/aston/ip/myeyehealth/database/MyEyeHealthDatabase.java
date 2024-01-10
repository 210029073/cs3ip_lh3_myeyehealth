package uk.ac.aston.ip.myeyehealth.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import uk.ac.aston.ip.myeyehealth.doa.RemindersDAO;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

@Database(entities = {Reminders.class}, version = 1)
public abstract class MyEyeHealthDatabase extends RoomDatabase {
    public abstract RemindersDAO remindersDAO();
}
