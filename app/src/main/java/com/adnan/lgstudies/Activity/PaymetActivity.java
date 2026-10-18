package com.adnan.lgstudies.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.adnan.lgstudies.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymetActivity extends AppCompatActivity implements PaymentResultListener {
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymet);
        Checkout.preload(getApplicationContext());
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        startPayment();
    }
    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_GVP563KyCPJCpK");
        checkout.setImage(R.drawable.apk_logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Lachit Global Studies");
            options.put("description", getIntent().getStringExtra("description"));
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(getIntent().getStringExtra("price"))*100);//pass amount in currency subunits
            options.put("prefill.email", getIntent().getStringExtra("email"));
            options.put("prefill.contact",getIntent().getStringExtra("mobile"));
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment successful "+s, Toast.LENGTH_SHORT).show();
        databaseReference.child(getIntent().getStringExtra("mobile")).child("Courses").child(getIntent().getStringExtra("course_name")).setValue(true);
        databaseReference.child(getIntent().getStringExtra("mobile")).child("Payments").child(getIntent().getStringExtra("course_name")).child(s).setValue(getIntent().getStringExtra("price"));
       Intent intent=new Intent(PaymetActivity.this, MyBatchesActivity.class);
       intent.putExtra("Batches","New Batches");
       startActivity(intent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment failed "+s, Toast.LENGTH_SHORT).show();
    }
}