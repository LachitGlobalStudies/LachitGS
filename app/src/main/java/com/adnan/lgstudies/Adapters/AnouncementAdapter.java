package com.adnan.lgstudies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AnouncementAdapter extends RecyclerView.Adapter<AnouncementAdapter.NotificationViewHolder> {
    Context context;
    ArrayList arrayList;


    public AnouncementAdapter(Context context, ArrayList arrayList, Intent intent) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public AnouncementAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.anouncement_item_view,parent,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnouncementAdapter.NotificationViewHolder holder, int position) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Batches");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView anounce;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            anounce=itemView.findViewById(R.id.anouncement);
        }
    }
}
