package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentTumblingEAssessBinding;
import uk.ac.aston.ip.myeyehealth.entities.TestRecord;

public class TumblingEAssessFragment extends Fragment {

    private TumblingEAssessViewModel mViewModel;

    private TumblingETestViewModel viewModel;

    private FragmentTumblingEAssessBinding binding;

    public static TumblingEAssessFragment newInstance() {
        return new TumblingEAssessFragment();
    }

    //TODO: NEED TO DESTROY SESSION


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTumblingEAssessBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: Need to hide navigation bar

        //TODO: get data from view model
        this.viewModel = new ViewModelProvider(requireActivity()).get(TumblingETestViewModel.class);
        int[] letterPosSize = this.viewModel.getCurrentLetterEPosSize().getValue();

        this.mViewModel = new ViewModelProvider(requireActivity()).get(TumblingEAssessViewModel.class);
        mViewModel.setLeftEyeScore(viewModel.getLeftEyeScore().getValue());
        mViewModel.setRightEyeScore(viewModel.getRightEyeScore().getValue());
        int size = letterPosSize[0];
        int position = letterPosSize[1];
        AtomicInteger currScore = new AtomicInteger();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.INVISIBLE);
        binding.option1Card.setOnClickListener(optionSelected -> {
            int parsedSize = (int) binding.option1.getTextSize();
            int angle = (int) binding.option1.getRotation();
            boolean isAnswerCorrect = false;

            if (angle == position) {
                Snackbar.make(getView(), "Correct answer", Snackbar.LENGTH_SHORT)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
                currScore.addAndGet(1);
                isAnswerCorrect = true;
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_SHORT)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.red))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }
            calculateScore(isAnswerCorrect);
        });

        binding.option2Card.setOnClickListener(optionSelected -> {
            int parsedSize = (int) binding.option2.getTextSize();
            int angle = (int) binding.option2.getRotation();
            boolean isAnswerCorrect = false;
            if (angle == position) {
                Snackbar.make(getView(), "Correct answer", Snackbar.LENGTH_SHORT)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
                currScore.addAndGet(1);
                isAnswerCorrect = true;
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_SHORT)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.red))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }
            calculateScore(isAnswerCorrect);
        });

        binding.option3Card.setOnClickListener(optionSelected -> {
            int parsedSize = (int) binding.option3.getTextSize();
            int angle = (int) binding.option3.getRotation();
            boolean isAnswerCorrect = false;
            if (angle == position) {
                Snackbar.make(getView(), "Correct answer", Snackbar.LENGTH_SHORT)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
                currScore.addAndGet(1);
                isAnswerCorrect = true;
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_SHORT)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.red))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }
            calculateScore(isAnswerCorrect);
        });

        binding.option4Card.setOnClickListener(optionSelected -> {
            int parsedSize = (int) binding.option4.getTextSize();
            int angle = (int) binding.option4.getRotation();
            boolean isAnswerCorrect = false;
            if (angle == position) {
                Snackbar.make(getView(), "Correct answer", Snackbar.LENGTH_SHORT)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
                currScore.addAndGet(1);
                isAnswerCorrect = true;
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_SHORT)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.red))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }
            calculateScore(isAnswerCorrect);
        });

        binding.btnNotSure.setOnClickListener(listener -> {
            calculateScore(false);
        });

    }

    private void calculateScore(boolean isCorrectAnswer) {
        if(viewModel.numberOfTimesLeftEyeTested.getValue() < 5) {
            //TODO: DO LEFT EYE TEST
            //increment numberOfTimeLeftEyeTested
            if(mViewModel.getNumberOfTimesLeftEyeTested() == null) {
//                mViewModel.setNumberOfTimesLeftEyeTested(new MutableLiveData<>(1));
            }
            else {
                int old = viewModel.numberOfTimesLeftEyeTested.getValue();
                viewModel.numberOfTimesLeftEyeTested.setValue(old + 1);
            }
//            mViewModel.setNumberOfTimesLeftEyeTested(old + 1);

            if(isCorrectAnswer) {
                //update score
                int oldScore = viewModel.getLeftEyeScore().getValue();
                viewModel.setLeftEyeScore(oldScore + 1);
                mViewModel.setLeftEyeScore(oldScore + 1);
            }


            //switch back to test
            NavHostFragment.findNavController(TumblingEAssessFragment.this)
                    .navigate(R.id.action_tumblingEAssessFragment_to_tumblingETestFragment);
        }

        if(viewModel.numberOfTimesLeftEyeTested.getValue() == 5) {
            //TODO: SWITCH TO FRAGMENT ASKING USER TO COVER RIGHT EYE
            //if user has not seen the cover right eye message
            //then switch the fragment

            if(!viewModel.hasSwitchedToRightEyeTest.getValue()) {
                //set to true
                viewModel.hasSwitchedToRightEyeTest.setValue(true);
                NavHostFragment.findNavController(TumblingEAssessFragment.this)
                        .navigate(R.id.action_tumblingETestFragment_to_tumblingECoverRightEyeFragment);

//                Navigation.findNavController(getView()).navigate(R.id.action_tumblingEAssessFragment_to_tumblingECoverRightEyeFragment);
            }

            else {

                if(viewModel.numberOfTimesRightEyeTested.getValue() == 5) {
//                    Snackbar.make(getView(),"Left Eye: " + viewModel.getLeftEyeScore().getValue() + "\nRight Eye: " + viewModel.getRightEyeScore().getValue(), Snackbar.LENGTH_LONG)
//                            .show();
                    TumblingETestViewModel.TumblingETestScore tumblingETestScore = new TumblingETestViewModel.TumblingETestScore();
                    float leftEyeScore = viewModel.getLeftEyeScore().getValue() / 5f * 100;
                    tumblingETestScore.setLeftEyeScore(leftEyeScore);

                    float rightEyeScore = viewModel.getRightEyeScore().getValue() / 5f * 100;
                    tumblingETestScore.setRightEyeScore(rightEyeScore);

                    Gson gson = new Gson();
                    String output = gson.toJson(tumblingETestScore);
//                    Log.d("Test Result Json", output);

//                    Log.d("Left Eye Score", String.valueOf(tumblingETestScore.getLeftEyeScore()));
//                    Log.d("Right Eye Score", String.valueOf(tumblingETestScore.getRightEyeScore()));

                    MyEyeHealthDatabase myEyeHealthDatabase = MyEyeHealthDatabase.getInstance(getContext());
                    Thread thread = new Thread(() -> {
                        TestRecord testRecord = new TestRecord();
                        testRecord.testResultScore = output;
                        testRecord.testResultDescription = "Tumbling E Test";
                        testRecord.testResultRecordTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

                        myEyeHealthDatabase.testRecordsDAO().insertTestRecord(testRecord);
//                        myEyeHealthDatabase.close();
                    });
                    thread.start();
                    viewModel.onCleared();
//                    mViewModel.onCleared();
                    //switch back to test

//                    mViewModel.setLeftEyeScore(viewModel.getLeftEyeScore().getValue());
//                    mViewModel.setRightEyeScore(viewModel.getRightEyeScore().getValue());

                    Navigation.findNavController(TumblingEAssessFragment.this.getView()).navigate(R.id.action_tumblingEAssessFragment_to_tumblingETestScoreFragment);

                    return;
                }

                //increment numberOfTimeRightEyeTested
                int old = viewModel.numberOfTimesRightEyeTested.getValue();
                viewModel.numberOfTimesRightEyeTested.setValue(old + 1);

                if (isCorrectAnswer) {
                    //update score
                    int oldScore = viewModel.getRightEyeScore().getValue();
                    viewModel.setRightEyeScore(oldScore + 1);
                    mViewModel.setRightEyeScore(oldScore + 1);
                }
                //switch back to test
                NavHostFragment.findNavController(TumblingEAssessFragment.this)
                        .navigate(R.id.action_tumblingEAssessFragment_to_tumblingETestFragment);
            }
        }

        else {
            //TODO: SHOW SCORE
            //RETURN HOME TEMP
//            String scoreMsg = "Score for Left eye is: " + viewModel.getLeftEyeScore().getValue() + "\n Score for Right eye is: " + viewModel.getRightEyeScore().getValue() +
//                    "\nNumber of times left eye tested: " + mViewModel.getNumberOfTimesLeftEyeTested().getValue();
//            String scoreMsg =
//                    "Number of times left eye tested: " + viewModel.numberOfTimesLeftEyeTested.getValue();
//            Snackbar.make(getView(), scoreMsg, Snackbar.LENGTH_LONG)
//                    .show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TumblingEAssessViewModel.class);
        // TODO: Use the ViewModel
    }

}