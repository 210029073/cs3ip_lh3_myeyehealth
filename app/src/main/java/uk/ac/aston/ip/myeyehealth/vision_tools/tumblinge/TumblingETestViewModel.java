package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TumblingETestViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    //TODO: Need to separate score for right and left eyes
    private MutableLiveData<Integer> leftEyeScore = new MutableLiveData<>();

    private MutableLiveData<Integer> rightEyeScore = new MutableLiveData<>();

    private MutableLiveData<int[]> currentLetterEPosSize = new MutableLiveData<>();

    public void setLeftEyeScore(int score) {
        this.leftEyeScore.setValue(score);
    }

    public void setRightEyeScore(int score) {
        this.rightEyeScore.setValue(score);
    }

    public void setCurrentLetterEPosSize(int[] ePosSize) {
        this.currentLetterEPosSize.setValue(ePosSize);
    }

    public LiveData<Integer> getLeftEyeScore() {
        return this.leftEyeScore;
    }

    public LiveData<Integer> getRightEyeScore() {
        return this.rightEyeScore;
    }

    public LiveData<int[]> getCurrentLetterEPosSize() {
        return this.currentLetterEPosSize;
    }
}