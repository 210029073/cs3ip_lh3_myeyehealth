package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpdateRemindersViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<Integer> reminderId = new MutableLiveData<>(0);
    private String reminderName;
    private String reminderType;
    private float dose;
    private long time;
    private boolean isRepeated;

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }


    public void setReminderId(int reminderId) {
        this.reminderId.setValue(reminderId);
    }

    public int getReminderId() {
        return reminderId.getValue();
    }

    public void setDose(float dose) {
        this.dose = dose;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public long getTime() {
        return time;
    }

    public String getReminderName() {
        return reminderName;
    }

    public String getReminderType() {
        return reminderType;
    }

    public float getDose() {
        return dose;
    }

    public boolean isRepeated() {
        return isRepeated;
    }
}