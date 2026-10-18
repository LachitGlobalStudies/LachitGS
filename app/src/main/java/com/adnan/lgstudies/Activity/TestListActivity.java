package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Fragments.TestListFragment;
import com.adnan.lgstudies.R;

public class TestListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        // Begin a fragment transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Instantiate the fragment
        TestListFragment testListFragment = new TestListFragment();
        // Replace the fragment container with the fragment
        fragmentTransaction.replace(R.id.frame_layout_test_list, testListFragment);
        fragmentTransaction.commit();




    }
}