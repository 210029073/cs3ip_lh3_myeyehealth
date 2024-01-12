package uk.ac.aston.ip.myeyehealth.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalTime;

import javax.annotation.processing.Generated;


@Entity
public class Reminders {
    @PrimaryKey
    @Generated("AUTOINCREMENT")
    public int reminderNo;

    @ColumnInfo(name = "reminder_name")
    public String reminderName;

    @ColumnInfo(name = "reminder_type")
    public String reminderType;

    @ColumnInfo(name = "reminder_time")
    public long time;

    @ColumnInfo(name = "is_reminder_repeated")
    public boolean isRepeated;
}
