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

import com.adnan.lgstudies.Activity.ExploreActivity;
import com.adnan.lgstudies.Activity.MybatchesclassroomActivity;
import com.adnan.lgstudies.Activity.PaymetActivity;
import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BatchesAdapter extends RecyclerView.Adapter<BatchesAdapter.MyViewHolder> {
    Context context;
    ArrayList<Batches> list;
String mobile,email;
    public BatchesAdapter(Context context, ArrayList<Batches> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.batch_item_view,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Batches batches = list.get(position);
        if (batches.isPremium()) {

            holder.course_name.setText(batches.getCourse_name());
            holder.start_date.setText(batches.getStart_date());
            holder.end_date.setText(batches.getEnd_date());
            holder.price.setText("Rs." + batches.getPrice());
            holder.buy_now.setText("Lets Study");
            holder.expolre.setVisibility(View.GONE);
            Glide.with(context).load(batches.getBatch_thumbnail()).into(holder.batch_logo);
            holder.batch_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MybatchesclassroomActivity.class);
                    intent.putExtra("course_name", batches.getCourse_name());
                    intent.putExtra("Batches",((Activity) context).getIntent().getStringExtra("Batches"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            holder.buy_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MybatchesclassroomActivity.class);
                    intent.putExtra("course_name", batches.getCourse_name());
                    intent.putExtra("Batches",((Activity) context).getIntent().getStringExtra("Batches"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        } else {
            Glide.with(context).load(batches.getBatch_thumbnail()).into(holder.batch_logo);
            holder.course_name.setText(batches.getCourse_name());
            holder.start_date.setText(batches.getStart_date());
            holder.end_date.setText(batches.getEnd_date());
            holder.price.setText("Rs." + batches.getPrice());
            holder.batch_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ExploreActivity.class);
                    intent.putExtra("Batches",((Activity) context).getIntent().getStringExtra("Batches"));
                    intent.putExtra("course_name", batches.getCourse_name());
                    intent.putExtra("price",batches.getPrice());
                    intent.putExtra("upi_id","9101477006@airtel");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        holder.buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                            Intent intent = new Intent(context, PaymetActivity.class);
                            intent.putExtra("course_name", batches.getCourse_name());
                            intent.putExtra("price",batches.getPrice());
                            intent.putExtra("email",((Activity) context).getIntent().getStringExtra("email"));
                            intent.putExtra("mobile",((Activity) context).getIntent().getStringExtra("mobile"));
                            intent.putExtra("Batches",((Activity) context).getIntent().getStringExtra("Batches"));
                            intent.putExtra("description",batches.getCourse_name()+" Fees");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);



            }
        });
        holder.expolre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExploreActivity.class);
                intent.putExtra("mobile",mobile);
                intent.putExtra("email",email);
                intent.putExtra("course_name", batches.getCourse_name());
                intent.putExtra("Batches",((Activity) context).getIntent().getStringExtra("Batches"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView course_name,start_date,end_date,price,mobile;
        Button expolre,buy_now;
        ImageView batch_logo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            course_name=itemView.findViewById(R.id.course_name);
            start_date=itemView.findViewById(R.id.start_date);
            end_date=itemView.findViewById(R.id.end_date);
            price=itemView.findViewById(R.id.price);
            expolre=itemView.findViewById(R.id.explore);
            buy_now=itemView.findViewById(R.id.buy_now);
            batch_logo=itemView.findViewById(R.id.thumbnail);




        }
    }
}

