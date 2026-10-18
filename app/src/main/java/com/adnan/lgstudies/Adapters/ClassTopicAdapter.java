package com.adnan.lgstudies.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.Activity.Classes_List_Activity;
import com.adnan.lgstudies.R;

import java.util.ArrayList;

public class ClassTopicAdapter extends RecyclerView.Adapter<ClassTopicAdapter.ClassTopicViewholder> {

    Context context;
    ArrayList<ClassesCategory>classesCategories ;

    public ClassTopicAdapter(Context context, ArrayList<ClassesCategory> classesCategories) {
        this.context = context;
        this.classesCategories = classesCategories;
    }

    @NonNull
    @Override
    public ClassTopicViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_topic_item_view,parent,false);

       return new ClassTopicViewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ClassTopicViewholder holder, int position) {
        ClassesCategory classesCategory =classesCategories.get(position);
        holder.class_topic.setText(classesCategory.getClass_category());
        holder.total_videos.setText(classesCategory.getVideos_no());
        holder.total_notes.setText(classesCategory.getNotes_no());
        holder.total_exercises.setText(classesCategory.getExcercises());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity) view.getContext();
                Intent intent= new Intent(activity, Classes_List_Activity.class);
                intent.putExtra("class_topic",classesCategory.getClass_category());
                intent.putExtra("course_name",activity.getIntent().getStringExtra("course_name"));
                intent.putExtra("description",activity.getIntent().getStringExtra("description"));
                intent.putExtra("price",activity.getIntent().getStringExtra("price"));
                intent.putExtra("Batches",((Activity) context).getIntent().getStringExtra("Batches"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return classesCategories.size();
    }

    public static class ClassTopicViewholder extends RecyclerView.ViewHolder
    {
        TextView class_topic,total_videos,total_exercises,total_notes;
        CardView cardView;
        public ClassTopicViewholder(@NonNull View itemView) {
            super(itemView);

           class_topic=itemView.findViewById(R.id.class_topic);
           total_exercises=itemView.findViewById(R.id.total_exercises);
           total_videos=itemView.findViewById(R.id.total_video);
           total_notes=itemView.findViewById(R.id.total_notes);
           cardView=itemView.findViewById(R.id.classes_topic_card);


        }
    }

}
