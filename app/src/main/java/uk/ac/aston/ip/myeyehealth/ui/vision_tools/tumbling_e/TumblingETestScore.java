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

public class TumblingETestScore extends Fragment {

    private TumblingETestScoreViewModel mViewModel;

    public static TumblingETestScore newInstance() {
        return new TumblingETestScore();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tumbling_e_test_score, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TumblingETestScoreViewModel.class);
        // TODO: Use the ViewModel
    }

}