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

        //May need to create a re-usable version of this, as DRY code.
        binding.bpmUnit.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             * The following will check whilst the user is typing if they have specified
             * a bpm unit value.
             * If not, it will show an error, otherwise it will be disappeared.
             * */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(binding.bpmUnit.getEditText().getText().toString().isEmpty()) {
                    binding.bpmUnit.setError("Please enter a BPM value greater than zero in bpm.");
                    binding.bpmUnit.setErrorEnabled(true);
                }

                else {
                    binding.bpmUnit.setErrorEnabled(false);
                }
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