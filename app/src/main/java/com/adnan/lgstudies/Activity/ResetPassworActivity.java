package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.R;
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

import java.util.concurrent.TimeUnit;

public class ResetPassworActivity extends AppCompatActivity {
FirebaseAuth mAuth;
String mobile,otpId;
ProgressBar progressBar;
LinearLayout mobile_cont,otp_cont,pa_re_con;
Button continueBtn,otpBtn,createBtn;
EditText otp_box,pass_box,re_pass_box;
TextView otp_send,otptextview;
CountDownTimer countDownTimer;
long totalTimeInMillis=2*60*1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passwor);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        otp_box=findViewById(R.id.otp_box);
        pass_box=findViewById(R.id.password_box);
        re_pass_box=findViewById(R.id.re_password_box);
        mobile_cont=findViewById(R.id.mobile_container);
        otp_cont=findViewById(R.id.otp_container);
        pa_re_con=findViewById(R.id.password_reset_container);
        continueBtn=findViewById(R.id.continue_btn);
        otpBtn=findViewById(R.id.otp_submit_btn);
        createBtn=findViewById(R.id.create_password);
        otp_send=findViewById(R.id.opt_send_textview);
        otptextview=findViewById(R.id.otp_duration);
        otptextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateOTP();
                startCountdownTimer();
                otptextview.setEnabled(false);
            }
        });

        progressBar=findViewById(R.id.progressBar3);
        mAuth=FirebaseAuth.getInstance();
        EditText email=(EditText) findViewById(R.id.reset_password_email);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().length()==10 && !email.getText().toString().isEmpty()){
                    mobile=email.getText().toString();
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(mobile);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                otp_cont.setVisibility(View.VISIBLE);
                                mobile_cont.setVisibility(View.INVISIBLE);
                                otp_send.setText("OTP has sent to +91"+mobile);
                                initiateOTP();
                                startCountdownTimer();
                            }else {
                                Toast.makeText(ResetPassworActivity.this, "You are not a registered user", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    email.setError("Enter Valid Mobile No");
                    email.requestFocus();
                }


            }
        });
        otpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=otp_box.getText().toString();
                otp_cont.setVisibility(View.INVISIBLE);
                PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpId,otp);
                signInWithPhoneAuthCredential(credential);
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=pass_box.getText().toString();
                String repassword=re_pass_box.getText().toString();
                if (password.equals(repassword)){
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(mobile);
                    databaseReference.child("password").setValue(password);
                    Toast.makeText(ResetPassworActivity.this, "Password successfully Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPassworActivity.this,LoginActivity.class));

                }else{
                    Toast.makeText(ResetPassworActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void initiateOTP() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+mobile)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(ResetPassworActivity.this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(          new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpId=s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                otp_box.setText(phoneAuthCredential.getSmsCode());
                                signInWithPhoneAuthCredential(phoneAuthCredential);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ResetPassworActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        })        // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);




    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                                pa_re_con.setVisibility(View.VISIBLE);
                            otp_cont.setVisibility(View.INVISIBLE);
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the timer text with the remaining time
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                // Update a TextView with the remaining time
                otptextview.setText(timeLeftFormatted);

            }

            @Override
            public void onFinish() {

                otptextview.setText("Resend OTP!");
                otptextview.setEnabled(true);

            }
        };
        countDownTimer.start();
    }
}