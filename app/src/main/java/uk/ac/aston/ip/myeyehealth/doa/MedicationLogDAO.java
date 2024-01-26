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
}
