package uk.ac.aston.ip.myeyehealth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import uk.ac.aston.ip.myeyehealth.databinding.ActivityMainBinding;
import uk.ac.aston.ip.myeyehealth.reminders.RemindersActivity;

import android.view.Menu;
import android.view.MenuItem;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyEyeHealth";
            String description = "You need to take your medications. Press review to continue.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
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
                    .setPriority(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingRemindersIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.medication_64, "REVIEW", pendingRemindersIntent);

            notificationManager.notify(0, builder.build());
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
}