package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.LocalTime;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.database.MyHealthDatabaseThread;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentManageRemindersBinding;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

public class ManageRemindersFragment extends Fragment {

    private ManageRemindersViewModel mViewModel;

    private FragmentManageRemindersBinding binding;

    public static ManageRemindersFragment newInstance() {
        return new ManageRemindersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentManageRemindersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onPrimaryNavigationFragmentChanged(boolean isPrimaryNavigationFragment) {
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment);
    }

    @Override
    public void onResume() {
        super.onResume();

        ManageRemindersViewModel manageRemindersViewModel = new ViewModelProvider(requireActivity())
                .get(ManageRemindersViewModel.class);

        Reminders reminders = MyEyeHealthDatabase.getInstance(getContext())
                .remindersDAO().findRemindersById(manageRemindersViewModel.reminderNo.getValue());

        manageRemindersViewModel.getReminderName().setValue(reminders.reminderName);
        manageRemindersViewModel.getReminderDose().setValue(reminders.dose);
        manageRemindersViewModel.getReminderType().setValue(reminders.reminderType);
        manageRemindersViewModel.getReminderTime().setValue(reminders.time);
        manageRemindersViewModel.getIsRepeated().setValue(reminders.isRepeated);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ManageRemindersViewModel manageRemindersViewModel = new ViewModelProvider(requireActivity())
                .get(ManageRemindersViewModel.class);

        //set text inputs
        String reminderTime = "";
        String reminderName = manageRemindersViewModel.reminderName.getValue();
        String reminderType = manageRemindersViewModel.reminderType.getValue();
        int reminderNo = manageRemindersViewModel.reminderNo.getValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            reminderTime = String.valueOf(LocalTime.ofNanoOfDay(manageRemindersViewModel.reminderTime.getValue()));
        }
        float reminderDose = manageRemindersViewModel.reminderDose.getValue();
        boolean isReminderRepeated = manageRemindersViewModel.isRepeated.getValue();

        binding.txtReminderName.getEditText().setText(reminderName);
        binding.txtReminderType.getEditText().setText(reminderType);
        binding.txtReminderDose.getEditText().setText(String.valueOf(reminderDose));
        binding.reminderTimeAmTxt.setText(reminderTime);
        binding.checkboxIsRepeat.setChecked(isReminderRepeated);

        binding.btnUpdate.setOnClickListener(listener -> {
            Reminders reminder = MyEyeHealthDatabase.getInstance(getContext())
                    .remindersDAO().findRemindersById(reminderNo);
            UpdateRemindersViewModel updateRemindersViewModel = new ViewModelProvider(requireActivity())
                    .get(UpdateRemindersViewModel.class);
            updateRemindersViewModel.setReminderName(reminder.reminderName);
            updateRemindersViewModel.setReminderType(reminder.reminderType);
            updateRemindersViewModel.setDose(reminder.dose);
            updateRemindersViewModel.setTime(reminder.time);

            updateRemindersViewModel.setReminderId(reminderNo);
            updateRemindersViewModel.setRepeated(reminder.isRepeated);

            NavHostFragment.findNavController(ManageRemindersFragment.this)
                    .navigate(R.id.action_manageRemindersFragment_to_updateReminders);
        });

        binding.btnDelete.setOnClickListener(listener -> {

            Reminders reminder = MyHealthDatabaseThread.getInstance(getContext()).remindersDAO().findRemindersById(manageRemindersViewModel.getReminderNo().getValue());
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                    .setMessage("Are you sure you want to delete " + reminder.reminderName + "?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Thread thread = new Thread(() -> {
                        MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getContext());
                        database.remindersDAO().deleteReminder(reminder);
                    });

                    thread.start();

//                    NavHostFragment.findNavController(ManageRemindersFragment.this)
//                            .navigateUp();
//                    Navigation.findNavController(getView())
//                            .navigate(R.id.showAllRemindersFragment);
                    //return back to reminders fragment
                    NavHostFragment.findNavController(ManageRemindersFragment.this)
                            .popBackStack(R.id.manageRemindersFragment, false);

                    NavHostFragment.findNavController(ManageRemindersFragment.this).navigateUp();

                    Snackbar.make(getView(), "Successfully Deleted " + reminder.reminderName, Snackbar.LENGTH_SHORT).show();
                }
            });

            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialog.show();
        });
    }

    private void setDeleteAction(Reminders reminder, int id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to delete " + reminder.reminderName + "?");

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getContext());
                database.remindersDAO().deleteReminder(reminder);

                Snackbar.make(getView(), "Successfully Deleted " + reminder.reminderName, Snackbar.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ManageRemindersFragment.this)
                        .navigateUp();
                Navigation.findNavController(getView())
                        .navigate(R.id.showAllRemindersFragment);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ManageRemindersViewModel.class);
        // TODO: Use the ViewModel
    }

}