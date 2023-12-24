package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.ip.myeyehealth.R;

public class TumblingECoverRightEyeFragment extends Fragment {

    private TumblingECoverRightEyeViewModel mViewModel;

    public static TumblingECoverRightEyeFragment newInstance() {
        return new TumblingECoverRightEyeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tumbling_e_cover_right_eye, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TumblingECoverRightEyeViewModel.class);
        // TODO: Use the ViewModel
    }

}