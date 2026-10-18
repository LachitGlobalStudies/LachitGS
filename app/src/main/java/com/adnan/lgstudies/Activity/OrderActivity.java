package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderActivity extends AppCompatActivity {
ImageView Qrimage,order_back_arro;
EditText utr_et_text;
Button payment_done,check_pay;
CardView payment_container,Q_container;
DatabaseReference databaseReference;
TextView dont_wory,textView,already_pay,upi;
ProgressBar progressBar;
String mobile,upiid;
private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_order);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        databaseReference=FirebaseDatabase.getInstance().getReference();
        order_back_arro=findViewById(R.id.order_back_arrow);
        Qrimage=findViewById(R.id.qrcode);
        check_pay=findViewById(R.id.check_payment);
        utr_et_text=findViewById(R.id.utr_number);
        payment_done=findViewById(R.id.payment_done_btn);
        payment_container=findViewById(R.id.payment_details_container);
        Q_container=findViewById(R.id.qr_container);
        progressBar=findViewById(R.id.progressBar2);
        already_pay=findViewById(R.id.already_paid);
        textView=findViewById(R.id.textviewUTR);
        dont_wory=findViewById(R.id.donot_wory);
        upi=findViewById(R.id.upi_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    upiid=snapshot.child("UpiId").getValue(String.class);
                    upi.setText("UPI ID \n" +upiid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        order_back_arro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        utr_et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==12){
                    payment_done.setEnabled(true);
                }else {
                    payment_done.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dont_wory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/919101477006"));
                startActivity(intent);
            }
        });
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        mobile = preferences.getString("login", null);
        already_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Q_container.setVisibility(View.INVISIBLE);
                payment_container.setVisibility(View.VISIBLE);
            }
        });
        check_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Q_container.setVisibility(View.INVISIBLE);
                payment_container.setVisibility(View.VISIBLE);
            }
        });
        databaseReference.child(getIntent().getStringExtra("Batches")).child(getIntent().getStringExtra("course_name")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String qr_code_url=snapshot.child("qr_code").getValue().toString();
                Glide.with(getApplicationContext()).load(qr_code_url).into(Qrimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        payment_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String utr_id=utr_et_text.getText().toString();

                        databaseReference.child("Payments").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    if (snapshot.hasChild(utr_id)){
                                        String amount = snapshot.child(utr_id).child("Amount").getValue().toString();

                                        if (amount.equals(getIntent().getStringExtra("price"))) {
                                            databaseReference.child("Users").child(mobile).child("Courses").child(getIntent().getStringExtra("course_name")).setValue(true);
                                            databaseReference.child("Users").child(mobile).child("Payments").child(utr_id).child("Amount").setValue(amount);
                                            databaseReference.child("Payments").child(utr_id).removeValue();
                                            Intent intent = new Intent(OrderActivity.this, MyBatchesActivity.class);
                                            intent.putExtra("Batches",getIntent().getStringExtra("Batches"));
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                                            textView.setText("You don't pay for " + getIntent().getStringExtra("course_name"));
                                            textView.setTextColor(Color.rgb(225, 0, 0));
                                            Toast.makeText(OrderActivity.this, "You don't pay for " + getIntent().getStringExtra("course_name"), Toast.LENGTH_SHORT).show();
                                    }


                                    }else {
                                        String formattedText = "<font color='#FF0000'><u><b>Do Not Worry.</b></u></font> \nIf your UTR no is showing invalid but you have paid then wait \n for sometime and your money is save." +
                                                "For quick solution you can send your Screenshoot to <a href='https://wa.me/9101477006'><font color='#00FF00'><b>9101477006</b></font></a>.";
                                        dont_wory.setText(Html.fromHtml(formattedText));
                                        utr_et_text.setError("Invalid UTR  or try after some times");
                                        progressBar.setVisibility(View.INVISIBLE);
                                        utr_et_text.setText("");
                                    }
                                }else {
                                    String formattedText = "<font color='#FF0000'><u><b>Do Not Worry.</b></u></font> \nIf your UTR no is showing invalid but you have paid then wait \n for sometime and your money is save." +
                                            "For quick solution you can send your Screenshoot to <a href='https://wa.me/9101477006'><font color='#00FF00'><b>9101477006</b></font></a>.";
                                    dont_wory.setText(Html.fromHtml(formattedText));
                                    utr_et_text.setError("Invalid UTR  or try after some times");
                                    progressBar.setVisibility(View.INVISIBLE);
                                    utr_et_text.setText("");
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            }
        });


    }

    @Override
    public void onBackPressed() {

        if (payment_container.getVisibility()==View.VISIBLE){
            payment_container.setVisibility(View.INVISIBLE);
                Q_container.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }


    }
}
