package uk.ac.aston.ip.myeyehealth.test.medicationlogs;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.reminders.MedicationLogsRepository;
import uk.ac.aston.ip.myeyehealth.views.MissedMedicationViews;
import uk.ac.aston.ip.myeyehealth.views.MissedReminder;

@RunWith(AndroidJUnit4.class)
public class MissedMedicationRemindersTest {
    private MedicationLogsRepository repository;
    private MedicationRemindersTest medicationRemindersTest;
    @Before
    public void prepareRepository() throws IOException {
        this.repository = new MedicationLogsRepository(ApplicationProvider.getApplicationContext());
        this.medicationRemindersTest = new MedicationRemindersTest();
        this.medicationRemindersTest.prepareTest();
    }

    @Test
    public void setRepository() {
        Assert.assertNotNull(repository);
    }

    @Test
    public void getMissedRemindersForAllDurations() {
        List<MedicationLog> reMedicationViews = this.repository.getRemindersMissed();
        assertNotNull(reMedicationViews);
        assertNotEquals(0, reMedicationViews.size());
    }
}
