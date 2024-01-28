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

    private List<MissedMedicationViews> remindersList;

    public static class RemindersListViewHolder extends RecyclerView.ViewHolder {

        private final TextView reminderName;

        private final TextView reminderType;

        private final TextView reminderDose;

        private final TextView reminderTimeAMPM;


        public RemindersListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.reminderName = itemView.findViewById(R.id.txt_reminder_name);
            this.reminderType = itemView.findViewById(R.id.txt_reminder_type);
            this.reminderDose = itemView.findViewById(R.id.txt_reminder_dose);
            this.reminderTimeAMPM = itemView.findViewById(R.id.reminder_time_taken);
        }

        public TextView getReminderName() {
            return this.reminderName;
        }

        public TextView getReminderType() {
            return reminderType;
        }

        public TextView getReminderDose() {
            return reminderDose;
        }

        public TextView getReminderTimeAMPM() {
            return reminderTimeAMPM;
        }
    }

    public ListRemindersNotTakenAdapter(List<MissedMedicationViews> reminders) {
        this.remindersList = reminders;
    }

    @NonNull
    @Override
    public RemindersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);
        return new RemindersListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersListViewHolder holder, int position) {
        holder.getReminderName().setText(remindersList.get(position).reminders.reminderName);
        if (remindersList.size() > 0) {
            Log.d("reminderList", "size: " + remindersList.get(0).reminders);
            if (remindersList.get(position).reminders.reminderType.equals("Eye Drops")) {
                holder.getReminderType().setText(remindersList.get(position).reminders.reminderType);
                String doseAndUnit = remindersList.get(position).reminders.dose + " drops";
                holder.getReminderDose().setText(doseAndUnit);
//
            }
            holder.getReminderType().setText(remindersList.get(position).reminders.reminderType);
            holder.getReminderDose().setText(String.valueOf(remindersList.get(position).reminders.dose));
            holder.getReminderTimeAMPM().setText(String.valueOf(LocalTime.ofNanoOfDay(remindersList.get(position).reminders.time)));

        }
    }
    @Override
    public int getItemCount() {
        return remindersList.size();
    }
}
