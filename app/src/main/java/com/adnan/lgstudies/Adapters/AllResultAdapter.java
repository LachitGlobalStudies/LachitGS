package com.adnan.lgstudies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AllResultAdapter extends RecyclerView.Adapter<AllResultAdapter.MyviewHolder> {
    Context context;
ArrayList<ProgressReport>list;

    public AllResultAdapter(Context context, ArrayList<ProgressReport> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AllResultAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.results_item_view,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllResultAdapter.MyviewHolder holder, int position) {
        ProgressReport progressReport=list.get(position);
        holder.name.setText(progressReport.getName());
        holder.rank.setText(progressReport.getRank()+" out of "+progressReport.getTotal_user());
        holder.marks_obtained.setText(""+progressReport.getMarks_obtained());
        holder.test_series.setText(progressReport.getTest_series());
        holder.batch.setText(progressReport.getBatch());
        Glide.with(context).load(progressReport.getPimage()).into(holder.profile);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView name,batch,test_series,marks_obtained,rank;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user_name);
            batch=itemView.findViewById(R.id.batch_name);
            test_series=itemView.findViewById(R.id.series_name);
            marks_obtained=itemView.findViewById(R.id.obtained_marks);
            profile=itemView.findViewById(R.id.user_image);
            rank=itemView.findViewById(R.id.rank);
        }
    }
}
