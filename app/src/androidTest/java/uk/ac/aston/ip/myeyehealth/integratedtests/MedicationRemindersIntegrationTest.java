package uk.ac.aston.ip.myeyehealth.integratedtests;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import uk.ac.aston.ip.myeyehealth.medicationlogstest.MedicationRemindersTest;
import uk.ac.aston.ip.myeyehealth.medicationlogstest.MissedMedicationRemindersTest;

@RunWith(AndroidJUnit4.class)
public class MedicationRemindersIntegrationTest {
    private MedicationRemindersTest medicationRemindersTest;
    private MissedMedicationRemindersTest missedMedicationRemindersTest;

    @Before
    public void prepareTests() {
        this.medicationRemindersTest = new MedicationRemindersTest();
        this.missedMedicationRemindersTest = new MissedMedicationRemindersTest();
    }

    @Test
    public void runAddMedicationReminders() throws IOException {
        this.medicationRemindersTest.prepareTest();
    }

    @Test
    public void runMissedMedicationReminder() throws IOException {
        this.missedMedicationRemindersTest.prepareTests();
    }
}
