package uk.ac.aston.ip.myeyehealth.doa;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.views.MissedMedicationViews;

@Dao
public interface MedicationLogDAO {
    @Query("SELECT * FROM medicationlog")
    List<MedicationLog> getMedicationLogs();

    @Insert
    void insertMedicationLog(MedicationLog medicationLog);

    @Update
    void updateMedicationLog(MedicationLog oldMedicationLog, MedicationLog newMedicationLog);

    @Delete
    void deleteMedicationLog(MedicationLog medicationLog);

    @Query(
            "SELECT * FROM medicationlog " +
                    "INNER JOIN reminders ON reminders.reminderNo == medicationlog.reminderNo " +
                    "WHERE medicationTimeTaken < :time AND medicationTaken = 0")
    List<MissedMedicationViews> findMissedDoses(Long time);

}
