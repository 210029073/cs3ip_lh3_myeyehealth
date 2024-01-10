package uk.ac.aston.ip.myeyehealth.doa;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import uk.ac.aston.ip.myeyehealth.entities.Reminders;

@Dao
public interface RemindersDAO {
    @Query("SELECT * FROM reminders")
    List<Reminders> getAll();
}
