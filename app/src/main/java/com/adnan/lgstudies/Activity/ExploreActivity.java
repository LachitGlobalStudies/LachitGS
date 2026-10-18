package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adnan.lgstudies.Adapters.Batches;
import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.R;
import com.adnan.lgstudies.Adapters.VPClassroomFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExploreActivity extends AppCompatActivity {
TextView coursename, price;
Button buy_now;
TabLayout tabLayout;
ViewPager2 viewPager2;
VPClassroomFragmentAdapter vpAdapter;

String course_name,mobile,amount;
DatabaseReference databaseReference;
Batches batches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
      mobile = preferences.getString("login", null);
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        coursename=(TextView) findViewById(R.id.batch_title);
        price=(TextView) findViewById(R.id.price_show);
        buy_now=(Button) findViewById(R.id.buy_now_btn);
        ImageView explr_back_arrow=(ImageView)findViewById(R.id.explore_back_arrow);
        explr_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onBackPressed();

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("Batches")).child(getIntent().getStringExtra("course_name"));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    course_name=snapshot.child("course_name").getValue().toString();
                    amount =snapshot.child("price").getValue().toString();
                    coursename.setText(course_name);
                    price.setText("Rs."+amount);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                databaseReference.child(mobile).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String email=snapshot.child("email").getValue().toString();
                            Intent intent = new Intent(ExploreActivity.this, PaymetActivity.class);
                            intent.putExtra("mobile",mobile);
                            intent.putExtra("email",email);
                            intent.putExtra("course_name",course_name);
                            intent.putExtra("price",amount);
                            intent.putExtra("description",course_name+" Fees");
                            intent.putExtra("Batches",getIntent().getStringExtra("Batches"));
                            startActivity(intent);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        viewPager2=findViewById(R.id.viewpager);
        vpAdapter=new VPClassroomFragmentAdapter(this);
        viewPager2.setAdapter(vpAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });






    }

    @Override
    public void onBackPressed() {
        if (viewPager2.getCurrentItem() > 0) {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        } else {
            super.onBackPressed();
        }

    }
}