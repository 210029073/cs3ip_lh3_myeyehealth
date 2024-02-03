package uk.ac.aston.ip.myeyehealth.vision_tools.tumblinge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import uk.ac.aston.ip.myeyehealth.R;
import uk.ac.aston.ip.myeyehealth.entities.TestRecord;
import uk.ac.aston.ip.myeyehealth.reminders.adapter.ListMissedRemindersAdapter;

public class TestRecordsAdapter extends RecyclerView.Adapter<TestRecordsAdapter.TestRecordsViewHolder> {

    private List<TestRecord> testRecords;

    @NonNull
    @Override
    public TestRecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_record_items, parent, false);
        return new TestRecordsAdapter.TestRecordsViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull TestRecordsViewHolder holder, int position) {

        holder.getTestName().setText(testRecords.get(position).testResultDescription);

        Gson gson = new Gson();

        if(testRecords.get(position).testResultDescription.equals("Tumbling E Test")) {
            TumblingETestViewModel.TumblingETestScore scoreObj = gson.fromJson(testRecords.get(position).testResultScore, TumblingETestViewModel.TumblingETestScore.class);
            holder.getScore().setText("Left Eye: " + Float.valueOf(scoreObj.getLeftEyeScore()).intValue() + "%\n\n" +"Right Eye: " +  Float.valueOf(scoreObj.getRightEyeScore()).intValue() + "%");
        }
        else {
            holder.getScore().setText(testRecords.get(position).testResultScore);
        }

        long time = testRecords.get(position).testResultRecordTime;
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
        holder.getTestRecordedTime().setText(dateTime.toLocalDate().toString() + "\n" + dateTime.toLocalTime());
    }

    @Override
    public int getItemCount() {
        return testRecords.size();
    }

    static class TestRecordsViewHolder extends RecyclerView.ViewHolder {

        private final TextView testRecordedTime;

        private final TextView score;

        private final TextView testName;


        public TestRecordsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.testName = itemView.findViewById(R.id.txt_test_name);
            this.testRecordedTime = itemView.findViewById(R.id.txt_time_recorded);
            this.score = itemView.findViewById(R.id.txt_score);
        }

        public TextView getScore() {
            return score;
        }

        public TextView getTestRecordedTime() {
            return testRecordedTime;
        }

        public TextView getTestName() {
            return testName;
        }
    }

    public TestRecordsAdapter(List<TestRecord> testRecords) {
        this.testRecords = testRecords;
    }
}
