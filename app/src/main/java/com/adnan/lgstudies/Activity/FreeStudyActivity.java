package com.adnan.lgstudies.Activity;
import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.widget.TextView;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class FreeStudyActivity extends AppCompatActivity {
GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_free_study);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }


        // Initialize GridLayout
        gridLayout = findViewById(R.id.gridLayout);

        // Initialize Firebase Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Reference to "Free Study" node
        DatabaseReference freeStudyRef = databaseRef.child("Free Study");

        // Add ValueEventListener to retrieve data for each child node
        freeStudyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the GridLayout before adding data
                gridLayout.removeAllViews();

                // Iterate through the dataSnapshot and add data to the GridLayout
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String nodeName = childSnapshot.getKey();
                    String nodeValue = childSnapshot.getValue(String.class);

                    // Create a CardView
                    CardView cardView = new CardView(FreeStudyActivity.this);

                    int cardSize = (int) getResources().getDimension(R.dimen.card_size); // Assumes you have a dimen resource for the card size
                    int margin = (int) getResources().getDimension(R.dimen.card_margin); // Assumes you have a dimen resource for the margin

                    // Set the layout parameters for the CardView
                    CardView.LayoutParams layoutParams = new CardView.LayoutParams(cardSize, cardSize);
                    layoutParams.setMargins(margin, margin, margin, margin);

                    cardView.setLayoutParams(layoutParams);


                    // Create a TextView to set the title
                    TextView titleTextView = new TextView(FreeStudyActivity.this);
                    titleTextView.setText(nodeName + ": " + nodeValue);
                    titleTextView.setPadding(16, 16, 16, 16);

                    // Add the TextView to the CardView
                    cardView.addView(titleTextView);

                    // Add the CardView to the GridLayout
                    gridLayout.addView(cardView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur during data retrieval
            }
        });
    }
}