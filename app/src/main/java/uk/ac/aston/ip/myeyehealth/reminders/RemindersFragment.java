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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

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

        //TODO: Create the local database using android room
        MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getContext());
        List<Reminders> reminders = database.remindersDAO().getAll();
        Instant date = Instant.ofEpochSecond(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        long yesterday = date.minus(Period.ofDays(1)).getEpochSecond();
        long today = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        List<MedicationLog> remindersTaken = database.remindersDAO().findRemindersTakenToday(today, yesterday);
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
                    if(reminderTaken.remindersNo == reminder.reminderNo && reminderTaken.medicationTimeTaken < today && reminderTaken.medicationTimeTaken > yesterday) {
                        addedReminder.add(reminder.reminderNo);
                    }
                }

                binding.listRemindersAll.addView(generateReminderCardsForDailyMedication(reminder));
                if(!addedReminder.contains(reminder.reminderNo)) {
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
        return materialCardView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RemindersViewModel.class);
        // TODO: Use the ViewModel
    }

}