package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Adapters.ProgressReport;
import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultActivity extends AppCompatActivity {
    ImageView imageView;
    TextView subject, testSeries, marksObtained, rank, userName;
    Button ok,re_atmpt;
    DatabaseReference databaseReference, UserRef;
    int marks_obtained;
    int myrank =1;
    int total_user;
    long user_marks=0;
    String name = "", pimg = "";
    String  batch, test_series,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
       mobile = preferences.getString("login", null);
        batch = getIntent().getStringExtra("batch_name");
        test_series = getIntent().getStringExtra("test_series");
        imageView = findViewById(R.id.user_image);
        subject = findViewById(R.id.batch_name);
        testSeries = findViewById(R.id.series_name);
        marksObtained = findViewById(R.id.obtained_marks);
        rank = findViewById(R.id.rank);
        userName = findViewById(R.id.user_name);
        ok=findViewById(R.id.ok_btn);
        re_atmpt=findViewById(R.id.re_attempt);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserRef=FirebaseDatabase.getInstance().getReference("Users").child(mobile).child("Progress Report").child(batch).child(test_series).child(UserRef.push().getKey().toString());
                ProgressReport progressReport=new ProgressReport(batch,test_series,name,pimg,marks_obtained,total_user,myrank);
                UserRef.setValue(progressReport);

                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finish();
            }
        });
        re_atmpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        // Initialize marks_obtained here, after the activity is fully initialized
        marks_obtained = getIntent().getIntExtra("total_score", 0);
        // Fetch data from Firebase
        fetchUserData();
        fetchTotalUsers();
    }

    private void fetchUserData() {
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child(mobile);
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    pimg = snapshot.child("pimage").getValue(String.class);
                    name = snapshot.child("name").getValue(String.class);
                    // Update the UI with fetched user data
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here
                Toast.makeText(ResultActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTotalUsers() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Test Rank").child(batch).child(test_series);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    total_user = (int) snapshot.getChildrenCount();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String user = dataSnapshot.getKey();
                        databaseReference.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

                                    for (DataSnapshot snaps : snapshot.getChildren()) {
                                        long marks = snaps.getValue(Long.class);
                                        if (marks > user_marks) {
                                            user_marks = marks;
                                        }
                                    }
                                }


                                // Update the UI with the rank and other data
                                if (user_marks > marks_obtained) {
                                    myrank++;
                                }
                                updateUI();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error here
                                Toast.makeText(ResultActivity.this, "Error fetching user rank", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here
                Toast.makeText(ResultActivity.this, "Error fetching total users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        marksObtained.setText(""+marks_obtained);
        if (marks_obtained==0){
            rank.setText(total_user + "th out of " + total_user);
        } else if (myrank==1) {
            rank.setText(myrank + "st out of " + total_user);
        } else if (myrank==2) {
            rank.setText(myrank + "nd out of " + total_user);

        } else if (myrank==3) {
            rank.setText(myrank + "rd out of " + total_user);

        }else {
            rank.setText(myrank + "th out of " + total_user);
        }

        testSeries.setText(test_series);
        subject.setText(batch);
        userName.setText(name);
        Glide.with(getApplicationContext()).load(pimg).into(imageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
