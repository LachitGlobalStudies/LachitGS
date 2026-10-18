package com.adnan.lgstudies.Fragments;

import static android.content.Context.MODE_PRIVATE;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;

import com.adnan.lgstudies.Adapters.CommentAdapter;
import com.adnan.lgstudies.Adapters.CommentClass;
import com.adnan.lgstudies.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CommentFragment extends Fragment {


    EditText comment;
    ImageView comment_send;
    WebView webView;
    RecyclerView recyclerView;
    CommentAdapter adapter;
    ArrayList<CommentClass> commentItems;
    String mobile;
    DatabaseReference databaseReference, userReference, commentReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_comment, container, false);
        SharedPreferences preferences = requireContext().getSharedPreferences("login", MODE_PRIVATE);
        mobile  = preferences.getString("login", null);
        commentReference = FirebaseDatabase.getInstance().getReference("Comments").child(getActivity().getIntent().getStringExtra("postkey"));

        comment_send =view.findViewById(R.id.comment_send);
        comment =view.findViewById(R.id.comment);
        recyclerView =view.findViewById(R.id.comment_recyclerview);
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int count, int i2) {
                if (charSequence.length() > 0) {
                    comment_send.setImageResource(R.drawable.baseline_send_24);
                    comment_send.setClickable(true);
                } else {
                    comment_send.setImageResource(R.drawable.send_inactive);
                    comment_send.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        commentItems = new ArrayList<>();
        adapter = new CommentAdapter(getContext(), commentItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        commentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                commentItems.clear();
                layoutManager.scrollToPositionWithOffset(commentItems.size() - 1, 0);
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CommentClass commentClass = dataSnapshot.getValue(CommentClass.class);
                        commentItems.add(commentClass);
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(commentItems.size() - 1);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String postkey = getActivity().getIntent().getStringExtra("postkey");

                userReference = FirebaseDatabase.getInstance().getReference("Users").child(mobile);
                databaseReference = FirebaseDatabase.getInstance().getReference("Comments");
                // databaseReference = FirebaseDatabase.getInstance().getReference().child("New Batches").child(getIntent().getStringExtra("course_name")).child("Classes").child(getIntent().getStringExtra("class_topic")).child("Videos").child(postkey);
                String message = comment.getText().toString();
                if (!message.isEmpty()) {
                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String profile_photo = snapshot.child("pimage").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy '\n'HH:mm:ss");
                            String currenttime = sdf.format(new Date());
                            CommentClass commentClass = new CommentClass(name, message, currenttime, profile_photo);
                            String randomkey = databaseReference.push().getKey();
                            databaseReference.child(postkey).child(randomkey).setValue(commentClass);
                            comment.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        return view;
    }
}