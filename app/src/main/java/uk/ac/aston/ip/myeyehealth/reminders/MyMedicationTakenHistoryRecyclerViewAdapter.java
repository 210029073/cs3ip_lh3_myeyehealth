package uk.ac.aston.ip.myeyehealth.reminders;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.aston.ip.myeyehealth.entities.MedicationLog;
import uk.ac.aston.ip.myeyehealth.entities.Reminders;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentMedicationTakenHistoryBinding;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MedicationLogReminder}.
 */
public class MyMedicationTakenHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyMedicationTakenHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<MedicationLog> mValues;
    private final List<Reminders> mReminderValues;

    private List<MedicationLogReminder> mMedRemindersValue;

    static class MedicationLogReminder {
        private final int medicationLogNo;
        private final int reminderNo;
        private String reminderName;
        private String reminderType;
        private String dateTime;

        public MedicationLogReminder(int medicationLogNo, int reminderNo, String reminderName, String reminderType, String dateTime) {
            this.medicationLogNo = medicationLogNo;
            this.reminderNo = reminderNo;
            this.reminderName = reminderName;
            this.reminderType = reminderType;
            this.dateTime = dateTime;
        }

        public int getMedicationLogNo() {
            return medicationLogNo;
        }

        public int getReminderNo() {
            return reminderNo;
        }

        public String getReminderName() {
            return reminderName;
        }

        public String getReminderType() {
            return reminderType;
        }

        public String getDateTime() {
            return dateTime;
        }
    }

    public MyMedicationTakenHistoryRecyclerViewAdapter(List<MedicationLog> items, List<Reminders> reminders) {
        mValues = items;
        this.mReminderValues = reminders;
        this.mMedRemindersValue = new ArrayList<>();

        for(MedicationLog medicationLog: mValues) {
            for(Reminders reminder : mReminderValues) {
                if(reminder.reminderNo == medicationLog.remindersNo && medicationLog.isMedicationTaken) {

                    String dateTime = LocalDate.ofEpochDay(medicationLog.medicationTimeTaken).format(DateTimeFormatter.ISO_LOCAL_DATE).toString();

                    this.mMedRemindersValue.add(
                            new MedicationLogReminder(medicationLog.medicationLogNo, reminder.reminderNo, reminder.reminderName, reminder.reminderType, dateTime)
                    );
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentMedicationTakenHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mMedRemindersValue.get(position);
        holder.mIdView.setText(String.valueOf(mMedRemindersValue.get(position).dateTime));
        holder.mContentView.setText(holder.mItem.reminderName);
        holder.mReminderType.setText(holder.mItem.reminderType);
    }

    @Override
    public int getItemCount() {
        return mMedRemindersValue.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        private final TextView mReminderType;
        public MedicationLogReminder mItem;

        public ViewHolder(FragmentMedicationTakenHistoryBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            this.mReminderType = binding.txtReminderType;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}