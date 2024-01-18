package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentAddReminderBinding;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge.TumblingETestScoreFragment;

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

        AtomicInteger hour = new AtomicInteger();
        AtomicInteger minute = new AtomicInteger();

        AtomicReference<String> medicationName = new AtomicReference<>();
        AtomicReference<String> medicationType = new AtomicReference<>();

        AtomicReference<Float> medicationDose = new AtomicReference<>();

        binding.btnReminderTime.setOnClickListener(listener -> {
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTheme(R.style.Theme_MyEyeHealth_TimePicker)
                    .setTitleText("Select Reminder Time AM")
                    .build();
            timePicker.show(getParentFragmentManager(), "atag");
            timePicker.addOnDismissListener(l -> {
                Log.i("hours in picker", String.valueOf(timePicker.getHour()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String localTime = LocalTime.of(timePicker.getHour(), timePicker.getMinute()).toString();

                    //Store time in local variable
                    hour.set(timePicker.getHour());
                    minute.set(timePicker.getMinute());

                    Log.i("time in picker", localTime);

                    binding.reminderTimeAmTxt.setText(localTime);
                }
            });

        });

        binding.btnSubmit.setOnClickListener(listener -> {
            //gather inputs from medication name and type
            medicationName.set(binding.txtReminderName.getEditText().getText().toString());
            medicationType.set(binding.txtReminderType.getEditText().getText().toString());
            medicationDose.set(Float.valueOf(binding.txtReminderDose.getEditText().getText().toString()));

//            MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getContext());
            Reminders reminder = new Reminders();
            reminder.reminderName = medicationName.get();
            reminder.reminderType = medicationType.get();
            reminder.dose = medicationDose.get();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                reminder.time = LocalTime.of(hour.get(), minute.get()).toNanoOfDay();
            }
//            database.remindersDAO().addReminder(reminder);
            AddReminderThread addReminderThread = new AddReminderThread(reminder, getContext());
            Thread thread = new Thread(addReminderThread);
            thread.start();
            NavHostFragment.findNavController(AddReminderFragment.this)
                    .popBackStack(R.id.addReminderFragment, false);

            Navigation.findNavController(AddReminderFragment.this.getView()).navigateUp();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddReminderViewModel.class);
        // TODO: Use the ViewModel
    }

}