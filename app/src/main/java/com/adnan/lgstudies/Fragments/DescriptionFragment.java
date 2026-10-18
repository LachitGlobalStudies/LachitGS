package com.adnan.lgstudies.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DescriptionFragment extends Fragment {
TextView batche_name,starts_on,ends_on,topic_cover;
ImageView thumbNail,teacher_list;
DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_description, container, false);
        batche_name=view.findViewById(R.id.batche_name);
        starts_on=view.findViewById(R.id.starts_on);
        ends_on=view.findViewById(R.id.ends_on);
        topic_cover=view.findViewById(R.id.topic_cover);
        thumbNail=view.findViewById(R.id.thumbnail);
        teacher_list=view.findViewById(R.id.teacher_list);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(getActivity().getIntent().getStringExtra("Batches")).child(getActivity().getIntent().getStringExtra("course_name"));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                   String startsOn=snapshot.child("start_date").getValue().toString();
                   String endsOn=snapshot.child("end_date").getValue().toString();
                   String course_name=snapshot.child("course_name").getValue().toString();
                   String topicCover=snapshot.child("topic_cover").getValue().toString();
                   String description=snapshot.child("description").getValue().toString();
                   String teacherList=snapshot.child("teacher_list").getValue().toString();
                   String thumbnail=snapshot.child("batch_thumbnail").getValue().toString();
                   batche_name.setText(course_name);
                   starts_on.setText(startsOn);
                   ends_on.setText(endsOn);
                   topic_cover.setText(topicCover);
                   Glide.with(getContext()).load(thumbnail).into(thumbNail);
                   Glide.with(getContext()).load(teacherList).into(teacher_list);



                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;



    }
}