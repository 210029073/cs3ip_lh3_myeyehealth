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
import uk.ac.aston.ip.myeyehealth.databinding.FragmentTumblingECoverRightEyeBinding;

public class TumblingECoverRightEyeFragment extends Fragment {

    private TumblingECoverRightEyeViewModel mViewModel;

    private FragmentTumblingECoverRightEyeBinding binding;

    public static TumblingECoverRightEyeFragment newInstance() {
        return new TumblingECoverRightEyeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTumblingECoverRightEyeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragmentCoverRightEyeTumblingeContainer.setOnClickListener(containerSwitch -> {
            NavHostFragment.findNavController(TumblingECoverRightEyeFragment.this)
                    .navigate(R.id.action_tumblingECoverRightEyeFragment_to_tumblingETestFragment);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TumblingECoverRightEyeViewModel.class);
        // TODO: Use the ViewModel
    }

}