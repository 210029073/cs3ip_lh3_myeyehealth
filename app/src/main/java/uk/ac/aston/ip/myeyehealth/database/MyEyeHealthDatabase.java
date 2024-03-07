package uk.ac.aston.ip.myeyehealth.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.commonsware.cwac.saferoom.SQLCipherUtils;

import net.sqlcipher.database.SupportFactory;

import java.io.IOException;

import uk.ac.aston.ip.myeyehealth.doa.HealthDAO;
import uk.ac.aston.ip.myeyehealth.doa.MedicationLogDAO;
import uk.ac.aston.ip.myeyehealth.doa.RemindersDAO;
import uk.ac.aston.ip.myeyehealth.doa.TestRecordDAO;
import uk.ac.aston.ip.myeyehealth.entities.Health;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.entities.TestRecord;
import uk.ac.aston.ip.myeyehealth.views.MissedMedicationViews;

@Database(version = 2, autoMigrations = {@AutoMigration(from = 1, to = 2)}, entities = {Reminders.class, MedicationLog.class, TestRecord.class, Health.class}, views = {MissedMedicationViews.class}, exportSchema = true)
public abstract class MyEyeHealthDatabase extends RoomDatabase {
    public static MyEyeHealthDatabase instance;
    public abstract RemindersDAO remindersDAO();

    public abstract TestRecordDAO testRecordsDAO();

    public abstract MedicationLogDAO medicationLogsDAO();

    public abstract HealthDAO healthDAO();


    public static synchronized MyEyeHealthDatabase getInstance(Context context) {

        SQLCipherUtils.State state = SQLCipherUtils.getDatabaseState(context, "myeyehealth.db");
        try {
            if (state == SQLCipherUtils.State.UNENCRYPTED) {
                SQLCipherUtils.encrypt(context, "myeyehealth.db", "ASTON_2024_cS3iP".getBytes());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        SupportFactory factory = new SupportFactory("ASTON_2024_cS3iP".getBytes());

        if(MyEyeHealthDatabase.instance == null) {
            MyEyeHealthDatabase.instance = Room.databaseBuilder(context.getApplicationContext(), MyEyeHealthDatabase.class, "myeyehealth.db")
                    .allowMainThreadQueries()
                    .openHelperFactory(factory)
                    .build();
        }
        return instance;
    }
}
