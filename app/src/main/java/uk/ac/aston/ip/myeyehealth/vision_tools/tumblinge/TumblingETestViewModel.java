package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TumblingETestViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    //TODO: Need to separate score for right and left eyes

    static class TumblingETestScore {
        private float leftEyeScore = 0;

        private float rightEyeScore = 0;

        public float getLeftEyeScore() {
            return leftEyeScore;
        }

        public float getRightEyeScore() {
            return rightEyeScore;
        }

        public void setLeftEyeScore(float leftEyeScore) {
            this.leftEyeScore = leftEyeScore;
        }

        public void setRightEyeScore(float rightEyeScore) {
            this.rightEyeScore = rightEyeScore;
        }
    }

    private MutableLiveData<Integer> leftEyeScore = new MutableLiveData<>(0);

    private MutableLiveData<Integer> rightEyeScore = new MutableLiveData<>(0);

    public MutableLiveData<Integer> numberOfTimesLeftEyeTested = new MutableLiveData<>(0);

    public MutableLiveData<Integer> numberOfTimesRightEyeTested = new MutableLiveData<>(0);

    public MutableLiveData<Boolean> hasSwitchedToRightEyeTest = new MutableLiveData<>(false);


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

    @Override
    protected void onCleared() {
        super.onCleared();
        this.leftEyeScore = new MutableLiveData<>(0);
        this.rightEyeScore = new MutableLiveData<>(0);
        this.numberOfTimesLeftEyeTested = new MutableLiveData<>(0);
        this.numberOfTimesRightEyeTested = new MutableLiveData<>(0);
        this.hasSwitchedToRightEyeTest = new MutableLiveData<>(false);
    }
}