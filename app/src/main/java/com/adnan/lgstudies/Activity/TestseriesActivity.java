package com.adnan.lgstudies.Activity;


import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Adapters.TestCategory;
import com.adnan.lgstudies.Adapters.TestTopicAdapter;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;;

public class TestseriesActivity extends AppCompatActivity {
RecyclerView recyclerView;
DatabaseReference databaseReference,userBatches,className,coachingCourse;
TestTopicAdapter adapter;
ArrayList <TestCategory>list;
LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_testseries);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        linearLayout=findViewById(R.id.testLayout);
        recyclerView=findViewById(R.id.recycler_View);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        userBatches= FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
       list=new ArrayList<>();
        adapter=new TestTopicAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(adapter);
        loadAll();
        setupButtons();


    }

    private void loadAll() {
        databaseReference.child("Test Series").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String test_name=dataSnapshot.getKey();
                        int total_test=(int) snapshot.child(test_name).getChildrenCount();
                        TestCategory testCategory=new TestCategory(test_name,total_test+" Test Series");
                        list.add(testCategory);


                    }



                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setupButtons() {

        className = FirebaseDatabase.getInstance().getReference().child("Class Name");
        className.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linearLayout.removeAllViews(); // Clear existing buttons
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String buttonName = dataSnapshot.getKey();

                    // Create button dynamically
                    Button button = new Button(getApplicationContext());
                    button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    button.setText(buttonName);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Clear list before fetching new data
                            list.clear();
                            adapter.notifyDataSetChanged();


                            // Fetch data corresponding to the clicked button
                            databaseReference=FirebaseDatabase.getInstance().getReference().child("Test Series");
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnap:snapshot.getChildren()){
                                        String course=dataSnap.getKey();
                                        coachingCourse = FirebaseDatabase.getInstance().getReference().child("Class Name").child(buttonName);
                                        coachingCourse.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snaps) {
                                                if (snaps.hasChild(course)){
                                                    String test_name=dataSnap.getKey();
                                                    int total_test=(int) snapshot.child(test_name).getChildrenCount();
                                                    TestCategory testCategory=new TestCategory(test_name,total_test+" Test Series");
                                                    list.add(testCategory);
                                                    adapter.notifyDataSetChanged();

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


                        }
                    });
                    linearLayout.addView(button);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
}