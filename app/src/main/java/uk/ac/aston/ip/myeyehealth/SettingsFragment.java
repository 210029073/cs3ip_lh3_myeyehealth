package uk.ac.aston.ip.myeyehealth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDataStore;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.entities.TestRecord;

/**
 * The SettingsFragment is responsible for managing the settings of the application
 * @version 1.3.0
 * @author Ibrahim Ahmad
 * */
public class SettingsFragment extends PreferenceFragmentCompat {
    private RxDataStore<Preferences> preferenceRxDataStore;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        this.preferenceRxDataStore = new RxPreferenceDataStoreBuilder(getContext(), "settings").build();

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
                    .putInt("REMINDER_TIME_PREFERENCE", 0);
        }
        //gets the preference
        ListPreference listPreference = findPreference("NOTIFICATION_REMINDER_TIME");

        //stores the state of the preference i.e minutes
        String preferenceState = listPreference.getValue();

        listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            //stores in preference store
            requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
                    .putInt("REMINDER_TIME_PREFERENCE", Integer.parseInt(preferenceState));
            Toast.makeText(getContext(), "Set notification reminder to be received " + newValue + "min before", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void prepareConfigurationSettingsForClearingData() {
        Preference clearAllReminders = findPreference("CLEAR_ALL_REMINDERS");
        Preference clearAllTestResults = findPreference("CLEAR_ALL_TEST_RESULTS");

        //This will check if there are reminders and test results are present.
        //if they are present it will then enable the options
        //otherwise disable them.
        Thread threadMain = new Thread(() -> {
            List<Reminders> remindersList = MyEyeHealthDatabase.getInstance(getContext()).remindersDAO().getAll();
            List<TestRecord> testRecords = MyEyeHealthDatabase.getInstance(getContext()).testRecordsDAO().getAll();

            //this will get the size
            if(remindersList.size() > 0) {
                clearAllReminders.setEnabled(true);
            }

            else {
                clearAllReminders.setEnabled(false);
            }

            //this will get the size
            if(testRecords.size() > 0) {
                clearAllTestResults.setEnabled(true);
            }

            else {
                clearAllTestResults.setEnabled(false);
            }
        });

        threadMain.setName("CheckRemindersTestResults");
        threadMain.start();

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
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        SwitchPreferenceCompat switchPreferenceCompat = findPreference("IS_NOTIFICATIONS_ENABLED");

        findPreference("VIEW_ABOUT").setOnPreferenceClickListener(preference -> {
            NavHostFragment.findNavController(SettingsFragment.this)
                    .navigate(R.id.aboutFragment);
            return true;
        });
        if(!getPreferenceManager().getSharedPreferences().contains("IS_NOTIFICATIONS_ENABLED")) {
            if (notificationManager.areNotificationsEnabled()) {
                requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putBoolean("IS_NOTIFICATIONS_ENABLED", true).commit();
                switchPreferenceCompat.setChecked(((HashMap<String, Boolean>) requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).getAll()).get("IS_NOTIFICATIONS_ENABLED"));
            }
            else {
                requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putBoolean("IS_NOTIFICATIONS_ENABLED", false).commit();
                switchPreferenceCompat.setChecked(((HashMap<String, Boolean>) requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).getAll()).get("IS_NOTIFICATIONS_ENABLED"));
            }
        }

        switchPreferenceCompat.setVisible(true);
        findPreference("NOTIFICATION_REMINDER_TIME").setVisible(true);
        findPreference("NOTIFICATION_REMINDER_TIME");

        switchPreferenceCompat.setChecked(
                getPreferenceManager().getSharedPreferences().getBoolean("IS_NOTIFICATIONS_ENABLED", false)
        );
        switchPreferenceCompat.setOnPreferenceClickListener(preference -> {
            if(switchPreferenceCompat.isChecked()) {
                this.preferenceRxDataStore.updateDataAsync(preferences -> {
                    MutablePreferences mutablePreferences = preferences.toMutablePreferences();
                    Preferences.Key<Boolean> isEnabledKey = PreferencesKeys.booleanKey("IS_NOTIFICATIONS_ENABLED");
                    mutablePreferences.set(isEnabledKey, true);
                    return Single.just(mutablePreferences.toPreferences());
                });
                this.preferenceRxDataStore.updateDataAsync(preferences -> {
                    MutablePreferences mutablePreferences = preferences.toMutablePreferences();
                    mutablePreferences.set(new Preferences.Key<>("IS_NOTIFICATIONS_ENABLED"), true);
                    return Single.just(mutablePreferences.toPreferences());
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationManager.createNotificationChannel(new NotificationChannel("ReminderAlarmReciever", "Send reminders when app is running", NotificationManager.IMPORTANCE_HIGH));
                    requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putBoolean("IS_NOTIFICATIONS_ENABLED", true).commit();
                    switchPreferenceCompat.setChecked(true);
                }
                return true;
            }
            else {

                this.preferenceRxDataStore.updateDataAsync(preferences -> {
                    MutablePreferences mutablePreferences = preferences.toMutablePreferences();
                    Preferences.Key<Boolean> isEnabledKey = PreferencesKeys.booleanKey("IS_NOTIFICATIONS_ENABLED");
                    mutablePreferences.set(isEnabledKey, false);
                    return Single.just(mutablePreferences.toPreferences());
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationManager.deleteNotificationChannel("ReminderAlarmReciever");
                    requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putBoolean("IS_NOTIFICATIONS_ENABLED", false).commit();
                    switchPreferenceCompat.setChecked(false);
                }
                return false;
            }
        });
    }
}