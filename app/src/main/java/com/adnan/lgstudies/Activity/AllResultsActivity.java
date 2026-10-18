package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.adnan.lgstudies.Adapters.AllResultAdapter;
import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Adapters.ProgressReport;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllResultsActivity extends AppCompatActivity {
DatabaseReference databaseReference;
RecyclerView recyclerView;
ArrayList<ProgressReport>list;
AllResultAdapter adapter;
String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_results);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        mobile = preferences.getString("login", null);
        recyclerView=findViewById(R.id.recycler_View);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(AllResultsActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        list=new ArrayList<>();
        adapter = new AllResultAdapter(this, list);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mobile)
                               .child("Progress Report").child(getIntent().getStringExtra("batch_name")).child(getIntent().getStringExtra("test_series"));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        ProgressReport progressReport=dataSnapshot.getValue(ProgressReport.class);
                        list.add(progressReport);
                    }

                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}