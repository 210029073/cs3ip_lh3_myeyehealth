package uk.ac.aston.ip.myeyehealth.vision_tools.color_blindness_test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentColorBlindnessTestScore2Binding;
import uk.ac.aston.ip.myeyehealth.entities.TestRecord;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ColorBlindnessTestScore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ColorBlindnessTestScore extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ColorBlindnessTestViewModel mViewModel;


    public static class ColorBlindTestModel {
        private float score;

        public ColorBlindTestModel(float score) {
            this.score = score;
        }

        public float getScore() {
            return score;
        }
    }

    private FragmentColorBlindnessTestScore2Binding binding;


    public ColorBlindnessTestScore() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ColorBlindnessTestScore.
     */
    // TODO: Rename and change types and number of parameters
    public static ColorBlindnessTestScore newInstance(String param1, String param2) {
        ColorBlindnessTestScore fragment = new ColorBlindnessTestScore();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentColorBlindnessTestScore2Binding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity())
                .get(ColorBlindnessTestViewModel.class);
        float test_score = (getArguments().getInt("score") / 5f) * 100f;
        Log.d("Score", String.valueOf(test_score));
        String score = String.valueOf(test_score);
        binding.scoreValue.setText(score + "%");

        binding.btnContinue.setOnClickListener(v ->
        {
            System.out.println(getActivity().getClass().getName());
            if(getActivity().getClass().getName().equals("uk.ac.aston.ip.myeyehealth.vision_tools.color_blindness_test.activity.ColorBlindTestActivity")) {
                getActivity().finish();
            }
            else {
                NavHostFragment.findNavController(ColorBlindnessTestScore.this)
                        .popBackStack(R.id.colorBlindnessTest, false);
                NavHostFragment.findNavController(ColorBlindnessTestScore.this)
                        .navigateUp();
            }
            //TODO: need to store the test results
            ColorBlindnessTestScore.ColorBlindTestModel colorBlindTestModel = new ColorBlindnessTestScore.ColorBlindTestModel(test_score);
            Gson gson = new Gson();
            String output = gson.toJson(colorBlindTestModel);

            Thread thread = new Thread(() -> {

                TestRecord testRecord = new TestRecord();
                testRecord.testResultScore = output;
                testRecord.testResultDescription = "Color Blindness Test";
                testRecord.testResultRecordTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

                MyEyeHealthDatabase.getInstance(getContext())
                        .testRecordsDAO().insertTestRecord(testRecord);
            });

            thread.start();
        });
    }
}