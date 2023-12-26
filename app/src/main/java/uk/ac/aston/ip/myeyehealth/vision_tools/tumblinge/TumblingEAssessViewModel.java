package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TumblingEAssessViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<Integer> numberOfTimesLeftEyeTested = new MutableLiveData<>(1);

    private MutableLiveData<Integer> numberOfTimesRightEyeTested = new MutableLiveData<>(1);

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
}