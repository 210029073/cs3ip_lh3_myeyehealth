package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RemindersViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    MutableLiveData<String> reminderName = new MutableLiveData<>("");

    MutableLiveData<String> reminderType = new MutableLiveData<>("");

    MutableLiveData reminderDose = new MutableLiveData(0);

    MutableLiveData isRepeated = new MutableLiveData(false);

    MutableLiveData reminderTime = new MutableLiveData(0l);
}