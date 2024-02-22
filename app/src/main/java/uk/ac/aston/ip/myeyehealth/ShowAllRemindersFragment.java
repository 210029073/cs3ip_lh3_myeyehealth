package uk.ac.aston.ip.myeyehealth;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentShowAllRemindersBinding;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.reminders.ManageRemindersViewModel;
import uk.ac.aston.ip.myeyehealth.reminders.adapter.ListRemindersAdapter;

public class ShowAllRemindersFragment extends Fragment {

    private ShowAllRemindersViewModel mViewModel;

    private FragmentShowAllRemindersBinding binding;

    public static ShowAllRemindersFragment newInstance() {
        return new ShowAllRemindersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentShowAllRemindersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Reminders> remindersList = MyEyeHealthDatabase.getInstance(getContext()).remindersDAO().getAll();
        ListRemindersAdapter listRemindersAdapter = new ListRemindersAdapter(remindersList);
        binding.remindersListRecyclerView.setAdapter(listRemindersAdapter);
        binding.remindersListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listRemindersAdapter.setOnClickListener(new ListRemindersAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Reminders model) {
                //get Reminder
                Reminders reminder = MyEyeHealthDatabase.getInstance(getContext())
                        .remindersDAO().findRemindersById(model.reminderNo);
                ManageRemindersViewModel manageRemindersViewModel = new ViewModelProvider(requireActivity())
                        .get(ManageRemindersViewModel.class);
                manageRemindersViewModel.getReminderNo().setValue(reminder.reminderNo);
                manageRemindersViewModel.getReminderName().setValue(reminder.reminderName);
                manageRemindersViewModel.getReminderType().setValue(reminder.reminderType);
                manageRemindersViewModel.getReminderDose().setValue(reminder.dose);
                manageRemindersViewModel.getReminderTime().setValue(reminder.time);
                manageRemindersViewModel.getIsRepeated().setValue(reminder.isRepeated);
                NavHostFragment.findNavController(ShowAllRemindersFragment.this)
                                .navigate(R.id.action_showAllRemindersFragment_to_manageRemindersFragment);

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ShowAllRemindersViewModel.class);
        // TODO: Use the ViewModel
    }

}