package uk.ac.aston.ip.myeyehealth.vision_tools.record_blood_pressure.list_blood_pressure_history;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.aston.ip.myeyehealth.vision_tools.record_blood_pressure.entity.BloodPressure;
import uk.ac.aston.ip.myeyehealth.databinding.FragmentListBloodPressureBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BloodPressure}.
 */
public class BloodPressureHistoryRecyclerViewAdapter extends RecyclerView.Adapter<BloodPressureHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<BloodPressure> mValues;

    public BloodPressureHistoryRecyclerViewAdapter(List<BloodPressure> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentListBloodPressureBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        LocalDateTime isoDateTime = LocalDateTime.parse(mValues.get(position).getIsoDateTime());
        LocalDate date = isoDateTime.toLocalDate();
        LocalTime time = isoDateTime.toLocalTime();
        String timeTakenDateParsed = date.format(DateTimeFormatter.ofPattern("dd MMM YYYY"))
                + "\n" + time.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
        holder.mDateTimeTaken.setText(timeTakenDateParsed);
        holder.mSYSValue.setText(mValues.get(position).getSys() + "/" + mValues.get(position).getDia() + " mmHg");
//        holder.mDIAValue.setText(mValues.get(position).getDia() + " mmHg");
        holder.mBPMValue.setText(mValues.get(position).getBpm() + " bpm");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mDateTimeTaken;
        public final TextView mSYSValue;
        public final TextView mBPMValue;
        public BloodPressure mItem;

        public ViewHolder(FragmentListBloodPressureBinding binding) {
            super(binding.getRoot());
            mDateTimeTaken = binding.txtBloodPressureTimeTaken;
            mSYSValue = binding.txtBloodPressureSys;
            mBPMValue = binding.txtBloodPressureBpm;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateTimeTaken.getText() + "'" + " '" + mSYSValue.getText()  + "'" + " '" + mBPMValue.getText() + "'";
        }
    }
}