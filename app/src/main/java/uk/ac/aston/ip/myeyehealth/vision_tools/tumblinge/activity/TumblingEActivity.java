package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.databinding.ActivityTumblingEactivityBinding;
import uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge.TumblingETestFragment;

public class TumblingEActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private ActivityTumblingEactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTumblingEactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tumbling_e);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        //this is the toolbar represented as menu and menu item for options
        // in order to access the menu item a listener is used to determine the menu item
        //selected to perform appropriate action.
//        binding.toolbar.setOnMenuItemClickListener(item ->
//        {
//            if (item.getItemId() == R.id.action_settings) {
//                navController.navigate(R.id.settingsFragment);
//                return true;
//            } else if (item.getItemId() == R.id.action_abouts) {
//                navController.navigate(R.id.aboutFragment);
//                return true;
//            }
//            return false;
//        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setMessage("Are you want to end the game?");

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Snackbar.make(getContext(), getView(), "You said yes", Snackbar.LENGTH_SHORT)
//                        .show();

//                NavHostFragment.findNavController(TumblingETestFragment.this)
//                        .popBackStack(R.id.visionToolsFragment, false);
                finish();
//                Toolbar toolbar = findViewById(R.id.toolbar);
//                toolbar.setVisibility(View.VISIBLE);

//                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
//                bottomNavigationView.setVisibility(View.VISIBLE);

            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //this will make changes to the back press system button by creating an OnBackPressedCallback
        //to create a custom back pressed callback function
        //see more: https://developer.android.com/guide/navigation/custom-back
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                dialog.create();
                dialog.show();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setMessage("Are you want to end the game?");

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Snackbar.make(getContext(), getView(), "You said yes", Snackbar.LENGTH_SHORT)
//                        .show();

//                NavHostFragment.findNavController(TumblingETestFragment.this)
//                        .popBackStack(R.id.visionToolsFragment, false);
                finish();
//                Toolbar toolbar = findViewById(R.id.toolbar);
//                toolbar.setVisibility(View.VISIBLE);

//                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
//                bottomNavigationView.setVisibility(View.VISIBLE);

            }
        });

//        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//        });

    }
}