package uk.ac.aston.ip.myeyehealth.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity(indices = {@Index(name = "pk_fk_reminders_medicationlog", value = "reminderNo")}, foreignKeys = {@ForeignKey(entity = Reminders.class, parentColumns = "reminderNo", childColumns = "reminderNo", onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)})
public class MedicationLog {
    @PrimaryKey(autoGenerate = true)
    public int medicationLogNo;

    @ColumnInfo(name = "reminderNo")
    public int remindersNo;

    @ColumnInfo(name = "medicationTimeTaken")
    public long medicationTimeTaken;

    @ColumnInfo(name = "medicationTaken")
    public boolean isMedicationTaken;
}
