package uk.ac.aston.ip.myeyehealth.reminders.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;

public class ListRemindersAdapter extends RecyclerView.Adapter<ListRemindersAdapter.RemindersListViewHolder> {

    private List<Reminders> remindersList;

    private OnClickListener listener;

    public static class RemindersListViewHolder extends RecyclerView.ViewHolder {
        private final TextView id;
        private final TextView reminderName;

        private final TextView reminderType;

        private final TextView reminderDose;

        private final TextView reminderTimeAMPM;


        public RemindersListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id = itemView.findViewById(R.id.txt_reminder_id);
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

        public TextView getId() {
            return id;
        }
    }

    public ListRemindersAdapter(List<Reminders> reminders) {
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
        holder.getId().setText(String.valueOf(remindersList.get(position).reminderNo));
        holder.getReminderName().setText(remindersList.get(position).reminderName);
        if(remindersList.get(position).reminderType.equalsIgnoreCase("Eye Drops") ||
                remindersList.get(position).reminderType.equalsIgnoreCase("Eye Gel")) {
            holder.getReminderType().setText(remindersList.get(position).reminderType);
            String doseAndUnit = remindersList.get(position).dose+" drops";
            holder.getReminderDose().setText("Dose: " + doseAndUnit);

        }
        else {
            holder.getReminderType().setText(remindersList.get(position).reminderType);
            holder.getReminderDose().setText("Dose: " + String.valueOf(remindersList.get(position).dose));
        }
        holder.getReminderTimeAMPM().setText("Time: " + String.valueOf(LocalTime.ofNanoOfDay(remindersList.get(position).time)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onClick(position, remindersList.get(position));
                    holder.itemView.setFocusable(true);
                    holder.itemView.requestFocus();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return remindersList.size();
    }

    //This interface is needed in order to create the custom onClickListener for the recycler view
    /**
     * An OnClickListener for managing the onclick actions when an recycleview item has been
     * collected from the Recycler view.
     *
     * @see uk.ac.aston.ip.myeyehealth.ShowAllRemindersFragment
     * @see RecyclerView
     * @see OnClickListener
     *
     * @author Ibrahim Ahmad
     * @version 1.0.0*/
    public interface OnClickListener {
        /**
         * This will handle the users onclick action when accessing a reminder item in the ListRemindersAdapter
         *
         * @param model Represents the model of the Reminders Object. This is used to store the object needed for the onclick action
         * @param position Represents the position of the recycle view item is located in the recyclerview
         *
         **/
        void onClick(int position, Reminders model);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}
