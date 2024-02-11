package uk.ac.aston.ip.myeyehealth.reminders;

import android.content.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
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
        List<MedicationLog> medicationLogs = new ArrayList<>();
        for(MedicationLog medicationLog : database.medicationLogsDAO().getMedicationLogs()) {
            if(!medicationLog.isMedicationTaken) {
                medicationLogs.add(medicationLog);
            }
        }

        return medicationLogs;
    }

    public List<MedicationLog> getPendingRemindersToday() {
        List<MedicationLog> medicationLogs = new ArrayList<>();
        long today = LocalDate.now().toEpochDay();
        for(MedicationLog medicationLog : database.medicationLogsDAO().getMedicationLogs()) {
            if(!medicationLog.isMedicationTaken && medicationLog.medicationTimeTaken == today) {
                medicationLogs.add(medicationLog);
            }
        }

        return medicationLogs;
    }

    public List<Reminders> getRemindersNotTakenTodayInReminders() {
        List<Reminders> remindersNotTakenToday = new ArrayList<>();
        List<MedicationLog> medicationLogs = this.getPendingRemindersToday();

        for(MedicationLog log : medicationLogs) {
            Reminders reminder = this.database.remindersDAO().findRemindersById(log.remindersNo);
            remindersNotTakenToday.add(reminder);
        }

        return remindersNotTakenToday;
    }

    public List<MedicationLog> getRemindersTakenToday() {
        List<MedicationLog> medicationLogs = new ArrayList<>();
        long today = LocalDate.now().toEpochDay();
        for(MedicationLog medicationLog : database.medicationLogsDAO().getMedicationLogs()) {
            if(medicationLog.isMedicationTaken && medicationLog.medicationTimeTaken == today) {
                medicationLogs.add(medicationLog);
            }
        }

        return medicationLogs;
    }

}
