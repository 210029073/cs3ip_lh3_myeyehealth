package uk.ac.aston.ip.myeyehealth;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.ActivityMainBinding;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.reminders.MedicationLogsRepository;
import uk.ac.aston.ip.myeyehealth.reminders.RemindersActivity;
import uk.ac.aston.ip.myeyehealth.reminders.alarms.ReminderAlarmReciever;
import uk.ac.aston.ip.myeyehealth.reminders.alarms.ReminderAlarmScheduler;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getPreferences(0).contains("RUN_FIRST_TIME_APP")) {
            getPreferences(0).edit().putBoolean("RUN_FIRST_TIME_APP", true).commit();
        }

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
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("Your Medication Reminders")
                    .setContentText(description)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingRemindersIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .addAction(R.drawable.ic_stat_name, "REVIEW", pendingRemindersIntent);

            if (!notificationManager.areNotificationsEnabled() && getPreferences(0).getBoolean("RUN_FIRST_TIME_APP", true)) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
                getPreferences(0).edit().putBoolean("RUN_FIRST_TIME_APP", false).commit();
            }
            MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getApplicationContext());
            int pendingMedications = database.remindersDAO().findRemindersTakenToday(LocalDate.now().toEpochDay()).stream().filter(medicationLog -> !medicationLog.isMedicationTaken).toArray().length;
            if(pendingMedications > 0) {
                if (!notificationManager.areNotificationsEnabled()) {
                    requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
                    getPreferences(0).edit().putBoolean("RUN_FIRST_TIME_APP", false).commit();
                    notificationManager.notify(0, builder.build());
                }

                //TODO: check if there is no notifications

                Instant date = Instant.ofEpochSecond(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                long yesterday = date.minus(Period.ofDays(1)).getEpochSecond();
                List<MedicationLog> medicationLogs = database.remindersDAO().findRemindersTakenTodayNotification(date.getEpochSecond(), yesterday);
                //if there is medication reminders then notify the user.
                if (medicationLogs.size() < database.remindersDAO().getAll().size() && notificationManager.getNotificationChannel("MyEyeHealth").getImportance() != NotificationManager.IMPORTANCE_NONE) {
                    notificationManager.notify(0, builder.build());
                }
            }
        }
        int notificationId = 1;
        if(getApplicationContext().getSystemService(NotificationManager.class).getNotificationChannel("ReminderAlarmReciever") != null) {
            if (getApplicationContext().getSystemService(NotificationManager.class).getNotificationChannel("ReminderAlarmReciever").getImportance() != NotificationManager.IMPORTANCE_NONE) {
//            if (((boolean) getSharedPreferences("settings", Context.MODE_PRIVATE).getAll().get("IS_NOTIFICATIONS_ENABLED"))) {
                MedicationLogsRepository medicationLogsRepository = new MedicationLogsRepository(getApplicationContext());
                for (MedicationLog medicationLog : medicationLogsRepository.getPendingRemindersToday()) {
                    //get the medication time
                    Reminders reminder = MyEyeHealthDatabase.getInstance(getApplicationContext()).remindersDAO().findRemindersById(medicationLog.remindersNo);
                    //check if it has not been taken, and the time is five minutes before actual time
                    long time = reminder.time;
                    long fiveMinTime = LocalTime.ofNanoOfDay(time).minus(5, ChronoUnit.MINUTES).toNanoOfDay();
                    if (!medicationLog.isMedicationTaken && LocalTime.now().toNanoOfDay() > fiveMinTime &&
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
                                .setSmallIcon(R.drawable.ic_stat_name)
                                .setContentTitle(reminder.reminderName + " \u23F0 " + LocalTime.ofNanoOfDay(reminder.time))
                                .setContentText("You need to take your " + reminder.reminderName)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true)
                                .setOnlyAlertOnce(true);

                        notificationManager.notify(notificationId++, builder.build());
                    }
                }
//            }
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
            if(item.isChecked()) {
                navController.navigate(item.getItemId());
                return true;
            }

            return false;

        });

        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            if(item.isChecked()) {
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
//        System.out.println(getPreferences(0).getAll().keySet());

        if(getApplicationContext().getSystemService(NotificationManager.class).getNotificationChannel("ReminderAlarmReciever") == null) {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NotificationManager.class);
            NotificationChannel channel = new NotificationChannel("ReminderAlarmReciever", "Send reminders when app is running", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

        }

        if (getApplicationContext().getSystemService(NotificationManager.class).getNotificationChannel("ReminderAlarmReciever").getImportance() != NotificationManager.IMPORTANCE_NONE) {
//            if ((boolean) getSharedPreferences("settings", Context.MODE_PRIVATE).getAll().get("IS_NOTIFICATIONS_ENABLED")) {
            setAlarms();
//            }
        }


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

    private void setAlarms() {
        List<Reminders> reminders = new MedicationLogsRepository(getApplicationContext()).getRemindersNotTakenTodayInReminders();
        for(Reminders reminder : reminders) {
                AlarmManager alarmManager = getApplicationContext().getSystemService(AlarmManager.class);

                Intent intent = new Intent(getApplicationContext(), ReminderAlarmReciever.class);
                getApplicationContext().sendBroadcast(intent);
                intent.putExtra("REMINDER_NAME", reminder.reminderName);
                intent.putExtra("REMINDER_ID", reminder.reminderNo);
                intent.putExtra("REMINDER_TIME", LocalTime.ofNanoOfDay(reminder.time).format(DateTimeFormatter.ofPattern("HH:mm")));
                LocalTime localTime = LocalTime.ofNanoOfDay(reminder.time);
                LocalDateTime dateTimeNow = LocalDateTime.now();
                LocalDateTime localDateTime = LocalDateTime.of(dateTimeNow.getYear(), dateTimeNow.getMonthValue(), dateTimeNow.getDayOfMonth(), localTime.getHour(), localTime.getMinute());
//                System.out.println(localDateTime);
                Calendar calendar = Calendar.getInstance();
                calendar.set(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(),
                        localDateTime.getHour(), localDateTime.getMinute());
                if(getSharedPreferences("settings", Context.MODE_PRIVATE)
                        .contains("REMINDER_TIME_PREFERENCE")) {
                    int receiveNotificationTimeEarlyInMinutes = getSharedPreferences("settings", Context.MODE_PRIVATE)
                            .contains("REMINDER_TIME_PREFERENCE") ? (int) getSharedPreferences("settings", Context.MODE_PRIVATE).getAll().get("REMINDER_TIME_PREFERENCE") : 0;
                    localDateTime = localDateTime.minusMinutes(receiveNotificationTimeEarlyInMinutes);
                }

                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                        PendingIntent.getBroadcast(
                                getApplicationContext(),
                                reminder.reminderNo,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        )

                );

        }
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
            ReminderAlarmScheduler scheduler = new ReminderAlarmScheduler(getApplicationContext());


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