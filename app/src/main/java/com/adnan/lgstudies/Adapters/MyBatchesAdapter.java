package com.adnan.lgstudies.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.Activity.MybatchesclassroomActivity;
import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyBatchesAdapter extends RecyclerView.Adapter<MyBatchesAdapter.MyBatchesViewHolder> {
    Context context;
    ArrayList<MyBatches>myBatchesArrayList;

    public MyBatchesAdapter(Context context, ArrayList<MyBatches> myBatchesArrayList) {
        this.context = context;
        this.myBatchesArrayList = myBatchesArrayList;
    }

    @NonNull
    @Override
    public MyBatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.my_batches_item_view,parent,false);
        return new MyBatchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBatchesViewHolder holder, int position) {
        MyBatches myBatches=myBatchesArrayList.get(position);
        holder.price.setText(myBatches.getPrice());
        holder.stsrt_date.setText(myBatches.getStart_date());
        holder.end_date.setText(myBatches.getEnd_date());
        holder.course_name.setText(myBatches.getCourse_name());
        Glide.with(context).load(myBatches.getBatch_thumbnail()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, MybatchesclassroomActivity.class);
                intent.putExtra("course_name",myBatches.getCourse_name());
                intent.putExtra("description",myBatches.getDescription());
                intent.putExtra("Batches",((Activity) context).getIntent().getStringExtra("Batches"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.lets_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, MybatchesclassroomActivity.class);
                intent.putExtra("course_name",myBatches.getCourse_name());
                intent.putExtra("description",myBatches.getDescription());
                intent.putExtra("Batches",((Activity) context).getIntent().getStringExtra("Batches"));

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return myBatchesArrayList.size();
    }

    public static class MyBatchesViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView stsrt_date,end_date,price,course_name;
        Button lets_study;
        public MyBatchesViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail=itemView.findViewById(R.id.thumbnail);
            stsrt_date=itemView.findViewById(R.id.start_date);
            end_date=itemView.findViewById(R.id.end_date);
            price=itemView.findViewById(R.id.price);
            course_name=itemView.findViewById(R.id.course_name);
            lets_study=itemView.findViewById(R.id.lets_study);

        }
    }

}
