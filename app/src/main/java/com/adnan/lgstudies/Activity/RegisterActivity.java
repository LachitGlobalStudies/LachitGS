package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Adapters.User;
import com.adnan.lgstudies.R;
import com.adnan.lgstudies.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

ActivityRegisterBinding binding;
StorageReference storageReference;
DatabaseReference databaseReference;


ProgressDialog progressDialog;
String name,mobile,otpId,password;
Uri imageUri;
TextView otp_sent;
    String phone;
    private FirebaseAuth mAuth;

    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP,edtPass,edtRePass;

    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    // string for storing our verification ID
    private String verificationId,deviceID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding= ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        deviceID= Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }

        mobile=getIntent().getStringExtra("mobile");

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        mAuth=FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // initializing variables for button and Edittext.
        edtPhone = findViewById(R.id.mobile_box);
        edtOTP = findViewById(R.id.otp_box);
        edtPass = findViewById(R.id.password_box);
        edtRePass = findViewById(R.id.re_password_box);
        verifyOTPBtn = findViewById(R.id.otp_submit_btn);
        generateOTPBtn = findViewById(R.id.SignUpBtn);
        binding.userLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        binding.SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                password=binding.passwordBox.getText().toString();
                    if (password.length()>=6){
                        if (password.equals(edtRePass.getText().toString())){
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    
                                    if (snapshot.hasChild(mobile)){
                                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(mobile);
                                        databaseReference.child("password").setValue(password);
                                        Toast.makeText(RegisterActivity.this, "Password successfully Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                                    }else {
                                        RegisterUser();
//                                       \

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this,"Passwords don't match",Toast.LENGTH_SHORT).show();
                        }


                    }else {
                        binding.passwordBox.setError("Create a minimum 6 digits password");
                        binding.passwordBox.requestFocus();
                    }



            }
        });



    }

   
    

    private void RegisterUser() {
        String imagee="https://firebasestorage.googleapis.com/v0/b/eokn-2c8e7.appspot.com/o/morkuplogo.png?alt=media&token=d8c203be-23d7-4497-a28f-723aff4e621d";
        User user=new User(mobile,"Default Name","default@email",password,imagee,deviceID,true);
        databaseReference.child(mobile).setValue(user);
        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login", mobile);
        editor.apply();
        Toast.makeText(RegisterActivity.this, "Password successfully Created", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        finish();
    }

}