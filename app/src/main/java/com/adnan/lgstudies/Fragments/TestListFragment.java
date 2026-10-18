package com.adnan.lgstudies.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnan.lgstudies.Adapters.TestSeriesAdapter;
import com.adnan.lgstudies.Adapters.TestSeriesData;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class TestListFragment extends Fragment {

    RecyclerView recyclerView;
    TestSeriesAdapter adapter;
    ArrayList<TestSeriesData> testSeriesData;
    DatabaseReference databaseReference, userReference;
    String mobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_list, container, false);
        SharedPreferences preferences = requireContext().getSharedPreferences("login", MODE_PRIVATE);
        mobile  = preferences.getString("login", null);
        recyclerView = view.findViewById(R.id.recycler_View);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        testSeriesData = new ArrayList<>();
        adapter = new TestSeriesAdapter(getContext(), testSeriesData);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Access the activity's intent here
        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();

            if (intent != null) {
                String batchName = intent.getStringExtra("batch_name");

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Test Series").child(batchName);
                    userReference = FirebaseDatabase.getInstance().getReference("Users");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String questionSet = dataSnapshot.getKey();
                                    Boolean premium=dataSnapshot.child("premium").getValue(Boolean.class);
                                    userReference.child(mobile).child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(batchName)) {
                                                TestSeriesData testSeries= new TestSeriesData(questionSet,batchName,true);
                                                testSeriesData.add(testSeries);
                                            } else{

                                                TestSeriesData testSeries= new TestSeriesData(questionSet,batchName,premium);
                                                testSeriesData.add(testSeries);
                                            }
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle the error here
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the error here
                        }
                    });

            } else {
                // Handle the case where the intent is null
            }
        } else {
            // Handle the case where the activity is null
        }
    }
}
