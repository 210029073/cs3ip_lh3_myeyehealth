package uk.ac.aston.ip.myeyehealth.medicationlogstest;

import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.doa.MedicationLogDAO;
import uk.ac.aston.ip.myeyehealth.doa.RemindersDAO;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

@RunWith(AndroidJUnit4.class)
public class MedicationRemindersTest {

    MyEyeHealthDatabase database;
    RemindersDAO remindersDAO;
    MedicationLogDAO medicationLogDAO;

    @Before
    public void prepareDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        this.database = MyEyeHealthDatabase.getInstance(context);
        this.remindersDAO = this.database.remindersDAO();
        this.medicationLogDAO = this.database.medicationLogsDAO();

    }

    @Before
    public void prepareReminders() {
        Reminders reminders = new Reminders();
        reminders.reminderName = "Hyloforte";
        reminders.reminderType = "Eye Drops";
        reminders.dose = 2.0f;

        Reminders reminder1 = new Reminders();
        reminders.reminderName = "Carbomer 0.2%";
        reminders.reminderType = "Eye Gel";
        reminders.dose = 4.0f;
        this.remindersDAO.addReminder(reminders);
        this.remindersDAO.addReminder(reminder1);
    }

    private MedicationLog missedYesterdaysMedication() {
        MedicationLog medicationLog = new MedicationLog();
        medicationLog.remindersNo = 1;
        medicationLog.isMedicationTaken = false;
        medicationLog.medicationTimeTaken = LocalDate.now().minus(Period.ofDays(1)).toEpochDay();
        return  medicationLog;
    }

    private MedicationLog takenNotTodayMedication() {
        MedicationLog medicationLog1 = new MedicationLog();
        medicationLog1.remindersNo = 2;
        medicationLog1.isMedicationTaken = false;
        medicationLog1.medicationTimeTaken = LocalDate.now().toEpochDay();
        return medicationLog1;
    }

    private MedicationLog takenTodayMedication() {
        MedicationLog medicationLog1 = new MedicationLog();
        medicationLog1.remindersNo = 1;
        medicationLog1.isMedicationTaken = true;
        medicationLog1.medicationTimeTaken = LocalDate.now().toEpochDay();
        return medicationLog1;
    }

    @Before
    public void prepareMedicationLogs() {
        MedicationLog medicationLog = missedYesterdaysMedication();
        MedicationLog medicationLog1 = takenTodayMedication();
        MedicationLog medicationLog2 = takenNotTodayMedication();

        this.database.medicationLogsDAO().insertMedicationLog(medicationLog);
        this.database.medicationLogsDAO().insertMedicationLog(medicationLog1);
        this.database.medicationLogsDAO().insertMedicationLog(medicationLog2);
    }

    public void prepareTest() throws IOException {
        prepareDatabase();
        prepareReminders();
        prepareMedicationLogs();

        getReminders();
        getMedicationLogs();
    }

    @Test
    public void getReminders() throws IOException {
        List<Reminders> remindersList = new ArrayList<>();
        remindersList = this.database.remindersDAO().getAll();
        assertNotEquals(0, remindersList.size());
    }

    @Test
    public void getMedicationLogs() throws  IOException {
        List<MedicationLog> medicationLogs = this.medicationLogDAO.getMedicationLogs();
        assertNotEquals(0, medicationLogs.size());
    }

}
