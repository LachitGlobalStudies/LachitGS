package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.adnan.lgstudies.Adapters.ImagePagerAdapter;
import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Adapters.RecVidAdapter;
import com.adnan.lgstudies.Adapters.RecVidItems;
import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView live_batches, test_series, record_batch, download, free_study, current_affairs;
    ImageView notification_icon, user_pic;
TextView name;
String profile_photo;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FirebaseAuth auth;
    String UserID="";
    DatabaseReference databaseReference;
    private DatabaseReference adsReference;
    String deviceID;
    CountDownTimer countDownTimer;
    private ViewPager viewPager;
    private int currentImageIndex = 0;
    private Handler handler;
    private Runnable runnable;
    RecyclerView recVid;
    Switch aSwitch;
 ArrayList<RecVidItems>list;
    RecVidAdapter adapter;
    CardView share,call,whatsapp;
    String mobile,email;


private View header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
        checkUpdate();
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }


        deviceID= Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        imageSlider();
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }


                    }
                });


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
         mobile = preferences.getString("login", null);
        databaseReference= FirebaseDatabase.getInstance().getReference();
         name=findViewById(R.id.user_name);
         share=findViewById(R.id.share_card_view);
         call=findViewById(R.id.call_card);
         whatsapp=findViewById(R.id.whats_app_card);
        live_batches=findViewById(R.id.card_view_live_batces);
        test_series=findViewById(R.id.card_view_test_series);
        record_batch=findViewById(R.id.card_view_recorded_batces);
        free_study=findViewById(R.id.card_view_free_study_material);
        download=findViewById(R.id.card_view_download);
        current_affairs=findViewById(R.id.card_view_current_affairs);
        notification_icon=findViewById(R.id.notification_icon);
       // aSwitch=findViewById(R.id.switch_to_other_batches);
        user_pic=findViewById(R.id.user_icon);
        recVid=findViewById(R.id.recycler_recomended);
        recVid.setHasFixedSize(true);
        recVid.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list=new ArrayList<>();
        adapter=new RecVidAdapter(this,list);
        recVid.setAdapter(adapter);
        live_batches.setOnClickListener(this);
        test_series.setOnClickListener(this);
        record_batch.setOnClickListener(this);
        free_study.setOnClickListener(this);
        download.setOnClickListener(this);
        current_affairs.setOnClickListener(this);
        notification_icon.setOnClickListener(this);
        user_pic.setOnClickListener(this);
        share.setOnClickListener(this);
        call.setOnClickListener(this);
        whatsapp.setOnClickListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        toolbar = findViewById(R.id.tooLbar);

        loginStatus();
        databaseReference.child("Recomended Video").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RecVidItems recVidItems = dataSnapshot.getValue(RecVidItems.class);
                        list.add(recVidItems);
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter about the dataset change
                    recVid.setAdapter(adapter); // Set the adapter for the RecyclerView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error here
            }
        });

        databaseReference.child("Users").child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    email=snapshot.child("email").getValue().toString();
                    profile_photo=snapshot.child("pimage").getValue().toString();
                    name.setText(snapshot.child("name").getValue().toString());
                    header=navigationView.getHeaderView(0);
                    TextView profile=(TextView) header.findViewById(R.id.profile_title);
                    TextView header_email=(TextView) header.findViewById(R.id.header_email);
                    profile.setText(snapshot.child("name").getValue().toString());
                    header_email.setText(snapshot.child("email").getValue().toString());
                    ImageView profile_image=(ImageView) header.findViewById(R.id.user_profile_pic);
                    if(profile_photo!=null){
                        Glide.with(getApplicationContext()).load(profile_photo).into(user_pic);
                        Glide.with(getApplicationContext()).load(profile_photo).into(profile_image);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch (id) {
                    case R.id.profile:
                        startActivity(new Intent(MainActivity.this,MyProfileActivity.class));
                        break;
                    case R.id.download:
                    case R.id.card_view_download:
                        startActivity(new Intent(MainActivity.this,DownloadActivity.class));
                        break;
                    case R.id.mybatches:
                            Intent intent=new Intent(MainActivity.this,MyBatchesActivity.class);
                            intent.putExtra("Batches","New Batches");
                            startActivity(intent);
                        break;
                    case R.id.my_recorded_batches:
                        Intent recorded_batch=new Intent(MainActivity.this,MyBatchesActivity.class);
                        recorded_batch.putExtra("Batches","Recorded Batches");
                        startActivity(recorded_batch);
                        break;
                    case R.id.livetuition:

                        break;
                    case R.id.test_results:
                        startActivity(new Intent(MainActivity.this,ProgressActivity.class));
                        break;
                    case R.id.notification:
                    case R.id.notification_icon:
                        startActivity(new Intent(MainActivity.this,NotificationActivity.class));
                        break;
                    case R.id.share:
                        shareApp();
                        break;
                    case R.id.contactUs:
                        startActivity(new Intent(MainActivity.this,ContactActivity.class));
                        break;
                    case R.id.FAQ:
                        startActivity(new Intent(MainActivity.this,FaqActivity.class));
                        break;
                    case R.id.logout:
                        DatabaseReference userlogout=FirebaseDatabase.getInstance().getReference("Users");
                               userlogout.child(mobile).child("loggedIn").setValue(false);
                        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                            // Clear all data
                        editor.clear();

                        // Apply the changes
                        editor.apply();

                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                        break;

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void checkUpdate() {

        DatabaseReference ref =
                FirebaseDatabase.getInstance().getReference("app_update");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Integer latestVersion =
                        snapshot.child("versionCode").getValue(Integer.class);

                String apkUrl =
                        snapshot.child("apkUrl").getValue(String.class);

                if(latestVersion == null || apkUrl == null) return;

                int currentVersion = 0;

                try {

                    PackageInfo pInfo =
                            getPackageManager().getPackageInfo(getPackageName(),0);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        currentVersion = (int) pInfo.getLongVersionCode();
                    else
                        currentVersion = pInfo.versionCode;

                } catch (Exception e){
                    e.printStackTrace();
                }

                if(latestVersion > currentVersion){

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Update Available")
                            .setMessage("New version available")
                            .setPositiveButton("Update",(d,w)->{

                                downloadApk(apkUrl);

                            })
                            .setNegativeButton("Later",null)
                            .show();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void downloadApk(String apkUrl){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Downloading Update");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(() -> {

            try {

                URL url = new URL(apkUrl);
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                connection.connect();

                int fileLength = connection.getContentLength();

                InputStream input = connection.getInputStream();

                File file = new File(getExternalFilesDir("updates"),"update.apk");

                FileOutputStream output = new FileOutputStream(file);

                byte[] data = new byte[4096];
                int count;
                int total = 0;

                while ((count = input.read(data)) != -1) {

                    total += count;

                    int progress = (int)((total * 100) / fileLength);

                    runOnUiThread(() ->
                            progressDialog.setProgress(progress));

                    output.write(data,0,count);
                }

                output.close();
                input.close();

                progressDialog.dismiss();

                installApk(file);

            } catch (Exception e){
                e.printStackTrace();
            }

        }).start();
    }

    private void installApk(File file){

        if(!file.exists()){
            Toast.makeText(this,"APK not found",Toast.LENGTH_LONG).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(
                this,
                getPackageName()+".fileprovider",
                file
        );

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);

    }

    private void imageSlider() {

        adsReference = FirebaseDatabase.getInstance().getReference().child("Ads");
        adsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> imageUrls = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String imageUrl = dataSnapshot.getValue(String.class);
                    imageUrls.add(imageUrl);
                }

                // Now you have a list of image URLs, you can set up your ViewPager
                setupViewPager(imageUrls);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database read error
            }
        });



    }

    private void setupViewPager(List<String> imageUrls) {


            // Create and set up a ViewPager
            ViewPager viewPager = findViewById(R.id.viewPager);
            PagerAdapter pagerAdapter = new ImagePagerAdapter(imageUrls);
            viewPager.setAdapter(pagerAdapter);

            // Define a Runnable to update the ViewPager periodically
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int currentItem = viewPager.getCurrentItem();
                    int nextItem = (currentItem + 1) % imageUrls.size();
                    viewPager.setCurrentItem(nextItem, true);
                    handler.postDelayed(this, 3000); // Delay for 3 seconds
                }
            };

            // Start the image loop when the activity is created
            handler.post(runnable);

    }

    private void loginStatus() {
         countDownTimer=new CountDownTimer(30000,1000) {
             @Override
             public void onTick(long l) {

             }

             @Override
             public void onFinish() {
                    countDownTimer.start();
                 databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(mobile);
                 databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if (snapshot.exists()){
                             String deviceId=snapshot.child("deviceId").getValue().toString();
                             if (deviceId.equals(deviceID)){

                             }else {
                                 logout();
                             }
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });

             }
         }.start();

    }

    private void logout() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setMessage("You are log out.....");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Clear all data
                editor.clear();

                // Apply the changes
                editor.apply();



                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();

            }
        });


        alertDialog.show();

    }
    private void shareApp() {
        // Create an Intent with ACTION_SEND
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // Add the text you want to share (e.g., your app's Play Store link)
        String shareMessage = "Check out this awesome learning app: https://play.google.com/store/apps/details?id=com.adnan.lachitgs";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        // Create a chooser dialog to let the user choose the sharing method
        Intent chooser = Intent.createChooser(shareIntent, "Share via");

        // Check if there's an activity that can handle this intent
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            // If an activity is found, start the chooser dialog
            startActivity(chooser);
        } else {
            // Handle the case where there's no app to handle the share intent
            // You can show a Toast or a message to inform the user
        }
    }
    private void openDialer(String phoneNumber) {
        // Create an Intent with ACTION_DIAL and the phone number
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        // Check if there's an activity that can handle this intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            // If an activity is found, start it
            startActivity(intent);
        } else {
            // Handle the case where there's no app to handle the dialer intent
            // You can show a Toast or a message to inform the user
        }
    }

    public void onBackPressed() {



        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setMessage("Do you want to exit the app ?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finishAffinity();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_view_live_batces:

                    Intent intent=new Intent(MainActivity.this, BatchesActivity.class);
                    intent.putExtra("Batches","New Batches");
                    intent.putExtra("email",email);
                    intent.putExtra("mobile",mobile);
                    startActivity(intent);




                break;
            case R.id.card_view_recorded_batces:
                Intent intent1=new Intent(MainActivity.this, RecordedBatchesActivity.class);
                intent1.putExtra("Batches","Recorded Batches");
                startActivity(intent1);
                break;
            case R.id.card_view_test_series:
                startActivity(new Intent(MainActivity.this, TestseriesActivity.class));
                break;
            case R.id.card_view_free_study_material:
                startActivity(new Intent(MainActivity.this, FreeStudyActivity.class));
                break;
            case R.id.card_view_current_affairs:
                startActivity(new Intent(MainActivity.this, CurrentAffairsActivity.class));
                break;
            case R.id.card_view_download:
                startActivity(new Intent(MainActivity.this, DownloadActivity.class));
                break;
            case R.id.user_icon:
                startActivity(new Intent(MainActivity.this, MyProfileActivity.class));
                break;
            case R.id.notification_icon:
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                break;
            case R.id.call_card:
                openDialer("9101477006");
                break;
            case R.id.share_card_view:
                shareApp();
                break;
            case R.id.whats_app_card:
                Intent whatsapp = new Intent(Intent.ACTION_VIEW);
                whatsapp.setData(Uri.parse("https://wa.me/919101477006"));
                startActivity(whatsapp);
                break;
        }

    }


}