package uk.ac.aston.ip.myeyehealth;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.ActivityMainBinding;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.reminders.MedicationLogsRepository;
import uk.ac.aston.ip.myeyehealth.reminders.RemindersActivity;
import uk.ac.aston.ip.myeyehealth.reminders.adapter.ListRemindersAdapter;
import uk.ac.aston.ip.myeyehealth.reminders.adapter.ListRemindersNotTakenAdapter;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageSwitcher;
import android.widget.TextView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //how to create notifications
        // the NotificationChannel class is not in the Support Library.
        {
            CharSequence name = "Show Medication Reminders";
            String description = "You need to take your medications. Press review to continue.";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("MyEyeHealth", name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


//            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
//            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
//            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
//            startActivity(intent);

            Intent openReminders = new Intent(this, RemindersActivity.class);
            openReminders.setAction("VIEW");
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(openReminders);

            PendingIntent pendingRemindersIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel.getId())
                    .setSmallIcon(R.drawable.medication_64)
                    .setContentTitle("Your Medication Reminders")
                    .setContentText(description)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingRemindersIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .addAction(R.drawable.medication_64, "REVIEW", pendingRemindersIntent);

            MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getApplicationContext());
            int pendingMedications = database.remindersDAO().findRemindersTakenToday(LocalDate.now().toEpochDay()).stream().filter(medicationLog -> !medicationLog.isMedicationTaken).toArray().length;
            if(pendingMedications > 0) {
                if (!notificationManager.areNotificationsEnabled()) {
                    requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
                    notificationManager.notify(0, builder.build());
                }

                //TODO: check if there is no notifications

                Instant date = Instant.ofEpochSecond(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                long yesterday = date.minus(Period.ofDays(1)).getEpochSecond();
                List<MedicationLog> medicationLogs = database.remindersDAO().findRemindersTakenTodayNotification(date.getEpochSecond(), yesterday);
                //if there is medication reminders then notify the user.
                Log.d("medicationLog", String.valueOf(medicationLogs.size()));
                Log.d("reminders", "med reminders: " + database.remindersDAO().getAll().size());
                if (medicationLogs.size() < database.remindersDAO().getAll().size()) {
                    notificationManager.notify(0, builder.build());
                }
            }
        }
        int notificationId = 1;
        MedicationLogsRepository medicationLogsRepository = new MedicationLogsRepository(getApplicationContext());
        for(MedicationLog medicationLog : medicationLogsRepository.getPendingRemindersToday()) {
            //get the medication time
            Reminders reminder = MyEyeHealthDatabase.getInstance(getApplicationContext()).remindersDAO().findRemindersById(medicationLog.remindersNo);
            //check if it has not been taken, and the time is five minutes before actual time
            long time = reminder.time;
            long fiveMinTime = LocalTime.ofNanoOfDay(time).minus(Calendar.MINUTE - 5, ChronoUnit.MINUTES).toNanoOfDay();
            if(!medicationLog.isMedicationTaken && LocalTime.now().toNanoOfDay() > fiveMinTime &&
            LocalTime.now().toNanoOfDay() < time) {
                //then create a notification prompt
                CharSequence name = "Send Timely Medication Reminder";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("MyEyeHealth", name, importance);
                channel.setDescription("Send timely medication reminder");
                // Register the channel with the system. You can't change the importance
                // or other notification behaviors after this.
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel.getId())
                        .setSmallIcon(R.drawable.medication_64)
                        .setContentTitle(reminder.reminderName + " \u23F0 " + LocalTime.ofNanoOfDay(reminder.time))
                        .setContentText("You need to take your " + reminder.reminderName)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true);

                notificationManager.notify(notificationId++, builder.build());
            }
        }

        setSupportActionBar(binding.toolbar);
        NavigationView navigationView = binding.navView;

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(binding.navDrawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            item.setChecked(true);
            Log.println(Log.INFO, "Item checked", "item: " + item.getTitle() + " checked: " + item.isChecked());
            if(item.isChecked()) {
                navController.navigate(item.getItemId());
                return true;
            }

            return false;

        });

        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            if(item.isChecked()) {
                Log.i("Item checked: ", String.valueOf(item.getItemId()));
                item.setChecked(false);
                navController.navigate(item.getItemId());
//                if(item.getItemId() ==  R.id.homeFragment || item.getItemId() ==  R.id.visionToolsFragment || item.getItemId() ==  R.id.remindersFragment) {
//                    bottomNavigationView.setSelectedItemId(item.getItemId());
//                }
//                else {
//
//
//                }
                binding.navDrawerLayout.close();
                return true;
            }
            else {
                return false;
            }
        });


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        //this is the toolbar represented as menu and menu item for options
        // in order to access the menu item a listener is used to determine the menu item
        //selected to perform appropriate action.
        binding.toolbar.setOnMenuItemClickListener(item ->
        {
            if(item.getItemId() == R.id.action_settings) {
                navController.navigate(R.id.settingsFragment);
                return true;
            }
            else if(item.getItemId() == R.id.action_abouts) {
                navController.navigate(R.id.aboutFragment);
                return true;
            }
            return false;
        });
        //temporarily hiding
        binding.fab.setVisibility(View.INVISIBLE);


        prepareMedicationRemindersLog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * The following prepares the medication logs for the user and stores them in a local database.
     * @see MyEyeHealthDatabase
     * */
    private void prepareMedicationRemindersLog() {
        MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getApplicationContext());
        MedicationLog medicationLog = new MedicationLog();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        List<MedicationLog> logs = MyEyeHealthDatabase.getInstance(getApplicationContext()).medicationLogsDAO().getMedicationLogs();
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
}