package uk.ac.aston.ip.myeyehealth.doa;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import uk.ac.aston.ip.myeyehealth.entities.Reminders;

@Dao
public interface RemindersDAO {
    @Query("SELECT * FROM reminders")
    List<Reminders> getAll();

    @Insert
    void addReminder(Reminders r);

    @Insert
    void addAllReminders(Reminders ... r);

    @Update
    void updateReminder(Reminders old, Reminders updated);

    @Delete
    void deleteReminder(Reminders candidate);

    @Delete
    void deleteAllReminders(Reminders ... candidate);
}
