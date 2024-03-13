package uk.ac.aston.ip.myeyehealth.vision_tools.color_blindness_test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentColourBlindTestWelcomeScreenBinding;
import uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge.TumblingEWelcomeScreenFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ColourBlindTestWelcomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ColourBlindTestWelcomeScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentColourBlindTestWelcomeScreenBinding binding;

    public ColourBlindTestWelcomeScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ColourBlindTestWelcomeScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ColourBlindTestWelcomeScreenFragment newInstance(String param1, String param2) {
        ColourBlindTestWelcomeScreenFragment fragment = new ColourBlindTestWelcomeScreenFragment();
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
        this.binding = FragmentColourBlindTestWelcomeScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.welcomeContainer.setOnClickListener(v -> {
            NavHostFragment.findNavController(ColourBlindTestWelcomeScreenFragment.this)
                    .navigate(R.id.action_colourBlindTestWelcomeScreenFragment_to_colorBlindnessTest);
        });
    }
}