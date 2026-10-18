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

import com.adnan.lgstudies.Adapters.Notes;
import com.adnan.lgstudies.Adapters.NotesAdapter;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NotesFragment extends Fragment {
ArrayList<Notes>notesItem;
NotesAdapter adapter;
DatabaseReference userReference,databaseReference;
String mobile;
   private Set<String> mDownloadedFiles = new HashSet<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences preferences = requireContext().getSharedPreferences("login", MODE_PRIVATE);
        mobile  = preferences.getString("login", null);
        View view= inflater.inflate(R.layout.fragment_notes, container, false);

        RecyclerView recyclerView= view.findViewById(R.id.notes_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        notesItem=new ArrayList<>();
        adapter=new NotesAdapter(getContext(),notesItem);
        recyclerView.setAdapter(adapter);
        userReference= FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("Courses");
        databaseReference= FirebaseDatabase.getInstance().getReference().child(getActivity().getIntent().getStringExtra("Batches"))
                .child(getActivity().getIntent().getStringExtra("course_name")).child("Classes")
                .child(getActivity().getIntent().getStringExtra("class_topic")).child("Notes");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(getActivity().getIntent().getStringExtra("course_name"))){
                                Notes notes=dataSnapshot.getValue(Notes.class);
                                notes.setPremium(true);
                                notesItem.add(notes);
                                adapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(notesItem.size() - 1);
                            }else{
                                Notes notes=dataSnapshot.getValue(Notes.class);
                                notesItem.add(notes);
                                adapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(notesItem.size() - 1);
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
