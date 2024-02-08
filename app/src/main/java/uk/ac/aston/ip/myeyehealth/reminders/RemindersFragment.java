package uk.ac.aston.ip.myeyehealth.reminders;

import android.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentRemindersBinding;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

public class RemindersFragment extends Fragment {

    private RemindersViewModel mViewModel;

    private FragmentRemindersBinding binding;

    public static RemindersFragment newInstance() {
        return new RemindersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRemindersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareMedicationRemindersLog();
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(@NonNull View v) {

            }

            @Override
            public void onViewDetachedFromWindow(@NonNull View v) {
                hideReminderOptionsFromAppBar();
            }
        });

        //TODO: Create the local database using android room
        MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getContext());
        List<Reminders> reminders = database.remindersDAO().getAll();
        Instant date = Instant.ofEpochSecond(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        long yesterday = date.minus(Period.ofDays(1)).getEpochSecond();
        long today = LocalDate.now().toEpochDay();
        List<MedicationLog> remindersTaken = database.remindersDAO().findRemindersTakenToday(today);
        Log.i("remindersTaken", "onViewCreated: " + remindersTaken.size());

        if(reminders.size() > 0) {
            binding.tmpPlaceholderMsg.setVisibility(View.INVISIBLE);
            binding.tmpPlaceholderMsg1.setVisibility(View.INVISIBLE);
            //keep track of the list
            ArrayList<Integer> addedReminder = new ArrayList<>();
            for(Reminders reminder : reminders) {
                boolean hasTaken = false;
                for(MedicationLog reminderTaken : remindersTaken) {
                    Log.d("Is Date same for: ", "Reminder taken :" + reminderTaken.remindersNo + "at Reminder: " + reminder.reminderNo);
                    Log.d("Is date same?", String.valueOf(LocalDateTime.ofEpochSecond(reminderTaken.medicationTimeTaken, 0, ZoneOffset.UTC).getDayOfMonth() == LocalDateTime.now().getDayOfMonth()));
                    if(!reminderTaken.isMedicationTaken && reminderTaken.remindersNo == reminder.reminderNo) {
                        addedReminder.add(reminder.reminderNo);
                    }
                }

                binding.listRemindersAll.addView(generateReminderCardsForDailyMedication(reminder));
                if(addedReminder.contains(reminder.reminderNo)) {
                    //TODO: Use recycler view.
                    //TODO: Need to do this via xml and duplicate the element to make it easy to control the dimensions
                    binding.tmpPlaceholderMsg.setVisibility(View.INVISIBLE);
                    MaterialCardView materialCardView = generateReminderCardsForDailyMedicationIntake(reminder);

                    binding.listReminders.addView(materialCardView);
                }

                else {
                    binding.tmpPlaceholderMsg.setVisibility(View.VISIBLE);
                    binding.tmpPlaceholderMsg.setText("You have checked all your medications.\nYour Done for the Day");
                }
            }
        }
        else {
            binding.tmpPlaceholderMsg.setVisibility(View.VISIBLE);
            binding.tmpPlaceholderMsg1.setVisibility(View.VISIBLE);
            binding.tmpPlaceholderMsg.setText("You currently have no medication.\nAdd a medication reminder to see them here...");
        }


        binding.addReminderActionBtn.setOnClickListener(click -> {
            NavHostFragment.findNavController(RemindersFragment.this)
                    .navigate(R.id.action_remindersFragment_to_addReminderFragment);
        });
    }

    @NonNull
    private MaterialCardView generateReminderCardsForDailyMedicationIntake(Reminders reminder) {
        MaterialCardView materialCardView = new MaterialCardView(getContext());

        TextView reminderName = new TextView(getContext());
        TextView reminderType = new TextView(getContext());
        TextView reminderDose = new TextView(getContext());
        TextView reminderTime = new TextView(getContext());

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        reminderName.setText(reminder.reminderName);
        reminderName.setTextSize(16);
//                reminderName.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
        reminderDose.setText("Dose: " + Float.toString(reminder.dose));
        reminderDose.setTextSize(16);
        reminderType.setText(reminder.reminderType);
        reminderType.setTextSize(16);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            reminderTime.setText("Time to take: " + LocalTime.ofNanoOfDay(reminder.time).toString());
            reminderTime.setTextSize(16);
        }

        linearLayout.setPadding(30, 25, 30, 50);
        linearLayout.addView(reminderName);
        linearLayout.addView(reminderType);
        linearLayout.addView(reminderDose);
        linearLayout.addView(reminderTime);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 15, 20, 15);

        materialCardView.addView(linearLayout);
        materialCardView.setLayoutParams(params);


        materialCardView.setOnClickListener(listener -> {
            ReminderTrackerViewModel viewModel = new ViewModelProvider(requireActivity()).get(ReminderTrackerViewModel.class);
            viewModel.reminderName.setValue(reminder.reminderName);
            viewModel.reminderNo.setValue(reminder.reminderNo);
            viewModel.reminderTime.setValue(reminder.time);
            viewModel.reminderType.setValue(reminder.reminderType);
            viewModel.reminderDose.setValue(reminder.dose);
            viewModel.isRepeated.setValue(reminder.isRepeated);

            //TODO: NEED TO NAVIGATE TO THE UPDATE REMINDER FRAGMENT

            Snackbar.make(getView(), reminderName.getText(), Snackbar.LENGTH_SHORT)
                    .show();

            NavHostFragment.findNavController(RemindersFragment.this)
                    .navigate(R.id.action_remindersFragment_to_reminderTrackerFragment);
        });

        materialCardView.setOnLongClickListener(v -> {
            MaterialToolbar toolbar = getActivity().findViewById(R.id.toolbar);
            binding.remindersContainer.setOnTouchListener((v1, event) -> {
                toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_update).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_abouts).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_settings).setVisible(true);

                materialCardView.setOnClickListener(reset -> {
                    ReminderTrackerViewModel viewModel = new ViewModelProvider(requireActivity()).get(ReminderTrackerViewModel.class);
                    viewModel.reminderName.setValue(reminder.reminderName);
                    viewModel.reminderNo.setValue(reminder.reminderNo);
                    viewModel.reminderTime.setValue(reminder.time);
                    viewModel.reminderType.setValue(reminder.reminderType);
                    viewModel.reminderDose.setValue(reminder.dose);
                    viewModel.isRepeated.setValue(reminder.isRepeated);

                    //TODO: NEED TO NAVIGATE TO THE UPDATE REMINDER FRAGMENT

                    Snackbar.make(getView(), reminderName.getText(), Snackbar.LENGTH_SHORT)
                            .show();

                    NavHostFragment.findNavController(RemindersFragment.this)
                            .navigate(R.id.action_remindersFragment_to_reminderTrackerFragment);
                });
                return true;
            });

            materialCardView.setOnClickListener(listener -> {
                toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_update).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(false);

                toolbar.getMenu().findItem(R.id.action_abouts).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_settings).setVisible(true);

                materialCardView.setOnClickListener(reset -> {
                    ReminderTrackerViewModel viewModel = new ViewModelProvider(requireActivity()).get(ReminderTrackerViewModel.class);
                    viewModel.reminderName.setValue(reminder.reminderName);
                    viewModel.reminderNo.setValue(reminder.reminderNo);
                    viewModel.reminderTime.setValue(reminder.time);
                    viewModel.reminderType.setValue(reminder.reminderType);
                    viewModel.reminderDose.setValue(reminder.dose);
                    viewModel.isRepeated.setValue(reminder.isRepeated);

                    //TODO: NEED TO NAVIGATE TO THE UPDATE REMINDER FRAGMENT

                    Snackbar.make(getView(), reminderName.getText(), Snackbar.LENGTH_SHORT)
                            .show();

                    NavHostFragment.findNavController(RemindersFragment.this)
                            .navigate(R.id.action_remindersFragment_to_reminderTrackerFragment);
                });
            });

            toolbar.getMenu().findItem(R.id.action_delete).setVisible(true);
            setDeleteAction(toolbar.getMenu().findItem(R.id.action_delete), reminder, reminder.reminderNo);
            toolbar.getMenu().findItem(R.id.action_update).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_cancel).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_cancel).setOnMenuItemClickListener(item -> {
                toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_update).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(false);

                toolbar.getMenu().findItem(R.id.action_abouts).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_settings).setVisible(true);
                materialCardView.setOnClickListener(listener -> {
                    ReminderTrackerViewModel viewModel = new ViewModelProvider(requireActivity()).get(ReminderTrackerViewModel.class);
                    viewModel.reminderName.setValue(reminder.reminderName);
                    viewModel.reminderNo.setValue(reminder.reminderNo);
                    viewModel.reminderTime.setValue(reminder.time);
                    viewModel.reminderType.setValue(reminder.reminderType);
                    viewModel.reminderDose.setValue(reminder.dose);
                    viewModel.isRepeated.setValue(reminder.isRepeated);

                    //TODO: NEED TO NAVIGATE TO THE UPDATE REMINDER FRAGMENT

                    Snackbar.make(getView(), reminderName.getText(), Snackbar.LENGTH_SHORT)
                            .show();

                    NavHostFragment.findNavController(RemindersFragment.this)
                            .navigate(R.id.action_remindersFragment_to_reminderTrackerFragment);
                });
                return true;
            });
            toolbar.getMenu().findItem(R.id.action_abouts).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_settings).setVisible(false);
            return true;
        });

        return materialCardView;
    }

    @NonNull
    private MaterialCardView generateReminderCardsForDailyMedication(Reminders reminder) {
        MaterialCardView materialCardView = new MaterialCardView(getContext());

        TextView reminderName = new TextView(getContext());
        TextView reminderType = new TextView(getContext());
        TextView reminderDose = new TextView(getContext());
        TextView reminderTime = new TextView(getContext());

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        reminderName.setText(reminder.reminderName);
        reminderName.setTextSize(16);
//                reminderName.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
        reminderDose.setText("Dose: " + Float.toString(reminder.dose));
        reminderDose.setTextSize(16);
        reminderType.setText(reminder.reminderType);
        reminderType.setTextSize(16);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            reminderTime.setText("Time to take: " + LocalTime.ofNanoOfDay(reminder.time).toString());
            reminderTime.setTextSize(16);
        }

        linearLayout.setPadding(30, 25, 30, 25);
        linearLayout.addView(reminderName);
        linearLayout.addView(reminderType);
        linearLayout.addView(reminderDose);
        linearLayout.addView(reminderTime);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 15, 20, 15);

        materialCardView.addView(linearLayout);
        materialCardView.setLayoutParams(params);

        //TODO: Show Editing Fragment
        materialCardView.setOnClickListener(listener -> {
            ReminderTrackerViewModel viewModel = new ViewModelProvider(requireActivity()).get(ReminderTrackerViewModel.class);
            viewModel.reminderName.setValue(reminder.reminderName);
            viewModel.reminderNo.setValue(reminder.reminderNo);
            viewModel.reminderTime.setValue(reminder.time);
            viewModel.reminderType.setValue(reminder.reminderType);
            viewModel.reminderDose.setValue(reminder.dose);
            viewModel.isRepeated.setValue(reminder.isRepeated);

            //TODO: NEED TO NAVIGATE TO THE UPDATE REMINDER FRAGMENT

            Snackbar.make(getView(), reminderName.getText(), Snackbar.LENGTH_SHORT)
                    .show();

            NavHostFragment.findNavController(RemindersFragment.this)
                    .navigate(R.id.action_remindersFragment_to_reminderTrackerFragment);
        });

        materialCardView.setOnLongClickListener(v -> {
            MaterialToolbar toolbar = getActivity().findViewById(R.id.toolbar);
            binding.remindersContainer.setOnTouchListener((v1, event) -> {
                toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_update).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_abouts).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_settings).setVisible(true);

                materialCardView.setOnClickListener(reset -> {
                    ReminderTrackerViewModel viewModel = new ViewModelProvider(requireActivity()).get(ReminderTrackerViewModel.class);
                    viewModel.reminderName.setValue(reminder.reminderName);
                    viewModel.reminderNo.setValue(reminder.reminderNo);
                    viewModel.reminderTime.setValue(reminder.time);
                    viewModel.reminderType.setValue(reminder.reminderType);
                    viewModel.reminderDose.setValue(reminder.dose);
                    viewModel.isRepeated.setValue(reminder.isRepeated);

                    //TODO: NEED TO NAVIGATE TO THE UPDATE REMINDER FRAGMENT

                    Snackbar.make(getView(), reminderName.getText(), Snackbar.LENGTH_SHORT)
                            .show();

                    NavHostFragment.findNavController(RemindersFragment.this)
                            .navigate(R.id.action_remindersFragment_to_reminderTrackerFragment);
                });
                return true;
            });

            materialCardView.setOnClickListener(listener -> {
                toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_update).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(false);

                toolbar.getMenu().findItem(R.id.action_abouts).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_settings).setVisible(true);

                materialCardView.setOnClickListener(reset -> {
                    ReminderTrackerViewModel viewModel = new ViewModelProvider(requireActivity()).get(ReminderTrackerViewModel.class);
                    viewModel.reminderName.setValue(reminder.reminderName);
                    viewModel.reminderNo.setValue(reminder.reminderNo);
                    viewModel.reminderTime.setValue(reminder.time);
                    viewModel.reminderType.setValue(reminder.reminderType);
                    viewModel.reminderDose.setValue(reminder.dose);
                    viewModel.isRepeated.setValue(reminder.isRepeated);

                    //TODO: NEED TO NAVIGATE TO THE UPDATE REMINDER FRAGMENT

                    Snackbar.make(getView(), reminderName.getText(), Snackbar.LENGTH_SHORT)
                            .show();

                    NavHostFragment.findNavController(RemindersFragment.this)
                            .navigate(R.id.action_remindersFragment_to_reminderTrackerFragment);
                });
            });

            toolbar.getMenu().findItem(R.id.action_delete).setVisible(true);
            setDeleteAction(toolbar.getMenu().findItem(R.id.action_delete), reminder, reminder.reminderNo);
            toolbar.getMenu().findItem(R.id.action_update).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_cancel).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_cancel).setOnMenuItemClickListener(item -> {
                toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_update).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_cancel).setVisible(false);

                toolbar.getMenu().findItem(R.id.action_abouts).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_settings).setVisible(true);
                materialCardView.setOnClickListener(listener -> {
                    ReminderTrackerViewModel viewModel = new ViewModelProvider(requireActivity()).get(ReminderTrackerViewModel.class);
                    viewModel.reminderName.setValue(reminder.reminderName);
                    viewModel.reminderNo.setValue(reminder.reminderNo);
                    viewModel.reminderTime.setValue(reminder.time);
                    viewModel.reminderType.setValue(reminder.reminderType);
                    viewModel.reminderDose.setValue(reminder.dose);
                    viewModel.isRepeated.setValue(reminder.isRepeated);

                    //TODO: NEED TO NAVIGATE TO THE UPDATE REMINDER FRAGMENT

                    Snackbar.make(getView(), reminderName.getText(), Snackbar.LENGTH_SHORT)
                            .show();

                    NavHostFragment.findNavController(RemindersFragment.this)
                            .navigate(R.id.action_remindersFragment_to_reminderTrackerFragment);
                });
                return true;
            });
            toolbar.getMenu().findItem(R.id.action_abouts).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_settings).setVisible(false);
            return true;
        });
        return materialCardView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RemindersViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideReminderOptionsFromAppBar();
    }

    @Override
    public void setExitTransition(@Nullable Object transition) {
        super.setExitTransition(transition);
        hideReminderOptionsFromAppBar();
    }

    private void hideReminderOptionsFromAppBar() {
        MaterialToolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_update).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_cancel).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_abouts).setVisible(true);
        toolbar.getMenu().findItem(R.id.action_settings).setVisible(true);
    }

    private void setDeleteAction(MenuItem appToolBarItem, Reminders reminder, int id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to delete " + reminder.reminderName + "?");

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getContext());
                database.remindersDAO().deleteReminder(reminder);

                Snackbar.make(getView(), "Successfully Deleted " + reminder.reminderName, Snackbar.LENGTH_SHORT).show();
                hideReminderOptionsFromAppBar();
                NavHostFragment.findNavController(RemindersFragment.this)
                        .navigateUp();
                Navigation.findNavController(getView())
                        .navigate(R.id.remindersFragment);
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        appToolBarItem.setOnMenuItemClickListener(item -> {
            dialog.create();
            dialog.show();
            return true;
        });
    }

    private void prepareMedicationRemindersLog() {
        MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getContext());
        MedicationLog medicationLog = new MedicationLog();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        List<MedicationLog> logs = MyEyeHealthDatabase.getInstance(getContext()).medicationLogsDAO().getMedicationLogs();
        LocalDate localDate = LocalDate.now();
        long todays_date = localDate.toEpochDay();
        long yesterday = Instant.ofEpochMilli(todays_date).minus(Period.ofDays(1)).getEpochSecond();

        List<Integer> reminderIds = database.medicationLogsDAO().findRemindersNotTakenToday(todays_date);
