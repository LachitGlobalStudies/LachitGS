package com.adnan.lgstudies.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnan.lgstudies.Adapters.ClassTopicAdapter;
import com.adnan.lgstudies.Adapters.ClassesCategory;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassTopicFragment extends Fragment {

DatabaseReference database;
ClassTopicAdapter adapter;
ArrayList<ClassesCategory> classesCategories;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_class_topic, container, false);
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.class_topic_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        classesCategories=new ArrayList<>();
        adapter=new ClassTopicAdapter(getContext(),classesCategories);
        recycler.setAdapter(adapter);

        database=FirebaseDatabase.getInstance().getReference().child(getActivity().getIntent().getStringExtra("Batches")).child(getActivity().getIntent().getStringExtra("course_name")).child("Classes");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                      String class_category=dataSnapshot.getKey();

                           int total_videos=(int) snapshot.child(class_category).child("Videos").getChildrenCount();
                           int total_notes=(int) snapshot.child(class_category).child("Notes").getChildrenCount();
                           int total_excercises=(int) snapshot.child(class_category).child("Excercise").getChildrenCount();
                        ClassesCategory classesCategory=new ClassesCategory(class_category,total_videos+" Videos",total_notes+" Notes",total_excercises+" Excercises");
                        classesCategories.add(classesCategory);

                    }


                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }



}