package uk.ac.aston.ip.myeyehealth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import uk.ac.aston.ip.myeyehealth.databinding.FragmentVisionToolsBinding;
import uk.ac.aston.ip.myeyehealth.vision_tools.color_blindness_test.activity.ColorBlindTestActivity;
import uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge.activity.TumblingEActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisionToolsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisionToolsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentVisionToolsBinding binding;
    public VisionToolsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VisionToolsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VisionToolsFragment newInstance(String param1, String param2) {
        VisionToolsFragment fragment = new VisionToolsFragment();
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
        binding = FragmentVisionToolsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void bindActionButtons() {
        binding.optionTumblingE.setOnClickListener(item -> {
            Toast.makeText(this.getContext(), "This should launch the tumbling E test...", Toast.LENGTH_SHORT).show();
//            NavHostFragment.findNavController(VisionToolsFragment.this)
//                    .navigate(R.id.action_visionToolsFragment_to_tumblingECoverLeftEyeFragment);
            //Start the activity
            Intent intent = new Intent(getContext(), TumblingEActivity.class);
            startActivity(intent);
        });

        binding.optionColorBlindnessTest.setOnClickListener(item -> {
            Toast.makeText(this.getContext(), "This should launch the Color blindness test...", Toast.LENGTH_SHORT).show();
//            NavHostFragment.findNavController(VisionToolsFragment.this)
//                    .navigate(R.id.action_visionToolsFragment_to_colorBlindnessTest);
            Intent intent = new Intent(getContext(), ColorBlindTestActivity.class);
            startActivity(intent);
        });

        binding.optionMetricConversion.setOnClickListener(item -> {
            Toast.makeText(this.getContext(), "This should launch the metric conversion tools...", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(VisionToolsFragment.this)
                    .navigate(R.id.action_visionToolsFragment_to_metricConversionFragment);
        });

        binding.optionRecordBloodPressure.setOnClickListener(button -> {
            NavHostFragment.findNavController(VisionToolsFragment.this)
                    .navigate(R.id.action_visionToolsFragment_to_recordBloodPressureFragment);
        });

        binding.optionViewBloodPressure.setOnClickListener(button -> {
            NavHostFragment.findNavController(VisionToolsFragment.this)
                    .navigate(R.id.action_visionToolsFragment_to_listBloodPressureFragment);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindActionButtons();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(getContext(), "Exitting from vision tools", Toast.LENGTH_SHORT).show();
    }
}