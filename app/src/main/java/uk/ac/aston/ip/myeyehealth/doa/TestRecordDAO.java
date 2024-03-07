package uk.ac.aston.ip.myeyehealth.doa;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import uk.ac.aston.ip.myeyehealth.entities.TestRecord;

@Dao
public interface TestRecordDAO {
    @Query("SELECT * FROM testrecord")
    List<TestRecord> getAll();

    @Insert
    void insertTestRecord(TestRecord record);

    @Insert
    void insertAllTestRecords(TestRecord... record);


    @Update
    void updateTestRecord(TestRecord old, TestRecord update);

    @Delete
    void deleteAllRecords(TestRecord... testRecords);

    @Delete
    void deleteRecord(TestRecord testRecord);

    @Query("DELETE FROM testrecord")
    void truncateTestRecords();

}
