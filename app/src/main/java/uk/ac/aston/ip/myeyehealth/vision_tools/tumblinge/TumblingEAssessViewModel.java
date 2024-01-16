package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TumblingEAssessViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<Integer> numberOfTimesLeftEyeTested = new MutableLiveData<>();

    private MutableLiveData<Integer> numberOfTimesRightEyeTested = new MutableLiveData<>();

    private MutableLiveData<Integer> leftEyeScore = new MutableLiveData<>(0);

    private MutableLiveData<Integer> rightEyeScore = new MutableLiveData<>(0);

    private MutableLiveData<Boolean> hasSwitchedToRightEyeTest = new MutableLiveData<>(false);

    public MutableLiveData<Integer> getNumberOfTimesLeftEyeTested() {
        return numberOfTimesLeftEyeTested;
    }

    public void setNumberOfTimesLeftEyeTested(MutableLiveData<Integer> numberOfTimesLeftEyeTested) {
        this.numberOfTimesLeftEyeTested = numberOfTimesLeftEyeTested;
    }

    public MutableLiveData<Integer> getNumberOfTimesRightEyeTested() {
        return numberOfTimesRightEyeTested;
    }

    public void setNumberOfTimesRightEyeTested(int numberOfTimesRightEyeTested) {
        this.numberOfTimesRightEyeTested.setValue(numberOfTimesRightEyeTested);
    }

    public MutableLiveData<Boolean> getHasSwitchedToRightEyeTest() {
        return this.hasSwitchedToRightEyeTest;
    }

    public void setHasSwitchedToRightEyeTest(boolean hasSwitchedToRightEyeTest) {
        this.hasSwitchedToRightEyeTest.setValue(hasSwitchedToRightEyeTest);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public MutableLiveData<Integer> getRightEyeScore() {
        return rightEyeScore;
    }

    public void setRightEyeScore(int rightEyeScore) {
        this.rightEyeScore.setValue(rightEyeScore);
    }

    public MutableLiveData<Integer> getLeftEyeScore() {
        return leftEyeScore;
    }

    public void setLeftEyeScore(int leftEyeScore) {
        this.leftEyeScore.setValue(leftEyeScore);
    }
}