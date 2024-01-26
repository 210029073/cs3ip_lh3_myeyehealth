package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import uk.ac.aston.ip.myeyehealth.HomeFragment;
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

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setMessage("Are you want to end the game?");

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(getContext(), getView(), "You pressed yes", Snackbar.LENGTH_SHORT)
                        .show();
                NavHostFragment.findNavController(TumblingECoverRightEyeFragment.this)
                        .popBackStack(R.id.visionToolsFragment, false);

                Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
                toolbar.setVisibility(View.VISIBLE);

                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(getContext(), getView(), "You pressed no", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });

        binding.btnEndTest.setOnClickListener(v -> {
            dialog.create();
            dialog.show();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TumblingECoverRightEyeViewModel.class);
        // TODO: Use the ViewModel
    }

}