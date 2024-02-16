package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManageRemindersViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    MutableLiveData<Integer> reminderNo = new MutableLiveData<>(0);
    MutableLiveData<String> reminderName = new MutableLiveData<>("");

    MutableLiveData<String> reminderType = new MutableLiveData<>("");

    MutableLiveData<Float> reminderDose = new MutableLiveData<>(0.0f);

    MutableLiveData<Boolean> isRepeated = new MutableLiveData<>(false);

    MutableLiveData<Long> reminderTime = new MutableLiveData<>(0l);

    public MutableLiveData<Integer> getReminderNo() {
        return reminderNo;
    }

    public MutableLiveData<String> getReminderName() {
        return reminderName;
    }

    public MutableLiveData<String> getReminderType() {
        return reminderType;
    }

    public MutableLiveData<Float> getReminderDose() {
        return reminderDose;
    }

    public MutableLiveData<Boolean> getIsRepeated() {
        return isRepeated;
    }

    public MutableLiveData<Long> getReminderTime() {
        return reminderTime;
    }
}