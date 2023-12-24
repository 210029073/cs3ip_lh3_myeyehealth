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

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentTumblingECoverLeftEyeBinding;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentVisionToolsBinding;

public class TumblingECoverLeftEyeFragment extends Fragment {

    private TumblingECoverLeftEyeViewModel mViewModel;

    private FragmentTumblingECoverLeftEyeBinding binding;

    public static TumblingECoverLeftEyeFragment newInstance() {
        return new TumblingECoverLeftEyeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTumblingECoverLeftEyeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fragmentCoverLeftEyeTumblingeContainer.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(TumblingECoverLeftEyeFragment.this)
                    .navigate(R.id.action_tumblingECoverLeftEyeFragment_to_tumblingETestFragment);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TumblingECoverLeftEyeViewModel.class);
        // TODO: Use the ViewModel
    }

}