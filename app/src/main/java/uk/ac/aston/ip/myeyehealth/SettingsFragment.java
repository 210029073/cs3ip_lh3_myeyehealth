package uk.ac.aston.ip.myeyehealth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.entities.TestRecord;

/**
 * The SettingsFragment is responsible for managing the settings of the application
 * @version 1.3.0
 * @author Ibrahim Ahmad
 * */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toggleTimelyNotificationsSetting();
        prepareConfigurationSettingsForClearingData();
        setPreferenceForReceivingNotifications();
    }

    private void setPreferenceForReceivingNotifications() {
        //if there is no preference already stored then set it to defaults, which is receiving on time
        if(!requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).contains("REMINDER_TIME_PREFERENCE")) {
            requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
                    .putInt("REMINDER_TIME_PREFERENCE", 0).apply();
        }
        //gets the preference
        ListPreference listPreference = findPreference("NOTIFICATION_REMINDER_TIME");

        //stores the state of the preference i.e minutes
        if(listPreference != null) {
            listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                //stores in preference store
                requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
                        .putInt("REMINDER_TIME_PREFERENCE", Integer.parseInt(newValue.toString())).apply();
//                System.out.println(requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
//                        .getInt("REMINDER_TIME_PREFERENCE", 0));
                Toast.makeText(getContext(), "Set notification reminder to be received " + newValue + "min before", Toast.LENGTH_SHORT).show();
                return true;
            });
        }
    }

    private void prepareConfigurationSettingsForClearingData() {
        Preference clearAllReminders = findPreference("CLEAR_ALL_REMINDERS");
        Preference clearAllTestResults = findPreference("CLEAR_ALL_TEST_RESULTS");

        //This will check if there are reminders and test results are present.
        //if they are present it will then enable the options
        //otherwise disable them.
//        Thread threadMain = new Thread(() -> {
            List<Reminders> remindersList1 = MyEyeHealthDatabase.getInstance(requireActivity().getApplicationContext()).remindersDAO().getAll();
            List<TestRecord> testRecords1 = MyEyeHealthDatabase.getInstance(requireActivity().getApplicationContext()).testRecordsDAO().getAll();

            //this will get the size
            if(remindersList1.size() > 0) {
                clearAllReminders.setEnabled(true);
            }

            else {
                clearAllReminders.setEnabled(false);
            }

            //this will get the size
            if(testRecords1.size() > 0) {
                clearAllTestResults.setEnabled(true);
            }

            else {
                clearAllTestResults.setEnabled(false);
            }
//        });


        clearAllReminders.setOnPreferenceClickListener(preference -> {
            Toast.makeText(getContext(), "The user has clicked clear all reminders", Toast.LENGTH_SHORT).show();

            List<Reminders> remindersList = MyEyeHealthDatabase.getInstance(getContext()).remindersDAO().getAll();

            //this will get the size
            if(remindersList.size() > 0) {
                Thread thread = new Thread(() -> {
                    MyEyeHealthDatabase.getInstance(getContext()).remindersDAO().truncateReminders();
                });

                thread.setName("SynchroniseDBOperation");
                thread.start();
                clearAllReminders.setEnabled(false);
                Toast.makeText(getContext(), "Successfully cleared all reminders.", Toast.LENGTH_SHORT).show();
                return true;
            }

            else {
                return false;
            }

        });

        clearAllTestResults.setOnPreferenceClickListener(preference -> {
            Toast.makeText(getContext(), "The user has clicked clear all test results", Toast.LENGTH_SHORT).show();

            List<TestRecord> testRecords;

            testRecords = MyEyeHealthDatabase.getInstance(getContext()).testRecordsDAO().getAll();

            if(testRecords.size() > 0) {
                Thread thread = new Thread(() -> {
                    MyEyeHealthDatabase.getInstance(getContext()).testRecordsDAO().truncateTestRecords();
                });

                thread.start();

                clearAllTestResults.setEnabled(false);
                return true;
            }

            else {
                return false;
            }
        });

    }


    private void toggleTimelyNotificationsSetting() {
        Preference manageNotificationPreference = findPreference("IS_NOTIFICATIONS_ENABLED");

        findPreference("VIEW_ABOUT").setOnPreferenceClickListener(preference -> {
            NavHostFragment.findNavController(SettingsFragment.this)
                    .navigate(R.id.aboutFragment);
            return true;
        });

        findPreference("NOTIFICATION_REMINDER_TIME").setVisible(true);
        findPreference("NOTIFICATION_REMINDER_TIME");

        manageNotificationPreference.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getContext().getPackageName());
            getContext().startActivity(intent);
            return true;
        });
    }
}