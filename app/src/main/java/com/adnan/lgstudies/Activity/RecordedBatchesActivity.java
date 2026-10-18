package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.Adapters.Batches;
import com.adnan.lgstudies.Adapters.BatchesAdapter;
import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecordedBatchesActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    DatabaseReference databaseReference,userBatches;
    BatchesAdapter adapter;
    TextView batchName;

    String mobile;
    ArrayList<Batches> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_recorded_batches);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        mobile = preferences.getString("login", null);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_recorded_batches);
        Button mybatchesBtn = findViewById(R.id.my_batches_button);
        ImageView batchesbackArr = findViewById(R.id.batches_back_arrow);
        mybatchesBtn.setOnClickListener(this);
        batchesbackArr.setOnClickListener(this);
        recyclerView = findViewById(R.id.batches_recycler_view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("Batches"));
        userBatches = FirebaseDatabase.getInstance().getReference().child("Users").child(mobile);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new BatchesAdapter(this, list);
        recyclerView.setAdapter(adapter);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String My_Batches=dataSnapshot.getKey();
                        userBatches.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(My_Batches)){
                                    Batches batches=dataSnapshot.getValue(Batches.class);
                                    batches.setPremium(true);
                                    list.add(batches);
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    Batches batches =dataSnapshot.getValue(Batches.class);
                                    list.add(batches);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){

            case  R.id.batches_back_arrow:
                onBackPressed();
                break;
            case R.id.my_batches_button:intent=new Intent(RecordedBatchesActivity.this,MyBatchesActivity.class);
            intent.putExtra("Batches",getIntent().getStringExtra("Batches"));
            startActivity(intent);
                break;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}