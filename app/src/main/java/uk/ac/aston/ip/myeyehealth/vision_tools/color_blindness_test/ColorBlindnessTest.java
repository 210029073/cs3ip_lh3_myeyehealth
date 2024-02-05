package uk.ac.aston.ip.myeyehealth.vision_tools.color_blindness_test;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentColorBlindnessTestBinding;

public class ColorBlindnessTest extends Fragment {

    private ColorBlindnessTestViewModel mViewModel;

    private FragmentColorBlindnessTestBinding binding;

    public static ColorBlindnessTest newInstance() {
        return new ColorBlindnessTest();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentColorBlindnessTestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ColorBlindQuestionGenerator colorBlindQuestionGenerator = new ColorBlindQuestionGenerator();
        int imageResource = colorBlindQuestionGenerator.getRandomColorBlindQuestionResource();
        binding.colorBlindnessImageview.setImageResource(imageResource);

        binding.colorBlindnessOptionA.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));

        binding.colorBlindnessOptionB.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));

        binding.colorBlindnessOptionC.setText(String.valueOf(colorBlindQuestionGenerator.findKey(imageResource)));

        binding.colorBlindnessOptionD.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ColorBlindnessTestViewModel.class);
        // TODO: Use the ViewModel
    }

}