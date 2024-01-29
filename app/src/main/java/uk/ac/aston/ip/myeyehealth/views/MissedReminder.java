package uk.ac.aston.ip.myeyehealth.views;

public class MissedReminder {
    public int reminderNo;

    public String reminderName;

    public String reminderType;

    public float dose;

    public long time;

    public long timeStamp;

    public boolean isRepeated;

    public MissedReminder(int reminderNo, String reminderName, String reminderType, float dose, long timeStamp, boolean isRepeated) {
        this.reminderNo = reminderNo;
        this.reminderName = reminderName;
        this.reminderType = reminderType;
        this.dose = dose;
        this.time = time;
        this.timeStamp = timeStamp;
        this.isRepeated = isRepeated;
    }

    public MissedReminder(int reminderNo, String reminderName, String reminderType, float dose, boolean isRepeated) {
        this.reminderNo = reminderNo;
        this.reminderName = reminderName;
        this.reminderType = reminderType;
        this.dose = dose;
        this.time = 0L;
        this.timeStamp = 0L;
        this.isRepeated = isRepeated;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
