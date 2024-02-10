package uk.ac.aston.ip.myeyehealth.reminders;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.views.MissedReminder;

public class MedicationLogsRepository {
    private MyEyeHealthDatabase database;

    public MedicationLogsRepository(Context context) {
        this.database = MyEyeHealthDatabase.getInstance(context);
    }

    public MedicationLogsRepository(MyEyeHealthDatabase database) {
        this.database = database;
    }

    public List<MedicationLog> getRemindersMissed() {
        return new ArrayList<>();
    }

}
