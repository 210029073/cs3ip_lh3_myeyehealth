package uk.ac.aston.ip.myeyehealth.vision_tools.record_blood_pressure;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.health.connect.datatypes.BloodPressureRecord;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentRecordBloodPressureBinding;
import uk.ac.aston.ip.myeyehealth.entities.Health;
import uk.ac.aston.ip.myeyehealth.textwatcher.TextErrorChangeListener;
import uk.ac.aston.ip.myeyehealth.vision_tools.record_blood_pressure.entity.BloodPressure;

/**
 * Fragment designed for handling the user's input when recording their
 * blood pressure
 *
 * @version 1.1.0
 * @author Ibrahim Ahmad*/
public class RecordBloodPressureFragment extends Fragment {

    private RecordBloodPressureViewModel mViewModel;

    private FragmentRecordBloodPressureBinding binding;

    public static RecordBloodPressureFragment newInstance() {
        return new RecordBloodPressureFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentRecordBloodPressureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MutableLiveData<Integer> dia = new MutableLiveData<>(0);
        MutableLiveData<Integer> sys = new MutableLiveData<>(0);
        MutableLiveData<Integer> bpm = new MutableLiveData<>(0);

        binding.bpmUnit.getEditText().addTextChangedListener(new TextErrorChangeListener(binding.bpmUnit, "Please enter a BPM value greater than zero in bpm.") {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.diaUnit.getEditText().addTextChangedListener(new TextErrorChangeListener(binding.diaUnit, "Please enter a DIA value greater than zero in mmHg.") {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.sysUnit.getEditText().addTextChangedListener(new TextErrorChangeListener(binding.sysUnit, "Please enter a SYS value greater than zero in mmHg.") {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.btnSubmit.setOnClickListener(button -> {

            if(binding.diaUnit.getEditText().getText().toString().isEmpty()
            ) {
                binding.diaUnit.setError("Please enter a DIA value greater than zero in mmHg.");
                binding.diaUnit.setErrorEnabled(true);
            }

            else if(binding.sysUnit.getEditText().getText().toString().isEmpty()) {
                binding.sysUnit.setError("Please enter a SYS value greater than zero in mmHg.");
                binding.sysUnit.setErrorEnabled(true);
            }

            else if(binding.bpmUnit.getEditText().getText().toString().isEmpty()) {
                binding.bpmUnit.setError("Please enter a BPM value greater than zero in bpm.");
                binding.bpmUnit.setErrorEnabled(true);
            }

            else {
                //Store the health logs in an object
                Health health = new Health();
                health.healthType = "Blood Pressure";

                //set the sys, dia, and bpm value.
                sys.setValue(Integer.parseInt(binding.sysUnit.getEditText().getText().toString()));
                dia.setValue(Integer.parseInt(binding.diaUnit.getEditText().getText().toString()));
                bpm.setValue(Integer.parseInt(binding.bpmUnit.getEditText().getText().toString()));

                health.healthData = new BloodPressure(sys.getValue(), dia.getValue())
                        .setBpm(bpm.getValue())
                        .build()
                        .toString();

                //Store health logs in database
                Thread thread = new Thread(() -> {
                    MyEyeHealthDatabase.getInstance(getContext())
                            .healthDAO().insertHealth(health);

                    //may need to remove this line
                    List<Health> health1 = MyEyeHealthDatabase.getInstance(getContext())
                            .healthDAO().getMedicationLogs();
                });

                thread.start();

                NavHostFragment.findNavController(RecordBloodPressureFragment.this)
                        .navigateUp();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RecordBloodPressureViewModel.class);
        // TODO: Use the ViewModel
    }

}