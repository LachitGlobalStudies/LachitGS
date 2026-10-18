package com.adnan.lgstudies.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<String> notificationList; // Replace 'String' with your actual data type

    // Constructor to initialize the notification list
    public NotificationAdapter(List<String> notificationList) {
        this.notificationList = notificationList;
    }

    // Method to update notification list
    public void setNotificationList(List<String> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged(); // Notify RecyclerView that data has changed
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_view, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        String notification = notificationList.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    // ViewHolder class
    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTextView = itemView.findViewById(R.id.notification);
        }

        // Method to bind data to views
        public void bind(String notification) {
            notificationTextView.setText(notification);
        }
    }
}
