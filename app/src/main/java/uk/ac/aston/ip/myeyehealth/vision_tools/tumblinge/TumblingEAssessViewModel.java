package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TumblingEAssessViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<Integer> numberOfTimesLeftEyeTested = new MutableLiveData<>(0);

    private MutableLiveData<Integer> numberOfTimesRightEyeTested = new MutableLiveData<>(0);

    private MutableLiveData<Boolean> hasSwitchedToRightEyeTest = new MutableLiveData<>(false);

    public MutableLiveData<Integer> getNumberOfTimesLeftEyeTested() {
        return numberOfTimesLeftEyeTested;
    }

    public void setNumberOfTimesLeftEyeTested(int numberOfTimesLeftEyeTested) {
        this.numberOfTimesLeftEyeTested.setValue(numberOfTimesLeftEyeTested);
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
}