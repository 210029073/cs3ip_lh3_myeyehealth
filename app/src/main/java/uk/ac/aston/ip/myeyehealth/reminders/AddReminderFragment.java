package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalTime;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentAddReminderBinding;

public class AddReminderFragment extends Fragment {

    private AddReminderViewModel mViewModel;

    private FragmentAddReminderBinding binding;

    public static AddReminderFragment newInstance() {
        return new AddReminderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddReminderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnReminderTime.setOnClickListener(listener -> {
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Reminder Time AM")
                    .build();
            timePicker.show(getParentFragmentManager(), "atag");
            timePicker.addOnDismissListener(l -> {
                Log.i("hours in picker", String.valueOf(timePicker.getHour()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String localTime = LocalTime.of(timePicker.getHour(), timePicker.getMinute()).toString();
                    Log.i("time in picker", localTime);
                    binding.reminderTimeAmTxt.setText(localTime);
                }
            });


        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddReminderViewModel.class);
        // TODO: Use the ViewModel
    }

}