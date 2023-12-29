package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import java.util.Random;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.VisionToolsFragment;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentTumblingETestBinding;

public class TumblingETestFragment extends Fragment {

    private TumblingETestViewModel mViewModel;

    private FragmentTumblingETestBinding binding;

    public static TumblingETestFragment newInstance() {
        return new TumblingETestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_tumbling_e_test, container, false);
        binding = FragmentTumblingETestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.INVISIBLE);

        int[] letterE = randomManipulateE();

        //TODO: Need to hide navigation bar.

        //TODO: set end test with a prompt before closing

        //TODO: Communicate with assess fragment
        mViewModel = new ViewModelProvider(requireActivity()).get(TumblingETestViewModel.class);
        mViewModel.setCurrentLetterEPosSize(letterE);

        binding.tumblingEContainer.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(TumblingETestFragment.this)
                    .navigate(R.id.action_tumblingETestFragment_to_tumblingEAssessFragment);
        });
    }

    private int[] randomManipulateE() {
        Random random = new Random();
        int[] randomRotate = {0, 90, 180, 270, 360};
        int[] randomSize = {8, 16, 32, 64, 128};
        int letterEAngleRotation = randomRotate[random.nextInt(randomRotate.length - 1)];
        int letterESize = randomSize[random.nextInt(randomSize.length - 1)];

        binding.txtLetterE.setTextAppearance(0);
        binding.txtLetterE.setRotation(letterEAngleRotation);
        binding.txtLetterE.setTextSize(letterESize);

        return new int[]{letterESize, letterEAngleRotation};
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TumblingETestViewModel.class);
        // TODO: Use the ViewModel
    }

}