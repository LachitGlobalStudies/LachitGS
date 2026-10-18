package com.adnan.lgstudies.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.adnan.lgstudies.R;

import java.util.List;

public class SelectSimActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 123;
    int simSlotToUse = 0; // 0 = SIM 1, 1 = SIM 2
    String number="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, PERMISSION_CODE);
        } else {
            checkSimAndSendSms();
        }
    }

    private void checkSimAndSendSms() {
        final SubscriptionManager subscriptionManager = SubscriptionManager.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            return;
        }

        final List<SubscriptionInfo> sims = subscriptionManager.getActiveSubscriptionInfoList();
        if (sims != null && sims.size() > 0) {
            String[] simLabels = new String[sims.size()];
            for (int i = 0; i < sims.size(); i++) {
                SubscriptionInfo info = sims.get(i);
                int slot = info.getSimSlotIndex();
                String numberDisplay = info.getNumber() != null ? info.getNumber() : "No number";
                simLabels[i] = "SIM " + (slot + 1) + ": " + numberDisplay;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(SelectSimActivity.this);
            builder.setTitle("Select SIM to send SMS");
            builder.setItems(simLabels, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SubscriptionInfo selectedSim = sims.get(which);
                    number = selectedSim.getNumber();
                    sendSmsUsingSim(selectedSim.getSubscriptionId(), "9864232818", "VERIFY_USER " + number);
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            Toast.makeText(this, "No SIM found", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void sendSmsUsingSim(final int subscriptionId, String toPhoneNumber, String message) {
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "Mobile verified successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SelectSimActivity.this,RegisterActivity.class);
                        i.putExtra("mobile", number);
                        startActivity(i);
                        break;
                    default:
                        new AlertDialog.Builder(SelectSimActivity.this)
                                .setTitle("SMS Failed")
                                .setMessage("Failed to send SMS. Do you want to retry?")
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkSimAndSendSms(); // Show SIM selection dialog again
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                        break;

                }
                unregisterReceiver(this);
            }
        }, new IntentFilter("SMS_SENT"));

        SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(subscriptionId);
        smsManager.sendTextMessage(toPhoneNumber, null, message, sentPI, null);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            boolean granted = true;
            for (int res : grantResults) {
                if (res != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (granted) {
                checkSimAndSendSms();
            } else {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}