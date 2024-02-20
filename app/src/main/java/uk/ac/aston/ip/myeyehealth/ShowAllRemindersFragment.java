package uk.ac.aston.ip.myeyehealth;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentShowAllRemindersBinding;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.reminders.ManageRemindersViewModel;
import uk.ac.aston.ip.myeyehealth.reminders.ReminderTrackerViewModel;
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

        binding.remindersListRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                LinearLayout view = (LinearLayout) rv.findChildViewUnder(e.getX(), e.getY());

                try {
                    view.setClickable(true);
                    view.setFocusable(true);
                    view.setHapticFeedbackEnabled(true);
                    view.setFocusableInTouchMode(true);
                    view.setDefaultFocusHighlightEnabled(true);
                    view.setFocusedByDefault(true);
                    MaterialTextView medicationId = (MaterialTextView) view.getChildAt(0);
                    MaterialTextView medicationName = (MaterialTextView) view.getChildAt(1);
                    MaterialTextView medicationType = (MaterialTextView) view.getChildAt(2);
                    MaterialTextView medicationDose = (MaterialTextView) view.getChildAt(3);
                    MaterialTextView medicationTime = (MaterialTextView) view.getChildAt(4);
                    Snackbar.make(getView(), medicationName.getText(), Snackbar.LENGTH_SHORT)
                            .show();
                    Snackbar.make(getView(), medicationType.getText(), Snackbar.LENGTH_SHORT).show();
                    Snackbar.make(getView(), medicationDose.getText(), Snackbar.LENGTH_SHORT).show();
                    Snackbar.make(getView(), medicationTime.getText(), Snackbar.LENGTH_SHORT).show();

                    //get Reminder
                    Reminders reminder = MyEyeHealthDatabase.getInstance(getContext())
                            .remindersDAO().findRemindersById(Integer.parseInt(medicationId.getText().toString()));
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

                catch (NullPointerException nullPointerException) {

                }

                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

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