package uk.ac.aston.ip.myeyehealth.doa;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import uk.ac.aston.ip.myeyehealth.entities.Health;

@Dao
public interface HealthDAO {

    @Query("SELECT * FROM Health")
    List<Health> getMedicationLogs();

    @Insert
    void insertHealth(Health obj);

    @Update
    void updateHealth(Health obj);

    @Delete
    void deleteHealth(Health obj);
}
