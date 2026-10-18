package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
private TextView textView,textview_reset_password;
EditText mobileBox,passwordBox;
FirebaseAuth auth;
private  DatabaseReference databaseReference;
Toast toast;
ProgressDialog progressDialog;
String deviceId;
    String mobile,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        deviceId= Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        textview_reset_password=findViewById(R.id.reset_password);
        textview_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, SelectSimActivity.class);
                startActivity(intent);
            }
        });
        // Checking for a valid token during app launch
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        String login = preferences.getString("login", null);

        if (login != null) {
            // Token exists, user is already logged in, navigate to the dashboard
            startActivity(new Intent(this, MainActivity.class));
            finish(); // Finish the login activity so the user can't go back to it
        }


        mobileBox=findViewById(R.id.mobile_box);
        passwordBox=findViewById(R.id.password_box);
       textView=(TextView) findViewById(R.id.user_register_page);

       auth=FirebaseAuth.getInstance();

        Button login_button =findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               mobile=mobileBox.getText().toString().trim();
               password=passwordBox.getText().toString().trim();

               if (!mobile.isEmpty()&& mobile.length()==10){
                   if (!password.isEmpty())
                   {
                       databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mobile);
                       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if (snapshot.exists()){
                                   String pass=snapshot.child("password").getValue().toString();
                                   if (password.equals(pass)){
                                       boolean isloggedin = (boolean) snapshot.child("loggedIn").getValue();
                                       if (isloggedin) {
                                           // User is already logged in on another device
                                           // Send a message/notification to the other device(s) to inform them of logout
                                           // Then, log out the current device
                                           showLogoutDialog();
                                       } else {
                                           // The user is not logged in on other devices
                                           // Store the token securely (use SharedPreferences or Keystore)
                                           SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                                           SharedPreferences.Editor editor = preferences.edit();
                                           editor.putString("login", mobile);
                                           editor.apply();
                                           // Update the user's status as logged in
                                           databaseReference.child("loggedIn").setValue(true);
                                           databaseReference.child("deviceId").setValue(deviceId);
                                           // Redirect to the main activity
                                           startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                           finish();
                                       }
                                   }else {
                                       Toast.makeText(LoginActivity.this, "You have entered wrong password", Toast.LENGTH_SHORT).show();
                                   }
                               }else {
                                   Toast.makeText(LoginActivity.this, "You are not a registerd user", Toast.LENGTH_SHORT).show();
                               }

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });

                   }else {
                       passwordBox.setError("Enter Valid Password");
                       passwordBox.requestFocus();
                   }
               }else {
                   mobileBox.setError("Enter Valid Mobile number");
                   mobileBox.requestFocus();
               }



            }
        });
       textView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(LoginActivity.this,SelectSimActivity.class));

               finish();
           }
       });

    }
    // Function to show the logout dialog
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout from all devices");
        builder.setMessage("You are already logged in on another device. Do you want to log out from all devices?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(mobile);

                // Set the "loggedIn" status to false in the Firebase Realtime Database
                userRef.child("loggedIn").setValue(true);
                userRef.child("deviceId").setValue(deviceId);

                // Proceed with the login on the current device

                // Store the token securely (use SharedPreferences or Keystore)
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("login", mobile);
                editor.apply();

                // Redirect to the main activity
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                progressDialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}