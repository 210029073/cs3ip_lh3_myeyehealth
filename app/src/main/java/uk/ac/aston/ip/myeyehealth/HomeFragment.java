package uk.ac.aston.ip.myeyehealth;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import uk.ac.aston.ip.myeyehealth.databinding.FragmentHomeBinding;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentHomeBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            //this is an example of retrieving a global argument from navigation graph
//            //and outputting to logcat.
////            Log.println(Log.INFO, "Name var", (String) requireArguments().get("name"));
//            //this can be used in order to update global arguements in navigation graph
////            requireArguments().putString("name", "Home");
//            item.setChecked(false);
//            if(item.getItemId() == R.id.visionToolsFragment) {
//                Log.println(Log.INFO, "Item checked", "item: " + item.getTitle() + " checked: " + item.isChecked());
//
//                item.setChecked(true);
//                NavHostFragment.findNavController(HomeFragment.this)
//                        .navigate(R.id.action_homeFragment_to_visionToolsFragment);
//                    return true;
//            }
//            if(item.getItemId() == R.id.homeFragment) {
//                Toast.makeText(this.getContext(), "You are currently at home.", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//            return false;
//        });

        binding.cardVisionTools.setOnClickListener(card -> {
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_visionToolsFragment);
        });

        binding.cardMedicationReminders.setOnClickListener(card -> {
            Snackbar.make(getView(), "Feature not added yet...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        });

        binding.cardMissedDoses.setOnClickListener(card -> {
            Snackbar.make(getView(), "Feature not added yet...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        });

        binding.cardViewTestScores.setOnClickListener(card -> {
            Snackbar.make(getView(), "Feature not added yet...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}