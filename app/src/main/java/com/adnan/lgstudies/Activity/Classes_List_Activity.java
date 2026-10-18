package com.adnan.lgstudies.Activity;
import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Adapters.VideoUpload;
import com.adnan.lgstudies.Adapters.VideosNotesFragmentHolder;
import com.adnan.lgstudies.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class Classes_List_Activity extends AppCompatActivity {
TextView course_name;

    ImageView back;
    ArrayList<VideoUpload>videoUploads;
    ViewPager2 viewPager2;
    VideosNotesFragmentHolder VNAdapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_classes_list);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        videoUploads=new ArrayList<>();
        back=findViewById(R.id.classes_list_act_backarrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        course_name=(TextView) findViewById(R.id.course_name);
        course_name.setText(getIntent().getStringExtra("class_topic"));
        tabLayout=(TabLayout) findViewById(R.id.video_notes_tab_layout);
       viewPager2 = findViewById(R.id.video_and_notes_viewpager);
        VNAdapter=new VideosNotesFragmentHolder(this);
        viewPager2.setAdapter(VNAdapter);
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