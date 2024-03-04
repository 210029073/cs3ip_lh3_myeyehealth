package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentUpdateRemindersBinding;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

//TODO: Need to configure the update fragment classes navigation, and then layout properties
public class UpdateReminders extends Fragment {

    private UpdateRemindersViewModel mViewModel;

    private FragmentUpdateRemindersBinding binding;

    public static UpdateReminders newInstance() {
        return new UpdateReminders();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateRemindersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(UpdateRemindersViewModel.class);

        binding.txtReminderName.getEditText().setText(mViewModel.getReminderName());
        binding.checkboxIsRepeat.setChecked(mViewModel.isRepeated());
        binding.txtReminderType.getEditText().setText(mViewModel.getReminderType());
        binding.txtReminderDose.getEditText().setText(String.valueOf(mViewModel.getDose()));
        LocalTime reminderTime = LocalTime.ofNanoOfDay(mViewModel.getTime());
        String time = reminderTime.toString();
        binding.reminderTimeAmTxt.setText(time);

        //creates the timepicker object
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(reminderTime.getHour())
                .setMinute(reminderTime.getMinute())
                .setTheme(R.style.Theme_MyEyeHealth_TimePicker)
                .setTitleText("Select Reminder Time AM")
                .build();

        AtomicInteger hour = new AtomicInteger(reminderTime.getHour());
        AtomicInteger minute = new AtomicInteger(reminderTime.getMinute());

        binding.btnReminderTime.setOnClickListener(v -> {
            materialTimePicker.show(getParentFragmentManager(), "atag");
            materialTimePicker.addOnDismissListener(dialog -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String localTime = LocalTime.of(materialTimePicker.getHour(), materialTimePicker.getMinute()).toString();

                    //Store time in local variable
                    hour.set(materialTimePicker.getHour());
                    minute.set(materialTimePicker.getMinute());

                    Log.i("time in picker", localTime);

                    binding.reminderTimeAmTxt.setText(localTime);
                }
            });
        });

        AtomicInteger reminderNo = new AtomicInteger(mViewModel.getReminderId());


        binding.btnUpdate.setOnClickListener(v -> {


            Thread thread = new Thread(() -> {
                MyEyeHealthDatabase myEyeHealthDatabase = MyEyeHealthDatabase.getInstance(getContext());

                Reminders reminders = new Reminders();
                reminders.reminderNo = reminderNo.get();
                reminders.reminderName = binding.txtReminderName.getEditText().getText().toString();
                reminders.reminderType = binding.txtReminderType.getEditText().getText().toString();
                reminders.isRepeated = binding.checkboxIsRepeat.isChecked();
                reminders.dose = Float.valueOf(binding.txtReminderDose.getEditText().getText().toString());
                reminders.time = LocalTime.of(hour.get(), minute.get()).toNanoOfDay();
                myEyeHealthDatabase.remindersDAO().updateReminder(reminders);

            });

            thread.start();

            NavHostFragment.findNavController(UpdateReminders.this)
                            .navigateUp();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateRemindersViewModel.class);
        // TODO: Use the ViewModel
    }

}