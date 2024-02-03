package uk.ac.aston.ip.myeyehealth.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TestRecord {
    @PrimaryKey(autoGenerate = true)
    public int testRecordNo;

    @ColumnInfo(name = "test_result_score")
    public String testResultScore;

    @ColumnInfo(name = "test_result_description")
    public String testResultDescription;

    @ColumnInfo(name = "test_result_record_time")
    public long testResultRecordTime;

}
