package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adnan.lgstudies.Adapters.MyBatches;
import com.adnan.lgstudies.Adapters.MyBatchesAdapter;
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


public class MyBatchesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyBatchesAdapter adapter;
ArrayList<MyBatches>myBatchesArrayList;

   DatabaseReference databaseReference,database;
    String course_name, start_date, end_date, price, description;

String mobile,course;
TextView batches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_my_batches);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        mobile = preferences.getString("login", null);
        ImageView imageView=findViewById(R.id.my_batches_back_arrow);
        batches=findViewById(R.id.course_name);
        batches.setText("My "+getIntent().getStringExtra("Batches"));
        recyclerView=findViewById(R.id.my_batches_recycler_view);
        myBatchesArrayList=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MyBatchesAdapter(this,myBatchesArrayList);
        recyclerView.setAdapter(adapter);
     databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(mobile).child("Courses");
     databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             if (snapshot.exists()) {
                 for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                     Date currentDate=new Date();
                    course=dataSnapshot.getKey();
                    database=FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("Batches"));
                    database.child(course).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                course_name=snapshot.child("course_name").getValue().toString();
                                start_date=snapshot.child("start_date").getValue().toString();
                                end_date=snapshot.child("end_date").getValue().toString();
                                price=snapshot.child("price").getValue().toString();
                                description=snapshot.child("description").getValue().toString();
                                String thumbnail=snapshot.child("batch_thumbnail").getValue().toString();



                                SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date date=dateformat.parse(end_date);
                                    if (!currentDate.after(date)) {
                                        MyBatches myBatches=new MyBatches(thumbnail,course_name,start_date,end_date,price,description,false);
                                        myBatchesArrayList.add(myBatches);

                                        adapter.notifyDataSetChanged();
                                    }else{
                                        databaseReference.child(course_name).removeValue();
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




             }else {
                 Toast.makeText(MyBatchesActivity.this, "You do not have any subscription", Toast.LENGTH_SHORT).show();
                 
             }

         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}