package uk.ac.aston.ip.myeyehealth;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentHomeBinding;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.reminders.adapter.ListRemindersNotTakenAdapter;
import uk.ac.aston.ip.myeyehealth.views.MissedMedicationViews;

/**
 * {@link HomeFragment} represents the main menu of the application housing the navigation
 * to reminders, missed doses, vision, tools and test results.
 * It also shows reminders that needs to be taken by the user on time.
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentHomeBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        for(int i = 0; i < container.getChildCount(); i++) {
            System.out.println(container.getChildAt(i).getClass().getName());
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            //this is an example of retrieving a global argument from navigation graph
//            //and outputting to logcat.
////            Log.println(Log.INFO, "Name var", (String) requireArguments().get("name"));
//            //this can be used in order to update global arguements in navigation graph
////            requireArguments().putString("name", "Home");
//            item.setChecked(false);
//            if(item.getItemId() == R.id.visionToolsFragment) {
//                Log.println(Log.INFO, "Item checked", "item: " + item.getTitle() + " checked: " + item.isChecked());
//
//                item.setChecked(true);
//                NavHostFragment.findNavController(HomeFragment.this)
//                        .navigate(R.id.action_homeFragment_to_visionToolsFragment);
//                    return true;
//            }
//            if(item.getItemId() == R.id.homeFragment) {
//                Toast.makeText(this.getContext(), "You are currently at home.", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//            return false;
//        });

        //The following below set the navigation to other fragments that are part of the Navigation graph
        //using the NavHostFragment class
        binding.cardVisionTools.setOnClickListener(card -> {
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_visionToolsFragment);
        });

        binding.cardMedicationReminders.setOnClickListener(card -> {
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_remindersFragment);
        });

        binding.cardMissedDoses.setOnClickListener(card -> {
//            Snackbar.make(getView(), "Feature not added yet...", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null)
//                    .show();
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(R.id.action_homeFragment_to_missedRemindersFragment);
        });

        binding.cardViewTestScores.setOnClickListener(card -> {
            NavHostFragment.findNavController(HomeFragment.this)
                            .navigate(R.id.action_homeFragment_to_testScoreFragment);
        });

        //This prepares the medication logs, to retrieve all of todays medications that needs
        //to be taken by the user.
        prepareMedicationRemindersLog();

        //The following below prepares the list of reminders that have not been taken yet by
        //the user for today using the RecyclerView class.
        RecyclerView recyclerView = view.findViewById(R.id.reminders_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getContext());
        List<Reminders> reminders = database.remindersDAO().getAll();

        //This obtains today's and yesterday's date and stores them as long
        Instant date = Instant.ofEpochSecond(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        long yesterday = date.minus(Period.ofDays(1)).getEpochSecond();
        long today = LocalDate.now().toEpochDay();
        //fetches reminders that are taken today
        List<MedicationLog> remindersTaken = database.remindersDAO().findRemindersTakenToday(today);
        ArrayList<Integer> addedReminder = new ArrayList<>();
        List<Reminders> remindersCarousel = new ArrayList<>();

        boolean hasTaken = false;

        //finds the medication reminders that were not taken, and are todays reminders
        for(MedicationLog reminderTaken : remindersTaken) {
            Reminders reminder = database.remindersDAO().findRemindersById(reminderTaken.remindersNo);
//            Log.d("Is Date same for: ", "Reminder taken :" + reminderTaken.remindersNo + "at Reminder: " + reminder.reminderNo);
//            Log.d("Is date same?", String.valueOf(LocalDateTime.ofEpochSecond(reminderTaken.medicationTimeTaken, 0, ZoneOffset.UTC).getDayOfMonth() == LocalDateTime.now().getDayOfMonth()));
            if(!reminderTaken.isMedicationTaken && reminderTaken.medicationTimeTaken == today) {
                addedReminder.add(reminder.reminderNo);
            }
        }


        //this adds reminders to the reminderCarousel object.
        database.remindersDAO().getAll().forEach(reminder -> {

            if(addedReminder.contains(reminder.reminderNo)) {
                //TODO: Use recycler view.
                //TODO: Need to do this via xml and duplicate the element to make it easy to control the dimensions
                remindersCarousel.add(reminder);
            }

        });

        //this gets the noReminderMsg that is shown on the HomeFragment if no reminders are added
        //or if they have all been taken for today
        ImageSwitcher noRemindersMsg = view.findViewById(R.id.msg_no_reminders_carousel);

        //checks if all medications are taken
        //if taken it will show the message
        //otherwise, hide the message.
        if(remindersCarousel.size() > 0) {
            noRemindersMsg.setVisibility(View.INVISIBLE);
        }
        else {
            noRemindersMsg.setVisibility(View.VISIBLE);
        }
        recyclerView.setAdapter(new ListRemindersNotTakenAdapter(remindersCarousel));
        recyclerView.setVisibility(View.VISIBLE);

    }

    /**
     * The following prepares the medication logs for the user and stores them in a local database.
     * @see MyEyeHealthDatabase
     * */
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

            //if the reminder does not exist, then insert it to the medication logs table.
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


            //if there is no logs at all for the following reminder
            //then add to the medication logs table
            else {
                medicationLog.remindersNo = reminder.reminderNo;
                medicationLog.isMedicationTaken = false;
                medicationLog.medicationTimeTaken = todays_date;
                database.medicationLogsDAO().insertMedicationLog(medicationLog);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}