package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Adapters.ProgressQuestionSetData;
import com.adnan.lgstudies.Adapters.ProgressTestSeriesAdapter;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ProgressQuestionSetActivity extends AppCompatActivity {
DatabaseReference databaseReference;
RecyclerView recyclerView;
ProgressTestSeriesAdapter adapter;
    String mobile;
    ArrayList<ProgressQuestionSetData>list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_question_set);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
       mobile = preferences.getString("login", null);
        recyclerView=findViewById(R.id.recycler_View);
        String batch=getIntent().getStringExtra("batch_name");
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("Progress Report").child(batch);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        list=new ArrayList<>();
        adapter=new ProgressTestSeriesAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(adapter);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String set_name=dataSnapshot.getKey();
                    databaseReference.child(set_name).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                int total_attempt=(int) snapshot.getChildrenCount();
                                ProgressQuestionSetData progressQuestionSetData=new ProgressQuestionSetData(batch,set_name,total_attempt);
                                list.add(progressQuestionSetData);
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
}