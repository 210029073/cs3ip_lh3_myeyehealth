package uk.ac.aston.ip.myeyehealth.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import uk.ac.aston.ip.myeyehealth.doa.MedicationLogDAO;
import uk.ac.aston.ip.myeyehealth.doa.RemindersDAO;
import uk.ac.aston.ip.myeyehealth.doa.TestRecordDAO;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.entities.TestRecord;

@Database(entities = {Reminders.class, MedicationLog.class, TestRecord.class}, version = 1, exportSchema = true)
public abstract class MyEyeHealthDatabase extends RoomDatabase {
    public static MyEyeHealthDatabase instance;
    public abstract RemindersDAO remindersDAO();

    public abstract TestRecordDAO testRecordsDAO();

    public abstract MedicationLogDAO medicationLogsDAO();


    public static synchronized MyEyeHealthDatabase getInstance(Context context) {
        if(MyEyeHealthDatabase.instance == null) {
            MyEyeHealthDatabase.instance = Room.databaseBuilder(context.getApplicationContext(), MyEyeHealthDatabase.class, "myeyehealth.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
