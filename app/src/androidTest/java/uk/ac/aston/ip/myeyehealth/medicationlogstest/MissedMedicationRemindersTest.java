package uk.ac.aston.ip.myeyehealth.medicationlogstest;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.reminders.MedicationLogsRepository;

@RunWith(AndroidJUnit4.class)
public class MissedMedicationRemindersTest {
    private MedicationLogsRepository repository;
    private MedicationRemindersTest medicationRemindersTest;

    private MyEyeHealthDatabase myEyeHealthDatabase;

    @Before
    public void prepareMedicationRemindersTest() throws IOException {
        this.medicationRemindersTest = new MedicationRemindersTest();
        this.medicationRemindersTest.prepareTest();
        this.myEyeHealthDatabase = this.medicationRemindersTest.database;

    }

    @Before
    public void setRepository() {
        this.repository = new MedicationLogsRepository(this.myEyeHealthDatabase);
        Assert.assertNotNull(repository);
    }

    @Test
    public void getMissedRemindersForAllDurations() {
        List<MedicationLog> reMedicationViews = this.repository.getRemindersMissed();
        assertNotNull(reMedicationViews);

        assertNotEquals(0, reMedicationViews.size());
    }

    @Test
    public void getPendingRemindersForToday() {
        List<MedicationLog> reMedicationViews = this.repository.getPendingRemindersToday();
        assertNotNull(reMedicationViews);

        assertNotEquals(0, reMedicationViews.size());
    }

    @Test
    public void getTakenForToday() {
        List<MedicationLog> reMedicationViews = this.repository.getRemindersTakenToday();
        assertNotNull(reMedicationViews);

        assertNotEquals(0, reMedicationViews.size());
    }

    @Test
    public void getNotTakenReminderForDay() {
        List<Reminders> remindersNotTaken = this.repository.getRemindersNotTakenTodayInReminders();
        assertNotNull("Repository should reminders not taken in Reminders object", remindersNotTaken);
        assertNotEquals("There should be at least 1 reminder not taken for today", 0, remindersNotTaken.size());
        assertEquals("Todays not taken item should be Carbomer 0.2%.",2, remindersNotTaken.get(0).reminderNo);
    }

    public void prepareTests() throws IOException {
        this.prepareMedicationRemindersTest();
        this.setRepository();
        this.getMissedRemindersForAllDurations();
        this.getTakenForToday();
        this.getPendingRemindersForToday();
    }
}
