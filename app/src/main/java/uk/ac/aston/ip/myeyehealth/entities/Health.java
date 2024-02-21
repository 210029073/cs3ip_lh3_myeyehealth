package uk.ac.aston.ip.myeyehealth.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Health {
    @PrimaryKey(autoGenerate = true)
    public int healthId;

    @ColumnInfo(name = "health_type")
    public String healthType;

    @ColumnInfo(name = "health_data")
    public String healthData;
}
