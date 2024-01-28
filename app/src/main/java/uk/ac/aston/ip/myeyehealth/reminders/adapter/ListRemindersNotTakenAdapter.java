package uk.ac.aston.ip.myeyehealth.reminders.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.views.MissedMedicationViews;

public class ListRemindersNotTakenAdapter extends RecyclerView.Adapter<ListRemindersNotTakenAdapter.RemindersListViewHolder> {

    private List<Reminders> remindersList;

    public static class RemindersListViewHolder extends RecyclerView.ViewHolder {

        private final TextView reminderName;

        private final TextView reminderTime;


        public RemindersListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.reminderName = itemView.findViewById(R.id.txt_reminder_name);
            this.reminderTime = itemView.findViewById(R.id.txt_reminder_time);
        }

        public TextView getReminderName() {
            return this.reminderName;
        }

        public TextView getReminderTime() {
            return reminderTime;
        }
    }

    public ListRemindersNotTakenAdapter(List<Reminders> reminders) {
        this.remindersList = reminders;
    }

    @NonNull
    @Override
    public RemindersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminders_list_items, parent, false);
        return new RemindersListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersListViewHolder holder, int position) {
        holder.getReminderName().setText(remindersList.get(position).reminderName);
        if (remindersList.size() > 0) {
            Log.d("reminderList", "size: " + remindersList.get(0));
            holder.getReminderTime().setText(String.valueOf(LocalTime.ofNanoOfDay(remindersList.get(position).time)));
        }
    }
    @Override
    public int getItemCount() {
        return remindersList.size();
    }
}
