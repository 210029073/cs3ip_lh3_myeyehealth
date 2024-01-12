package uk.ac.aston.ip.myeyehealth.reminders;

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
                MaterialCardView materialCardView = new MaterialCardView(getContext());

                TextView reminderName = new TextView(getContext());
                TextView reminderType = new TextView(getContext());
                TextView reminderTime = new TextView(getContext());

                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                reminderName.setText(reminder.reminderName);
                reminderType.setText(reminder.reminderType);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    reminderTime.setText(LocalTime.ofNanoOfDay(reminder.time).toString());
                }

                linearLayout.setPadding(25,25,25,25);

                linearLayout.addView(reminderName);
                linearLayout.addView(reminderType);
                linearLayout.addView(reminderTime);

                materialCardView.addView(linearLayout);

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