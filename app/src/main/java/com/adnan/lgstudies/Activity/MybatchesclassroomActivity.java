package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Adapters.VPClassroomFragmentAdapter;
import com.adnan.lgstudies.R;
import com.google.android.material.tabs.TabLayout;

public class MybatchesclassroomActivity extends AppCompatActivity {
TextView price,course_name;
ViewPager2 viewPager2;
VPClassroomFragmentAdapter vpAdapter;
TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_mybatchesclassroom);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        ImageView back=findViewById(R.id.explore_back_arrow);
       course_name=(TextView) findViewById(R.id.batch_title);
        course_name.setText(getIntent().getStringExtra("course_name"));
        viewPager2=findViewById(R.id.viewpager);
        vpAdapter=new VPClassroomFragmentAdapter(this);
        viewPager2.setAdapter(vpAdapter);
        tabLayout=findViewById(R.id.tab_layout);

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

        TabLayout.Tab tab=tabLayout.getTabAt(1);
        tab.select();
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




    }

    @Override
    public void onBackPressed() {
        if (viewPager2.getCurrentItem() >1) {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        } else if (viewPager2.getCurrentItem()<1){
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);

        }else {
            super.onBackPressed();
        }

    }
}
