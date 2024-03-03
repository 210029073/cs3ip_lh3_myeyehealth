package uk.ac.aston.ip.myeyehealth.vision_tools.color_blindness_test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import uk.ac.aston.ip.myeyehealth.databinding.ActivityColorBlindTestBinding;

import uk.ac.aston.ip.myeyehealth.R;

public class ColorBlindTestActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityColorBlindTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityColorBlindTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_color_blind_test);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        binding.fab.setVisibility(View.GONE);

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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_color_blind_test);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}