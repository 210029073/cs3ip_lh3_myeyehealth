package uk.ac.aston.ip.myeyehealth.vision_tools.color_blindness_test;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ColorBlindnessTestViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    MutableLiveData<Integer> score;

    MutableLiveData<Integer> numberOfTries;

    public ColorBlindnessTestViewModel() {
        this.score = new MutableLiveData<>(0);
        this.numberOfTries = new MutableLiveData<>(0);
    }

    public int getScore() {
        return score.getValue();
    }

    public int getNumberOfTries() {
        return numberOfTries.getValue();
    }
}