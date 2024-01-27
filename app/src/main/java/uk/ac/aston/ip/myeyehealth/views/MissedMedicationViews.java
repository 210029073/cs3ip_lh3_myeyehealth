package uk.ac.aston.ip.myeyehealth.views;

import androidx.room.DatabaseView;
import androidx.room.Query;

@DatabaseView("SELECT * FROM medicationlog " +
                "Inner JOIN reminders ON reminders.reminderNo == medicationlog.reminderNo " +
                "WHERE (medicationTaken IS NULL)"
)
public class MissedMedicationViews {
    public int reminderNo;
    public String reminderName;
    public String reminderType;
    public float dose;

    public long time;
    public long medicationTimeTaken;
}
