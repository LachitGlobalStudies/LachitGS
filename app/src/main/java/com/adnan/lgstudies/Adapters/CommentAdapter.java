package com.adnan.lgstudies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter <CommentAdapter.CommentViewHolder>{
Context context;
ArrayList<CommentClass>commentItems;

    public CommentAdapter(Context context, ArrayList<CommentClass> commentItems) {
        this.context = context;
        this.commentItems = commentItems;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.comment_view_items,parent,false);
       return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentClass commentClass=commentItems.get(position);
        holder.comment.setText(commentClass.getComment());
        holder.name.setText(commentClass.getName());
        holder.time.setText(commentClass.getTime());
        String image=commentClass.getProfile();
        if (image!=null) {
            Glide.with(context).load(commentClass.getProfile()).into(holder.profile);
        }

    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name,comment,time;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            profile=(ImageView) itemView.findViewById(R.id.profile);
            name=(TextView) itemView.findViewById(R.id.name);
            comment=(TextView) itemView.findViewById(R.id.comment);
            time=(TextView) itemView.findViewById(R.id.time);
        }
    }
}
