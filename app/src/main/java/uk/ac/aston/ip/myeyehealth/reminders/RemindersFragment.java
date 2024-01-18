package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.annotation.IdRes;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalTime;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentRemindersBinding;
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

        if(reminders.size() > 0) {
            binding.tmpPlaceholderMsg.setVisibility(View.INVISIBLE);
            for(Reminders reminder : reminders) {
                //TODO: Use recycler view.
                //TODO: Need to do this via xml and duplicate the element to make it easy to control the dimensions
                MaterialCardView materialCardView = new MaterialCardView(getContext());

                TextView reminderName = new TextView(getContext());
                TextView reminderType = new TextView(getContext());
                TextView reminderDose = new TextView(getContext());
                TextView reminderTime = new TextView(getContext());

                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                reminderName.setText(reminder.reminderName);
                reminderName.setTextSize(16);
                reminderName.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Headline);
                reminderDose.setText("Dose: " + Float.toString(reminder.dose));
                reminderDose.setTextSize(16);
                reminderType.setText(reminder.reminderType);
                reminderType.setTextSize(16);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    reminderTime.setText("Time to take: " + LocalTime.ofNanoOfDay(reminder.time).toString());
                    reminderTime.setTextSize(16);
                }

                linearLayout.setPadding(30,25,30,25);
                linearLayout.addView(reminderName);
                linearLayout.addView(reminderType);
                linearLayout.addView(reminderDose);
                linearLayout.addView(reminderTime);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(20,15,20,15);

                materialCardView.addView(linearLayout);
                materialCardView.setLayoutParams(params);

                materialCardView.setOnClickListener(listener -> {
                    ReminderTrackerViewModel viewModel = new ViewModelProvider(requireActivity()).get(ReminderTrackerViewModel.class);
                    viewModel.reminderName.setValue(reminder.reminderName);
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

                binding.listReminders.addView(materialCardView);
            }
        }
        else {
            binding.tmpPlaceholderMsg.setVisibility(View.VISIBLE);
        }


        binding.addReminderActionBtn.setOnClickListener(click -> {
            NavHostFragment.findNavController(RemindersFragment.this)
                    .navigate(R.id.action_remindersFragment_to_addReminderFragment);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RemindersViewModel.class);
        // TODO: Use the ViewModel
    }

}