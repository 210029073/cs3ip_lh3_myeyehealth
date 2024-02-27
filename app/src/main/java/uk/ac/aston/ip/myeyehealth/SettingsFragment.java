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
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
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
        this.preferenceRxDataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
//            ((Map<String, Boolean>) mutablePreferences.set("IS_NOTIFICATIONS_ENABLED", ));
            return Single.just(mutablePreferences.toPreferences());
        });
        if(!getPreferenceManager().getSharedPreferences().contains("IS_NOTIFICATIONS_ENABLED")) {
            if (notificationManager.areNotificationsEnabled()) {
                getPreferenceManager().getSharedPreferences().edit().putBoolean("IS_NOTIFICATIONS_ENABLED", true).commit();
                switchPreferenceCompat.setChecked(((HashMap<String, Boolean>) getPreferenceManager().getSharedPreferences().getAll()).get("IS_NOTIFICATIONS_ENABLED"));
            }
            else {
                getPreferenceManager().getSharedPreferences().edit().putBoolean("IS_NOTIFICATIONS_ENABLED", false).commit();
                switchPreferenceCompat.setChecked(((HashMap<String, Boolean>) getPreferenceManager().getSharedPreferences().getAll()).get("IS_NOTIFICATIONS_ENABLED"));
            }
        }

        switchPreferenceCompat.setVisible(true);
        switchPreferenceCompat.setChecked(
                getPreferenceManager().getSharedPreferences().getBoolean("IS_NOTIFICATIONS_ENABLED", false)
        );
        switchPreferenceCompat.setOnPreferenceClickListener(preference -> {
            if(switchPreferenceCompat.isChecked()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationManager.createNotificationChannel(new NotificationChannel("ReminderAlarmReciever", "Send reminders when app is running", NotificationManager.IMPORTANCE_HIGH));
                    getPreferenceManager().getSharedPreferences().edit().putBoolean("IS_NOTIFICATIONS_ENABLED", true).commit();
                    switchPreferenceCompat.setChecked(true);
                }
                return true;
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationManager.deleteNotificationChannel("ReminderAlarmReciever");
                    getPreferenceManager().getSharedPreferences().edit().putBoolean("IS_NOTIFICATIONS_ENABLED", false).commit();
                    switchPreferenceCompat.setChecked(false);
                }
                return false;
            }
        });
    }
}