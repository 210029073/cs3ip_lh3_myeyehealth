package uk.ac.aston.ip.myeyehealth.reminders;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentReminderTrackerBinding;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReminderTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderTrackerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentReminderTrackerBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReminderTrackerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReminderTrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminderTrackerFragment newInstance(String param1, String param2) {
        ReminderTrackerFragment fragment = new ReminderTrackerFragment();
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
        binding = FragmentReminderTrackerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReminderTrackerViewModel reminderTrackerViewModel = new ViewModelProvider(requireActivity())
                .get(ReminderTrackerViewModel.class);

        //set text inputs
        String reminderTime = "";
        String reminderName = reminderTrackerViewModel.reminderName.getValue();
        String reminderType = reminderTrackerViewModel.reminderType.getValue();
        int reminderNo = reminderTrackerViewModel.reminderNo.getValue();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            reminderTime = String.valueOf(LocalTime.ofNanoOfDay(reminderTrackerViewModel.reminderTime.getValue()));
        }
        float reminderDose = reminderTrackerViewModel.reminderDose.getValue();
        boolean isReminderRepeated = reminderTrackerViewModel.isRepeated.getValue();

        binding.txtReminderName.getEditText().setText(reminderName);
        binding.txtReminderType.getEditText().setText(reminderType);
        binding.txtReminderDose.getEditText().setText(String.valueOf(reminderDose));
        binding.reminderTimeAmTxt.setText(reminderTime);
        binding.checkboxIsRepeat.setChecked(isReminderRepeated);

        binding.btnTakenMedicine.setOnClickListener(listener -> {
            Thread thread = new Thread(() -> {
                //create object
                MedicationLog medicationLog = createMedicationLog(reminderNo, true);

                //Store object to database
                MyEyeHealthDatabase.getInstance(getContext()).medicationLogsDAO().insertMedicationLog(medicationLog);
            });
            thread.start();
            //return back to reminders fragment
            NavHostFragment.findNavController(ReminderTrackerFragment.this)
                    .popBackStack(R.id.reminderTrackerFragment, false);

            NavHostFragment.findNavController(ReminderTrackerFragment.this).navigateUp();
        });

        binding.btnNotTakenMedicine.setOnClickListener(listener -> {
            Thread thread = new Thread(() -> {
                //create object
                MedicationLog medicationLog = createMedicationLog(reminderNo, false);

                //Store object to database
                MyEyeHealthDatabase.getInstance(getContext()).medicationLogsDAO().insertMedicationLog(medicationLog);
            });
            thread.start();
            //return back to reminders fragment
            NavHostFragment.findNavController(ReminderTrackerFragment.this)
                    .popBackStack(R.id.reminderTrackerFragment, false);

            NavHostFragment.findNavController(ReminderTrackerFragment.this).navigateUp();
        });



    }

    @NonNull
    private static MedicationLog createMedicationLog(int reminderNo, boolean hasChecked) {
        MedicationLog medicationLog = new MedicationLog();
        medicationLog.isMedicationTaken = hasChecked;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            medicationLog.medicationTimeTaken = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        }
        medicationLog.remindersNo = reminderNo;
        return medicationLog;
    }
}