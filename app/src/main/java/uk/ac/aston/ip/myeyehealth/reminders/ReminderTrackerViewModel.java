package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReminderTrackerViewModel extends ViewModel {
    MutableLiveData<Integer> reminderNo = new MutableLiveData<>(0);
    MutableLiveData<String> reminderName = new MutableLiveData<>("");

    MutableLiveData<String> reminderType = new MutableLiveData<>("");

    MutableLiveData<Float> reminderDose = new MutableLiveData<>(0.0f);

    MutableLiveData<Boolean> isRepeated = new MutableLiveData<>(false);

    MutableLiveData<Long> reminderTime = new MutableLiveData<>(0l);
}
