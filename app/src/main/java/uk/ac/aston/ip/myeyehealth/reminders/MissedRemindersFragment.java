package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.database.MyEyeHealthDatabase;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentMissedRemindersBinding;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.reminders.adapter.ListRemindersAdapter;

public class MissedRemindersFragment extends Fragment {

    private MissedRemindersViewModel mViewModel;

    private FragmentMissedRemindersBinding binding;

    public static MissedRemindersFragment newInstance() {
        return new MissedRemindersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMissedRemindersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyEyeHealthDatabase database = MyEyeHealthDatabase.getInstance(getContext());

        List<Reminders> reminders = database.remindersDAO().findRemindersThatHaveBeenMissed(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        RecyclerView recyclerView = view.findViewById(R.id.reminders_list_recycler_view);
        recyclerView.setAdapter(new ListRemindersAdapter(reminders));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MissedRemindersViewModel.class);
        // TODO: Use the ViewModel
    }

}