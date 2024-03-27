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

/**
 * UpdateReminder is a {@link Fragment} that allows the user to update their reminder.
 * @author Ibrahim Ahmad
 * @version 1.2.0
 * */
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

        //retrieve medication reminders and bind them to the TextView, CheckBox objects
        //from mViewModel
        binding.txtReminderName.getEditText().setText(mViewModel.getReminderName());
        binding.checkboxIsRepeat.setChecked(mViewModel.isRepeated());
        binding.txtReminderType.getEditText().setText(mViewModel.getReminderType());
        binding.txtReminderDose.getEditText().setText(String.valueOf(mViewModel.getDose()));

        //parse the time to LocalTime
        LocalTime reminderTime = LocalTime.ofNanoOfDay(mViewModel.getTime());
        String time = reminderTime.toString(); //format to string
        binding.reminderTimeAmTxt.setText(time); //set time in string format to TextView object

        //creates the timepicker object
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H) //Set to twelve hour format
                .setHour(reminderTime.getHour()) //set hour from LocalTime object
                .setMinute(reminderTime.getMinute()) //set minute from LocalTime object
                .setTheme(R.style.Theme_MyEyeHealth_TimePicker) //apply theme
                .setTitleText("Select Reminder Time AM") //set title
                .build(); //build the MaterialTimePicker object

        //Create Two thread safe Atomic-based integer variables to store hour and minutes
        //this ensures thread-safe operations
        AtomicInteger hour = new AtomicInteger(reminderTime.getHour());
        AtomicInteger minute = new AtomicInteger(reminderTime.getMinute());

        //OnClickListener for setting the time
        binding.btnReminderTime.setOnClickListener(v -> {
            materialTimePicker.show(getParentFragmentManager(), "atag");
            materialTimePicker.addOnDismissListener(dialog -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String localTime = LocalTime.of(materialTimePicker.getHour(), materialTimePicker.getMinute()).toString();

                    //Store time in local variable
                    hour.set(materialTimePicker.getHour());
                    minute.set(materialTimePicker.getMinute());

//                    Log.i("time in picker", localTime);

                    binding.reminderTimeAmTxt.setText(localTime);
                }
            });
        });

        //stores the reminder no in an atomic-based integer variable
        AtomicInteger reminderNo = new AtomicInteger(mViewModel.getReminderId());

        //OnClickListener for updating reminder
        binding.btnUpdate.setOnClickListener(v -> {

            Reminders reminders = new Reminders();

            //Creates a thread for updating the reminders
            Thread thread = new Thread(() -> {
                MyEyeHealthDatabase myEyeHealthDatabase = MyEyeHealthDatabase.getInstance(getContext());

                reminders.reminderNo = reminderNo.get();
                reminders.reminderName = binding.txtReminderName.getEditText().getText().toString();
                reminders.reminderType = binding.txtReminderType.getEditText().getText().toString();
                reminders.isRepeated = binding.checkboxIsRepeat.isChecked();
                reminders.dose = Float.valueOf(binding.txtReminderDose.getEditText().getText().toString());
                reminders.time = LocalTime.of(hour.get(), minute.get()).toNanoOfDay();

                //update entry on database
                myEyeHealthDatabase.remindersDAO().updateReminder(reminders);
            });

            thread.start(); //start the thread

            //this will update the reminder details in ManageReminders fragment
            ManageRemindersViewModel manageRemindersViewModel = new ViewModelProvider(requireActivity())
                    .get(ManageRemindersViewModel.class);
            manageRemindersViewModel.reminderName.setValue(binding.txtReminderName.getEditText().getText().toString());
            manageRemindersViewModel.reminderDose.setValue(Float.valueOf(binding.txtReminderDose.getEditText().getText().toString()));
            manageRemindersViewModel.reminderType.setValue(binding.txtReminderType.getEditText().getText().toString());
            manageRemindersViewModel.reminderTime.setValue(LocalTime.of(hour.get(), minute.get()).toNanoOfDay());
            manageRemindersViewModel.isRepeated.setValue(binding.checkboxIsRepeat.isChecked());

            //return back to the manage reminders fragment
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