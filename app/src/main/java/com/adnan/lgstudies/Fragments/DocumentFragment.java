package com.adnan.lgstudies.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnan.lgstudies.Activity.OrderActivity;
import com.adnan.lgstudies.Adapters.DocumentAdapter;
import com.adnan.lgstudies.Adapters.Notes;
import com.adnan.lgstudies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DocumentFragment extends Fragment {

    ArrayList<Notes> notesItem;
    DatabaseReference userReference, databaseReference;
    DocumentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_document, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.notes_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        notesItem = new ArrayList<>();

       // adapter = new DocumentAdapter(getContext(), notesItem);
     adapter=new DocumentAdapter(new DocumentAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(int position) {
            Notes notes=notesItem.get(position);
            if (notes.isPremium()){
                String name=notes.getName();
                String pdf_link=notes.getPdf_link();
                Bundle bundle=new Bundle();
                bundle.putString("name",name);
                bundle.putString("pdf_link",pdf_link);
                // Replace the current fragment with AnotherFragment when an item is clicked
                DocumentViewFragment documentViewFragment = new DocumentViewFragment();
                documentViewFragment.setArguments(bundle);
                FragmentTransaction transaction = requireFragmentManager().beginTransaction();
                transaction.replace(R.id.container_fragment, documentViewFragment);
                transaction.addToBackStack(null); // Add to back stack for navigation
                transaction.commit();

            }else{      AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setMessage("Do you want to buy the Batch ?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                Intent intent = new Intent(activity, OrderActivity.class);
                                intent.putExtra("course_name", activity.getIntent().getStringExtra("course_name"));
                                intent.putExtra("price", activity.getIntent().getStringExtra("price"));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getContext().startActivity(intent);
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
},getContext(),notesItem);
        userReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("Courses");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Batches").child(getActivity().getIntent().getStringExtra("course_name")).child("Classes").child(getActivity().getIntent().getStringExtra("class_topic")).child("Notes");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(getActivity().getIntent().getStringExtra("course_name"))) {
                                Notes notes = dataSnapshot.getValue(Notes.class);
                                notes.setPremium(true);
                                notesItem.add(notes);
                                adapter.notifyDataSetChanged();
                            } else {
                                Notes notes = dataSnapshot.getValue(Notes.class);
                                notesItem.add(notes);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(adapter);



        return view;
    }
}