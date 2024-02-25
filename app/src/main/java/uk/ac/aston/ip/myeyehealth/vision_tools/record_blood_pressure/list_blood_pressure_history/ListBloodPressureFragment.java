package uk.ac.aston.ip.myeyehealth.vision_tools.record_blood_pressure.list_blood_pressure_history;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.vision_tools.record_blood_pressure.entity.BloodPressure;

/**
 * A fragment representing the blood pressure history
 * @author Ibrahim Ahmad
 * @version 1.0.0
 */
public class ListBloodPressureFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListBloodPressureFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ListBloodPressureFragment newInstance(int columnCount) {
        ListBloodPressureFragment fragment = new ListBloodPressureFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_blood_pressure_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            Gson gson = new Gson();
            List<BloodPressure> bloodPressureList = new ArrayList<>();
                    MyEyeHealthDatabase.getInstance(getContext())
                            .healthDAO().getMedicationLogs().forEach(health -> {
                                BloodPressure bloodPressure = gson.fromJson(health.healthData, BloodPressure.class);
                                bloodPressureList.add(bloodPressure);
                            });
            recyclerView.setAdapter(new BloodPressureHistoryRecyclerViewAdapter(bloodPressureList));
        }
        return view;
    }
}