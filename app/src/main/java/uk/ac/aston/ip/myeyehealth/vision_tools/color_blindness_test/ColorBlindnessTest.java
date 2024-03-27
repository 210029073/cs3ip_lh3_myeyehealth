package uk.ac.aston.ip.myeyehealth.vision_tools.color_blindness_test;

import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentColorBlindnessTestBinding;

public class ColorBlindnessTest extends Fragment {

    private ColorBlindnessTestViewModel mViewModel;

    private FragmentColorBlindnessTestBinding binding;

    //Stores the type of numbers accessed
    private ArrayList<String> combinationsAccessed;
    public static ColorBlindnessTest newInstance() {
        return new ColorBlindnessTest();
    }

    private int imageResource = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentColorBlindnessTestBinding.inflate(inflater, container, false);
        this.combinationsAccessed = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mViewModel = new ColorBlindnessTestViewModel();
//        this.mViewModel = new ViewModelProvider(requireActivity()).get(ColorBlindnessTestViewModel.class);
//        Log.d("ColorBlindTest", "onViewCreated: " + mViewModel.getScore());

        ColorBlindQuestionGenerator colorBlindQuestionGenerator = new ColorBlindQuestionGenerator();
        generateRandomQuestion(colorBlindQuestionGenerator);
        binding.colorBlindnessImageview.setImageResource(this.imageResource);

//            Log.d("ColorBlindTest", "onViewCreated: " + mViewModel.getNumberOfTries());


            binding.colorBlindnessOptionA.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));
            binding.colorBlindnessOptionA.setOnClickListener(v -> {

                if (colorBlindQuestionGenerator.findKey(this.imageResource).equals(binding.colorBlindnessOptionA.getText())) {
                    int tmp = mViewModel.getScore() + 1;
                    mViewModel.score.setValue(tmp);
                }
                mViewModel.numberOfTries.setValue(mViewModel.getNumberOfTries() + 1);
                colorBlindQuestionGenerator.removeKeyPair(this.imageResource);
                generateRandomQuestion(colorBlindQuestionGenerator);
//                Log.d("ColorBlindTest", "onViewCreated: " + mViewModel.getScore());

            });

            binding.colorBlindnessOptionB.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));
            binding.colorBlindnessOptionB.setOnClickListener(v -> {

                if (colorBlindQuestionGenerator.findKey(this.imageResource).equals(binding.colorBlindnessOptionB.getText())) {
                    int tmp = mViewModel.getScore() + 1;
                    mViewModel.score.setValue(tmp);
                }
                mViewModel.numberOfTries.setValue(mViewModel.getNumberOfTries() + 1);
                colorBlindQuestionGenerator.removeKeyPair(this.imageResource);
                generateRandomQuestion(colorBlindQuestionGenerator);
//                Log.d("ColorBlindTest", "onViewCreated: " + mViewModel.getScore());

            });

            binding.colorBlindnessOptionC.setText(String.valueOf(colorBlindQuestionGenerator.findKey(this.imageResource)));
            binding.colorBlindnessOptionC.setOnClickListener(v -> {

                if (colorBlindQuestionGenerator.findKey(imageResource).equals(binding.colorBlindnessOptionC.getText())) {
                    int tmp = mViewModel.getScore() + 1;
                    mViewModel.score.setValue(tmp);
                }
                mViewModel.numberOfTries.setValue(mViewModel.getNumberOfTries() + 1);
                colorBlindQuestionGenerator.removeKeyPair(this.imageResource);
                generateRandomQuestion(colorBlindQuestionGenerator);
//                Log.d("ColorBlindTest", "onViewCreated: " + mViewModel.getScore());

            });

            binding.colorBlindnessOptionD.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));
            binding.colorBlindnessOptionD.setOnClickListener(v -> {

                if (colorBlindQuestionGenerator.findKey(this.imageResource).equals(binding.colorBlindnessOptionD.getText())) {
                    int tmp = mViewModel.getScore() + 1;
                    mViewModel.score.setValue(tmp);
                }
                mViewModel.numberOfTries.setValue(mViewModel.getNumberOfTries() + 1);
                colorBlindQuestionGenerator.removeKeyPair(this.imageResource);
                generateRandomQuestion(colorBlindQuestionGenerator);
//                Log.d("ColorBlindTest", "onViewCreated: " + mViewModel.getScore());
            });

    }

    private void generateRandomQuestion(ColorBlindQuestionGenerator colorBlindQuestionGenerator) {
        if(mViewModel.getNumberOfTries() < 5) {


            this.imageResource = colorBlindQuestionGenerator.getRandomColorBlindQuestionResource();

            binding.colorBlindnessImageview.setImageResource(this.imageResource);

            //Choose a button at random to hold the answer
            int[] button_ids = new int[4];
            button_ids[0] = binding.colorBlindnessOptionA.getId();
            button_ids[1] = binding.colorBlindnessOptionB.getId();
            button_ids[2] = binding.colorBlindnessOptionC.getId();
            button_ids[3] = binding.colorBlindnessOptionD.getId();

            final int chosenId = new Random().nextInt(button_ids.length - 1);

//            binding.colorBlindnessOptionA.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));
//            binding.colorBlindnessOptionB.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));
//            binding.colorBlindnessOptionC.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));
//            binding.colorBlindnessOptionD.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));

//            View view = getView().findViewById(button_ids[chosenId]);
            View view = getView().findViewById(R.id.color_blindness_options_container);
            ViewGroup view1 = (ViewGroup) view;
            for(int i = 0; i < view1.getChildCount(); i++) {
                if (view1.getChildAt(i) instanceof Button) {
                    Button button = (Button) view1.getChildAt(i);
                        if(view1.getChildAt(i).getId() == button_ids[chosenId]) {
                            button.setText(colorBlindQuestionGenerator.findKey(this.imageResource));

                            if(this.combinationsAccessed.contains(colorBlindQuestionGenerator.findKey(this.imageResource))) {
                                button.setText(colorBlindQuestionGenerator.findKey(this.imageResource));
                            }
                            this.combinationsAccessed.add(colorBlindQuestionGenerator.findKey(this.imageResource));
                        }

                        else {
                            button.setText(String.valueOf(colorBlindQuestionGenerator.generateRandomCombination()));
                        }

                }
            }
//            for(View view : view1.getChildCount())
//            if (view instanceof Button) {
//                ((Button) view).setText(colorBlindQuestionGenerator.findKey(this.imageResource));
//            }

        }
        else {
            Bundle bundle = new Bundle();
            bundle.putInt("score", mViewModel.getScore());
            NavHostFragment.findNavController(ColorBlindnessTest.this)
                    .navigate(R.id.action_colorBlindnessTest_to_colorBlindnessTestScore2, bundle);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ColorBlindnessTestViewModel.class);
        // TODO: Use the ViewModel
    }

}