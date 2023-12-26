package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.atomic.AtomicInteger;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentTumblingEAssessBinding;

public class TumblingEAssessFragment extends Fragment {

    private TumblingEAssessViewModel mViewModel;

    private TumblingETestViewModel viewModel;

    private FragmentTumblingEAssessBinding binding;

    public static TumblingEAssessFragment newInstance() {
        return new TumblingEAssessFragment();
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

        int size = letterPosSize[0];
        int position = letterPosSize[1];
        AtomicInteger currScore = new AtomicInteger();

        binding.option1Card.setOnClickListener(optionSelected -> {
            int parsedSize = (int) binding.option1.getTextSize();
            int angle = (int) binding.option1.getRotation();
            boolean isAnswerCorrect = false;

            if (angle == position) {
                Snackbar.make(getView(), "Correct answer", Snackbar.LENGTH_SHORT)
                        .setAnchorView(R.id.option_1_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
                currScore.addAndGet(1);
                isAnswerCorrect = true;
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_SHORT)
                        .setAnchorView(R.id.option_1_card)
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
                        .setAnchorView(R.id.option_2_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
                currScore.addAndGet(1);
                isAnswerCorrect = true;
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_SHORT)
                        .setAnchorView(R.id.option_2_card)
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
                        .setAnchorView(R.id.option_3_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
                currScore.addAndGet(1);
                isAnswerCorrect = true;
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_SHORT)
                        .setAnchorView(R.id.option_3_card)
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
                        .setAnchorView(R.id.option_4_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
                currScore.addAndGet(1);
                isAnswerCorrect = true;
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_SHORT)
                        .setAnchorView(R.id.option_4_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.red))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }
            calculateScore(isAnswerCorrect);
        });

    }

    private void calculateScore(boolean isCorrectAnswer) {
        if(mViewModel.getNumberOfTimesLeftEyeTested().getValue() < 5) {
            //TODO: DO LEFT EYE TEST
            //increment numberOfTimeLeftEyeTested
            mViewModel.getNumberOfTimesLeftEyeTested().observe(getViewLifecycleOwner(), integer -> {
                ++integer;
            });
//            mViewModel.setNumberOfTimesLeftEyeTested(old + 1);

            if(isCorrectAnswer) {
                //update score
                int oldScore = viewModel.getLeftEyeScore().getValue();
                viewModel.setLeftEyeScore(oldScore + 1);
            }

            //switch back to test
            NavHostFragment.findNavController(TumblingEAssessFragment.this)
                    .navigate(R.id.action_tumblingEAssessFragment_to_tumblingETestFragment);
        }

        if(mViewModel.getNumberOfTimesLeftEyeTested().getValue() == 5) {
            //TODO: SWITCH TO FRAGMENT ASKING USER TO COVER RIGHT EYE
            //if user has not seen the cover right eye message
            //then switch the fragment

            if(!mViewModel.getHasSwitchedToRightEyeTest().getValue()) {
                //set to true
                mViewModel.setHasSwitchedToRightEyeTest(true);

                NavHostFragment.findNavController(TumblingEAssessFragment.this)
                        .navigate(R.id.action_tumblingEAssessFragment_to_tumblingECoverRightEyeFragment);
            }

            //increment numberOfTimeRightEyeTested
            int old = mViewModel.getNumberOfTimesRightEyeTested().getValue();
            mViewModel.setNumberOfTimesRightEyeTested(old + 1);

            if(isCorrectAnswer) {
                //update score
                int oldScore = viewModel.getRightEyeScore().getValue();
                viewModel.setRightEyeScore(oldScore + 1);
            }

            //switch back to test
            NavHostFragment.findNavController(TumblingEAssessFragment.this)
                    .navigate(R.id.action_tumblingEAssessFragment_to_tumblingETestFragment);
        }

        else {
            //TODO: SHOW SCORE
            //RETURN HOME TEMP
//            String scoreMsg = "Score for Left eye is: " + viewModel.getLeftEyeScore().getValue() + "\n Score for Right eye is: " + viewModel.getRightEyeScore().getValue() +
//                    "\nNumber of times left eye tested: " + mViewModel.getNumberOfTimesLeftEyeTested().getValue();
            String scoreMsg =
                    "Number of times left eye tested: " + mViewModel.getNumberOfTimesLeftEyeTested().getValue();
            Snackbar.make(getView(), scoreMsg, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TumblingEAssessViewModel.class);
        // TODO: Use the ViewModel
    }

}