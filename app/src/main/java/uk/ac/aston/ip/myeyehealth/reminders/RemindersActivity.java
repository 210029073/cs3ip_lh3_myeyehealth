package uk.ac.aston.ip.myeyehealth.reminders;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.View;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.databinding.ActivityRemindersBinding;

public class RemindersActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;

    private ActivityRemindersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRemindersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_reminders);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        //this is the toolbar represented as menu and menu item for options
        // in order to access the menu item a listener is used to determine the menu item
        //selected to perform appropriate action.
        binding.toolbar.setOnMenuItemClickListener(item ->
        {
            if (item.getItemId() == R.id.action_settings) {
                navController.navigate(R.id.settingsFragment);
                return true;
            } else if (item.getItemId() == R.id.action_abouts) {
                navController.navigate(R.id.aboutFragment);
                return true;
            }
            return false;
        });
        //temporarily hiding
        binding.fab.setVisibility(View.INVISIBLE);
    }
}