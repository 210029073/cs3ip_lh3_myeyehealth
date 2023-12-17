package uk.ac.aston.ip.myeyehealth.ui.vision_tools.tumbling_e;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aston.ip.myeyehealth.R;

public class CoverRightEye extends Fragment {

    private CoverRightEyeViewModel mViewModel;

    public static CoverRightEye newInstance() {
        return new CoverRightEye();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cover_right_eye, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CoverRightEyeViewModel.class);
        // TODO: Use the ViewModel
    }

}