package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentTumblingETestScoreBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TumblingETestScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TumblingETestScoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentTumblingETestScoreBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TumblingEAssessViewModel mViewModel;

    private TumblingETestViewModel viewModel;

    public TumblingETestScoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TumblingETestScoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TumblingETestScoreFragment newInstance(String param1, String param2) {
        TumblingETestScoreFragment fragment = new TumblingETestScoreFragment();
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
        binding = FragmentTumblingETestScoreBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mViewModel = new ViewModelProvider(requireActivity()).get(TumblingEAssessViewModel.class);
        this.viewModel = new ViewModelProvider(requireActivity()).get(TumblingETestViewModel.class);

        int left = mViewModel.getLeftEyeScore().getValue();
        int right = mViewModel.getRightEyeScore().getValue();

        Log.i("LeftEyeScore", String.valueOf(left));
        binding.leftEyeTestscore.setText(String.valueOf(left));
        binding.rightEyeTestscore.setText(String.valueOf(right));

        binding.btnContinue.setOnClickListener(listener -> {

            this.mViewModel.onCleared();
            this.viewModel.onCleared();

            NavHostFragment.findNavController(TumblingETestScoreFragment.this)
                    .popBackStack(R.id.homeFragment, false);

            Navigation.findNavController(TumblingETestScoreFragment.this.getView()).navigateUp();

            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.VISIBLE);

            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setVisibility(View.VISIBLE);

        });

    }
}