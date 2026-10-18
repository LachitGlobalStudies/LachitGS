package com.adnan.lgstudies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.Activity.AllResultsActivity;
import com.adnan.lgstudies.R;

import java.util.ArrayList;

public class ProgressTestSeriesAdapter extends RecyclerView.Adapter<ProgressTestSeriesAdapter.TestSeriesViewHolder> {
    Context context;
    ArrayList<ProgressQuestionSetData>datalist;

    public ProgressTestSeriesAdapter(Context context, ArrayList<ProgressQuestionSetData> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public ProgressTestSeriesAdapter.TestSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.test_series_item_view,parent,false);

        return new TestSeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressTestSeriesAdapter.TestSeriesViewHolder holder, int position) {

        ProgressQuestionSetData progressQuestionSetData=datalist.get(position);
            holder.lock_icon.setImageResource(R.drawable.progress_results);
            holder.test_icon.setVisibility(View.INVISIBLE);
            holder.test_series_title.setText(progressQuestionSetData.getSet_name());
            holder.total_attempt.setText(progressQuestionSetData.getTotal_attempt()+" Attempts Completed");
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, AllResultsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("test_series",progressQuestionSetData.getSet_name());
                    intent.putExtra("batch_name",progressQuestionSetData.getBatch());
                    context.startActivity(intent);

                }
            });



    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class TestSeriesViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView lock_icon,test_icon;
        TextView test_series_title,total_attempt;
        public TestSeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.test_series_card);
            lock_icon=itemView.findViewById(R.id.test_series_lock);
            test_series_title=itemView.findViewById(R.id.test_series_title);
            total_attempt=itemView.findViewById(R.id.attempt);
            test_icon=itemView.findViewById(R.id.test_series_icon);

        }
    }
}
