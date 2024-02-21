package uk.ac.aston.ip.myeyehealth.vision_tools.record_blood_pressure.entity;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A model class responsible for storing blood pressure.
 *
 * @author Ibrahim Ahmad (210029073@aston.ac.uk)
 * @version 1.0.0
 * */
public class BloodPressure {
    private int sys;
    private int dia;
    private int bpm;
    private String isoDateTime;

    public BloodPressure(int sys, int dia) {
        this.sys = sys;
        this.dia = dia;
    }

    public BloodPressure setBpm(int bpm) {
        this.bpm = bpm;
        return this;
    }

    public int getSys() {
        return sys;
    }

    public int getDia() {
        return dia;
    }

    public int getBpm() {
        return bpm;
    }

    public String getIsoDateTime() {
        return isoDateTime;
    }

    public BloodPressure build() {
        this.isoDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).toString();
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