//        database.medicationLogsDAO().getMedicationLogs().forEach(medicationLog1 -> {
//            reminderIds.add(medicationLog1.remindersNo);
//        });

        for(Reminders reminder : database.remindersDAO().getAll()) {
            int size = database.medicationLogsDAO().getMedicationLogs().size();
            if(!reminderIds.contains(reminder.reminderNo)) {
                medicationLog.remindersNo = reminder.reminderNo;
                medicationLog.isMedicationTaken = false;
                medicationLog.medicationTimeTaken = todays_date;
                database.medicationLogsDAO().insertMedicationLog(medicationLog);
            }

            if (database.medicationLogsDAO().getMedicationLogs().size() > 0) {

                List<Long> dates = new ArrayList<>();

                database.medicationLogsDAO().getMedicationLogs().forEach(medicationLog1 -> dates.add(medicationLog1.medicationTimeTaken));

                for(MedicationLog medicationLog1 : database.medicationLogsDAO().getMedicationLogs()) {


                    if (medicationLog1.remindersNo == reminder.reminderNo) {

                    }

                    else if(dates.contains(todays_date)) {

                    } else {
                        medicationLog.remindersNo = reminder.reminderNo;
                        medicationLog.isMedicationTaken = false;
                        medicationLog.medicationTimeTaken = todays_date;
                        database.medicationLogsDAO().insertMedicationLog(medicationLog);
                    }
                }
            }


            else {
                medicationLog.remindersNo = reminder.reminderNo;
                medicationLog.isMedicationTaken = false;
                medicationLog.medicationTimeTaken = todays_date;
                database.medicationLogsDAO().insertMedicationLog(medicationLog);
            }
        }
    }
}