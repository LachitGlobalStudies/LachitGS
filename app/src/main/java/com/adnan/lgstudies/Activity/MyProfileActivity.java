package com.adnan.lgstudies.Activity;
import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MyProfileActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    EditText etname,etmobile,etemail;
    ImageView profile,edit_icon;
    Button updateButton;
    private Uri imageUri;
    StorageReference storageReference;
    String user_mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_my_profile);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        etname=findViewById(R.id.etName);
        etemail=findViewById(R.id.etEmail);
        etmobile=findViewById(R.id.etMobile);
        profile=findViewById(R.id.ivProfile);
        updateButton=findViewById(R.id.btnUpdate);
        edit_icon=findViewById(R.id.edit_photo);
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        user_mobile = preferences.getString("login", null);
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(user_mobile).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                     String profile_photo=snapshot.child("pimage").getValue().toString();
                    String  name =snapshot.child("name").getValue().toString();
                   String email=snapshot.child("email").getValue().toString();
                   String mobile=snapshot.child("mobile").getValue().toString();

                    Glide.with(getApplicationContext()).load(profile_photo).into(profile);
                    profile.setEnabled(false);
                    etname.setText(name);
                    etname.setEnabled(false);
                    etemail.setText(email);
                    etemail.setEnabled(false);
                    etmobile.setText(mobile);
                    etmobile.setEnabled(false);
                    updateButton.setEnabled(false);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.setEnabled(true);
                etemail.setEnabled(true);
                etname.setEnabled(true);
                etmobile.setEnabled(false);
                updateButton.setEnabled(true);
                edit_icon.setEnabled(false);
                edit_icon.setImageResource(R.drawable.edit_round_line_icon);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=etname.getText().toString();
                String email=etemail.getText().toString();
                String mobile=etmobile.getText().toString();
                if (imageUri!=null){

                    storageReference= FirebaseStorage.getInstance().getReference("images/"+email);
                    storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                  databaseReference.child(user_mobile).child("name").setValue(name);
                                   databaseReference.child(user_mobile).child("email").setValue(email);
                                   databaseReference.child(user_mobile).child("mobile").setValue(mobile);
                                   databaseReference.child(user_mobile).child("pimage").setValue(uri.toString());
                                   etmobile.setEnabled(false);
                                   etemail.setEnabled(false);
                                   etname.setEnabled(false);
                                   updateButton.setEnabled(false);
                                   edit_icon.setEnabled(true);
                                   edit_icon.setImageResource(R.drawable.edit_icon);
                                   Toast.makeText(MyProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                               }
                           });
                        }
                    });

                }else{
                    databaseReference.child(user_mobile).child("name").setValue(name);
                    databaseReference.child(user_mobile).child("email").setValue(email);
                    databaseReference.child(user_mobile).child("mobile").setValue(mobile);
                    etmobile.setEnabled(false);
                    etname.setEnabled(false);
                    updateButton.setEnabled(false);
                    edit_icon.setEnabled(true);
                    edit_icon.setImageResource(R.drawable.edit_icon);
                    Toast.makeText(MyProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }


            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


    }

    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK){
            imageUri=data.getData();
            profile.setImageURI(imageUri);
        }
    }
}