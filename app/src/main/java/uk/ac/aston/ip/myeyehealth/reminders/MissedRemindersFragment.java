package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentMissedRemindersBinding;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.reminders.adapter.ListMissedRemindersAdapter;
import uk.ac.aston.ip.myeyehealth.reminders.adapter.ListRemindersAdapter;
import uk.ac.aston.ip.myeyehealth.views.MissedReminder;

public class MissedRemindersFragment extends Fragment {

    private MissedRemindersViewModel mViewModel;

    private FragmentMissedRemindersBinding binding;

    public static MissedRemindersFragment newInstance() {
        return new MissedRemindersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMissedRemindersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getContext());

        List<Reminders> reminders = database.remindersDAO().getAll();

        List<MedicationLog> remindersTaken = database.medicationLogsDAO().getMedicationLogs();

        List<MissedReminder> tmp = new ArrayList<>();


        HashMap<String, List<Integer>> visitedIndexes = new HashMap<>();

        //go through each medication log, if you cannot find the medication that was taken during that day then add to list
        // Assuming that MedicationLog has a field reminderNo and medicationTimeTaken represents the time the medication was taken
        HashMap<LocalDate, List<Integer>> visitedRemindersMap = new HashMap<>();
        HashMap<LocalDate, List<MedicationLog>> visitedMissedReminders = new HashMap<>();

        for (MedicationLog medicationLog : remindersTaken) {
            if (!medicationLog.isMedicationTaken) {
                Reminders reminder = database.remindersDAO().findRemindersById(medicationLog.remindersNo);
                MissedReminder missedReminder =
                        new MissedReminder(reminder.reminderNo, reminder.reminderName, reminder.reminderType, reminder.dose, medicationLog.medicationTimeTaken, reminder.isRepeated);
                tmp.add(missedReminder);
            }
        }

//        Log.d("missed reminders", "" + tmp.size());
//        for(Map.Entry<LocalDate, List<Integer>> entry : visitedRemindersMap.entrySet()) {
//            for(Reminders reminder : reminders) {
//                if(!entry.getValue().contains(reminder.reminderNo)) {
//
//                    MissedReminder missedReminder =
//                            new MissedReminder(reminder.reminderNo, reminder.reminderName, reminder.reminderType, reminder.dose, visitedMissedReminders.get(entry.getKey()).stream().findFirst().get().medicationTimeTaken, reminder.isRepeated);
//                    tmp.add(missedReminder);
//                }
//            }
//        }

        // Now visitedRemindersMap contains the mapping of reminder dates to lists of reminder numbers


//        for(MedicationLog medicationLog : remindersTaken) {
//            //find the date of the time taken
//            //set that date to midnight, and get the day before
//            //add to list if it does not exist
//            long currentDate = medicationLog.medicationTimeTaken;
//            Instant instant = Instant.ofEpochSecond(currentDate);
//            long dayBeforeCurrentDate = instant.minus(Period.ofDays(1)).getEpochSecond();
//
//            //get the reminders during that day
//
//            //loop through the reminders
//            for(Reminders reminder : reminders) {
//                if(medicationLog.remindersNo == reminder.reminderNo && medicationLog.medicationTimeTaken < currentDate) {
//                    continue;
//                }
//
//                else {
//                    tmp.add(new MissedReminder(reminder.reminderNo, reminder.reminderName, reminder.reminderType, reminder.dose, medicationLog.medicationTimeTaken, currentDate, reminder.isRepeated));
//                }
//            }
//
//        }

        if (tmp.size() < 1) {
            binding.remindersListRecyclerView.setVisibility(View.GONE);
            binding.noRemindersMsg.setVisibility(View.VISIBLE);
        } else {
            RecyclerView recyclerView = view.findViewById(R.id.reminders_list_recycler_view);
            recyclerView.setAdapter(new ListMissedRemindersAdapter(tmp));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MissedRemindersViewModel.class);
        // TODO: Use the ViewModel
    }

}