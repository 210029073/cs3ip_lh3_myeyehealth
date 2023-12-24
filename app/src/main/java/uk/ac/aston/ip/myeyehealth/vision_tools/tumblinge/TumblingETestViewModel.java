package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TumblingETestViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    //TODO: Need to separate score for right and left eyes
    private MutableLiveData<Integer> score;

    private MutableLiveData<Integer[]> currentLetterEPosSize;

    public void setScore(int score) {
        this.score.setValue(score);
    }

    public void setCurrentLetterEPosSize(Integer[] ePosSize) {
        this.currentLetterEPosSize.setValue(ePosSize);
    }

    public LiveData<Integer> getScore() {
        return this.score;
    }

    public LiveData<Integer[]> getCurrentLetterEPosSize() {
        return this.currentLetterEPosSize;
    }
}