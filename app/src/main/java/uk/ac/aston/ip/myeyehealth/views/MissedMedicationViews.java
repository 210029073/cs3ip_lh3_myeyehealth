package uk.ac.aston.ip.myeyehealth.views;

import androidx.room.DatabaseView;
import androidx.room.Embedded;
import androidx.room.Query;
import androidx.room.Relation;

import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

@DatabaseView("SELECT * FROM medicationlog " +
                "Inner JOIN reminders ON reminders.reminderNo == medicationlog.reminderNo " +
                "WHERE (medicationTaken IS NULL)"
)
public class MissedMedicationViews {
    @Embedded
    public Reminders reminders;

    @Relation(
            parentColumn = "reminderNo",
            entityColumn = "reminderNo"
    )
    public MedicationLog medicationLog;
}
