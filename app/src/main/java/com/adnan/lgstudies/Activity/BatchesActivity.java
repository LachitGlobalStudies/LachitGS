package com.adnan.lgstudies.Activity;
import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adnan.lgstudies.Adapters.Batches;
import com.adnan.lgstudies.Adapters.BatchesAdapter;
import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BatchesActivity extends AppCompatActivity implements View.OnClickListener{
RecyclerView recyclerView;
DatabaseReference databaseReference,userBatches,className,coachingCourse;
BatchesAdapter adapter;
ArrayList<Batches> list;
String mobile;
    LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_batches);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        mobile = preferences.getString("login", null);
        Button mybatchesBtn = findViewById(R.id.my_batches_button);
        ImageView batchesbackArr = findViewById(R.id.batches_back_arrow);
        TextView textView=findViewById(R.id.course_name);
        textView.setText(getIntent().getStringExtra("Batches"));
        linearLayout= (LinearLayout) findViewById(R.id.coachingLayout);
        ShowAllBatches();
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

        setupButtons();


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
                                databaseReference=FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("Batches"));
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnap:snapshot.getChildren()){
                                            String course=dataSnap.getKey();
                                            coachingCourse = FirebaseDatabase.getInstance().getReference().child("Class Name").child(buttonName);
                                            coachingCourse.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.hasChild(course)){
                                                        Date currentDate = new Date();
                                                        userBatches = FirebaseDatabase.getInstance().getReference().child("Users").child(mobile);
                                                        userBatches.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.hasChild(course)){
                                                                    Batches batches=dataSnap.getValue(Batches.class);
                                                                    batches.setPremium(true);
                                                                    String expireDate = batches.getEnd_date();
                                                                    SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
                                                                    try {
                                                                        Date date=dateformat.parse(expireDate);
                                                                        if (!currentDate.after(date)) {
                                                                            list.add(batches);
                                                                            adapter.notifyDataSetChanged();
                                                                        }
                                                                    }catch (ParseException e){

                                                                    }

                                                                }
                                                                else {
                                                                    Batches batches =dataSnap.getValue(Batches.class);
                                                                    String expireDate = batches.getEnd_date();
                                                                    SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
                                                                    try {
                                                                        Date date=dateformat.parse(expireDate);
                                                                        if (!currentDate.after(date)) {
                                                                            list.add(batches);
                                                                            adapter.notifyDataSetChanged();
                                                                        }
                                                                    }catch (ParseException e){

                                                                    }

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


    private void ShowAllBatches() {
        userBatches = FirebaseDatabase.getInstance().getReference().child("Users").child(mobile);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("Batches"));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Date currentDate = new Date();
                        String My_Batches=dataSnapshot.getKey();
                        userBatches.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(My_Batches)) {
                                    Batches batches = dataSnapshot.getValue(Batches.class);
                                    batches.setPremium(true);
                                    String expireDate = batches.getEnd_date();
                                    SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
                                    try {
                                        Date date=dateformat.parse(expireDate);
                                        if (!currentDate.after(date)) {
                                            list.add(batches);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }catch (ParseException e){

                                    }


                                } else {
                                    Batches batches = dataSnapshot.getValue(Batches.class);
                                    String expireDate = batches.getEnd_date();
                                    SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
                                    try {
                                        Date date=dateformat.parse(expireDate);
                                        if (!currentDate.after(date)) {
                                            list.add(batches);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }catch (ParseException e){

                                    }
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
                case R.id.my_batches_button:intent=new Intent(BatchesActivity.this,MyBatchesActivity.class);
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