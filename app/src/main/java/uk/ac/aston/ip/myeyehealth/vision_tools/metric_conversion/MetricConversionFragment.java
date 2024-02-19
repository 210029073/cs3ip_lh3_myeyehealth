package uk.ac.aston.ip.myeyehealth.vision_tools.metric_conversion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import uk.ac.aston.ip.myeyehealth.databinding.FragmentMetricConversionBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MetricConversionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MetricConversionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public FragmentMetricConversionBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MetricConversionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MetricConversionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MetricConversionFragment newInstance(String param1, String param2) {
        MetricConversionFragment fragment = new MetricConversionFragment();
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
        this.binding = FragmentMetricConversionBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get the users text input
        MutableLiveData<Integer> testingDistance = new MutableLiveData<>(0);
        MutableLiveData<Integer> distance = new MutableLiveData<>(0);

        //this gets the textview for storing the outputs for the unit conversions
        TextView testScoreOutput = binding.visualAcuityScore;
        //this stores the default output
        String defaultTestScoreOutput = testScoreOutput.getText().toString();

        MutableLiveData<String> testScoreOutputStr = new MutableLiveData<>("");

        //this will change the testScoreOutput TextView object when the user modifies the testingDistance
        binding.testingDistance.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //get the users text input
                Log.d("MetricConversionFragment", "onTextChanged: " + s);
                if(s.length() == 0) {
                    testScoreOutputStr.setValue(0 + "/" + distance.getValue());
                    testScoreOutput.setText(testScoreOutputStr.getValue());
                }
                else {
                    testingDistance.setValue(Integer.parseInt(binding.testingDistance.getEditText().getText().toString()));
                    int testDistanceFeet = (int) Integer.valueOf(testingDistance.getValue() * 10/3);
                    int distanceFeet = (int) Integer.valueOf(distance.getValue() * 10/3);
                    testScoreOutputStr.setValue(testDistanceFeet + "/" + distanceFeet);
                    testScoreOutput.setText(testScoreOutputStr.getValue());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.distance.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.equals("")) {
                    testScoreOutputStr.setValue(testingDistance.getValue() + "/" + 0);
                }
                else {
                    distance.setValue(Integer.parseInt(binding.distance.getEditText().getText().toString()));
                    int testDistanceFeet = (int) Integer.valueOf(testingDistance.getValue() * 10/3);
                    int distanceFeet = (int) Integer.valueOf(distance.getValue() * 10/3);
                    testScoreOutputStr.setValue(testDistanceFeet + "/" + distanceFeet);
                    testScoreOutput.setText(testScoreOutputStr.getValue());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}