package uk.ac.aston.ip.myeyehealth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceDataStore;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;

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