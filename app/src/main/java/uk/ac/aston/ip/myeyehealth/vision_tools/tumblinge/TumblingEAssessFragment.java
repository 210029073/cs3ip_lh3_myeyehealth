package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

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

        int size = letterPosSize[0];
        int position = letterPosSize[1];

        binding.option1Card.setOnClickListener(optionSelected -> {
            int parsedSize = (int) binding.option1.getTextSize();
            int angle = (int) binding.option1.getRotation();
            if (angle == position) {
                Snackbar.make(getView(), "Correct answer", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.option_1_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.option_1_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.red))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }
        });

        binding.option2Card.setOnClickListener(optionSelected -> {
            int parsedSize = (int) binding.option2.getTextSize();
            int angle = (int) binding.option2.getRotation();
            if (angle == position) {
                Snackbar.make(getView(), "Correct answer", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.option_2_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.option_2_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.red))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }
        });

        binding.option3Card.setOnClickListener(optionSelected -> {
            int parsedSize = (int) binding.option3.getTextSize();
            int angle = (int) binding.option3.getRotation();
            if (angle == position) {
                Snackbar.make(getView(), "Correct answer", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.option_3_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.option_3_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.red))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }
        });

        binding.option4Card.setOnClickListener(optionSelected -> {
            int parsedSize = (int) binding.option4.getTextSize();
            int angle = (int) binding.option4.getRotation();
            if (angle == position) {
                Snackbar.make(getView(), "Correct answer", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.option_4_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.green))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }

            else {
                Snackbar.make(getView(), "Incorrect answer", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.option_4_card)
                        .setBackgroundTintList(getContext().getColorStateList(R.color.red))
                        .setTextColor(getContext().getColorStateList(R.color.white))
                        .setAction("Action",null).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TumblingEAssessViewModel.class);
        // TODO: Use the ViewModel
    }

}