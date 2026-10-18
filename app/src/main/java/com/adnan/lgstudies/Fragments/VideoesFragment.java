package com.adnan.lgstudies.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnan.lgstudies.Adapters.ClassesAdapter;
import com.adnan.lgstudies.Adapters.VideoUpload;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class VideoesFragment extends Fragment {

    ArrayList<VideoUpload> videoUploads;
    ClassesAdapter adapter;
    DatabaseReference databaseReference,userReference;
    String mobile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences preferences = requireContext().getSharedPreferences("login", MODE_PRIVATE);
        mobile  = preferences.getString("login", null);
        View view= inflater.inflate(R.layout.fragment_videoes, container, false);
        RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.classes_recycler_view);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        videoUploads=new ArrayList<>();
        adapter=new ClassesAdapter(getContext(),videoUploads);
        recyclerView.setAdapter(adapter);
        userReference=FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("Courses");
        databaseReference= FirebaseDatabase.getInstance().getReference().child(getActivity().getIntent().getStringExtra("Batches")).child(getActivity().getIntent().getStringExtra("course_name")).child("Classes").child(getActivity().getIntent().getStringExtra("class_topic")).child("Videos");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(getActivity().getIntent().getStringExtra("course_name"))){
                                VideoUpload videoUpload=dataSnapshot.getValue(VideoUpload.class);
                                videoUpload.setPremium(true);
                                videoUploads.add(videoUpload);
                                adapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(videoUploads.size() - 1);
                            }else{
                                VideoUpload videoUpload=dataSnapshot.getValue(VideoUpload.class);
                                videoUploads.add(videoUpload);
                                adapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(videoUploads.size() - 1);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}