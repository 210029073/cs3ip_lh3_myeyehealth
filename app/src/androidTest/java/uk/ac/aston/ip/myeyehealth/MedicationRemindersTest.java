package uk.ac.aston.ip.myeyehealth;

import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.doa.RemindersDAO;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

@RunWith(AndroidJUnit4.class)
public class MedicationRemindersTest {

    private MyEyeHealthDatabase database;
    private RemindersDAO remindersDAO;

    @Before
    public void prepareDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        this.database = MyEyeHealthDatabase.getInstance(context);
        this.remindersDAO = this.database.remindersDAO();

    }

    @Before
    public void prepareReminders() {
        Reminders reminders = new Reminders();
        reminders.reminderName = "Hyloforte";
        reminders.reminderType = "Eye Drops";
        reminders.dose = 2.0f;
        this.remindersDAO.addReminder(reminders);
    }

    @Test
    public void getReminders() throws IOException {
        List<Reminders> remindersList = new ArrayList<>();
        remindersList = this.remindersDAO.getAll();
        assertNotEquals(0, remindersList.size());
    }
}
