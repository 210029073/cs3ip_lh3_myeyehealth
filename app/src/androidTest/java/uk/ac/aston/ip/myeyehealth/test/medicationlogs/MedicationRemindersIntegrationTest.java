package uk.ac.aston.ip.myeyehealth.test.medicationlogs;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class MedicationRemindersIntegrationTest {
    private MedicationRemindersTest medicationRemindersTest;

    @Before
    public void prepareTests() {
        this.medicationRemindersTest = new MedicationRemindersTest();
    }

    @Test
    public void runAddMedicationReminders() throws IOException {
        this.medicationRemindersTest.prepareTest();
    }
}
