package com.adnan.lgstudies.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.Activity.Class_Play_Activity;

import com.adnan.lgstudies.Activity.PaymetActivity;
import com.adnan.lgstudies.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ClassesViewHolder>{
    Context context;
    ArrayList<VideoUpload> videoUploads;
RewardedAd rewardedAd;
ProgressDialog progressDialog;
    public ClassesAdapter(Context context, ArrayList<VideoUpload> videoUploads) {
        this.context = context;
        this.videoUploads = videoUploads;

    }

    @NonNull
    @Override
    public ClassesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.lpr_classes_item_view,parent,false);
        return new ClassesViewHolder(view);
    }
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ClassesViewHolder holder,  int position) {
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Wait...");
        VideoUpload videoUpload=videoUploads.get(position);
       if (videoUpload.isPremium()){
            String thumbnail="https://img.youtube.com/vi/"+videoUpload.getVideoId()+"/0.jpg";
           holder.topic_title.setText(videoUploads.get(position).getTopic_title());
           Glide.with(context).load(thumbnail).placeholder(R.drawable.apk_logo).into(holder.thumbnail);
           holder.lock_icon.setVisibility(View.GONE);
           holder.cardView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   AppCompatActivity activity=(AppCompatActivity) view.getContext();
                   Intent intent=new Intent(activity, Class_Play_Activity.class);
                   intent.putExtra("topic_title",videoUploads.get(position).getTopic_title());
                   intent.putExtra("video_id",videoUploads.get(position).getVideoId());
                   intent.putExtra("postkey",videoUploads.get(position).getPostkey());
                   intent.putExtra("course_name",activity.getIntent().getStringExtra("course_name"));
                   intent.putExtra("class_topic",activity.getIntent().getStringExtra("class_topic"));
                   intent.putExtra("Batches",activity.getIntent().getStringExtra("Batches"));

                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(intent);
               }
           });


       }else{
           Glide.with(context).load(videoUploads.get(position).getThumbnail()).into(holder.thumbnail);
           holder.topic_title.setText(videoUploads.get(position).getTopic_title());
           holder.lock_icon.setVisibility(View.VISIBLE);
           holder.cardView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   progressDialog.show();
                   loadRewardedAd(view,position);



               }
           });



       }

    }

    private void loadRewardedAd(View view,int position) {
        AppCompatActivity activity=(AppCompatActivity) view.getContext();
        AdRequest adRequest=new AdRequest.Builder().build();
        RewardedAd.load(context, AdsUnit.rewardedId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                rewardedAd=ad;
                progressDialog.dismiss();
                showAlert(view,position);


            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // loadRewardedAd(view,position);
              progressDialog.dismiss();
            }
        });



    }

    private void showAlert(View view,int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("Do you want to buy the Batch ?\n OR Want to see the ad");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                AppCompatActivity activity=(AppCompatActivity) view.getContext();
                Intent intent=new Intent(activity, PaymetActivity.class);
                intent.putExtra("topic_title",videoUploads.get(position).getTopic_title());
                intent.putExtra("video_id",videoUploads.get(position).getVideoId());
                intent.putExtra("postkey",videoUploads.get(position).getPostkey());
                intent.putExtra("course_name",activity.getIntent().getStringExtra("course_name"));
                intent.putExtra("price",activity.getIntent().getStringExtra("price"));
                intent.putExtra("description",activity.getIntent().getStringExtra("course_name")+" Fees");
                intent.putExtra("Batches",activity.getIntent().getStringExtra("Batches"));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNeutralButton("Show Ad", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppCompatActivity activity=(AppCompatActivity) view.getContext();
                ShowRewardedAd(view,position,activity);
            }
        });

        alertDialog.show();
    }

    private void ShowRewardedAd(View view,int position,AppCompatActivity activity) {

        if (rewardedAd!=null) {
            rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                }
            });
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Intents(activity,position);
                }


            });

        }
    }

    private void Intents(AppCompatActivity activity, int position) {
        Intent intent = new Intent(activity, Class_Play_Activity.class);
        intent.putExtra("topic_title", videoUploads.get(position).getTopic_title());
        intent.putExtra("video_id", videoUploads.get(position).getVideoId());
        intent.putExtra("postkey", videoUploads.get(position).getPostkey());
        intent.putExtra("course_name", activity.getIntent().getStringExtra("course_name"));
        intent.putExtra("class_topic", activity.getIntent().getStringExtra("class_topic"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return videoUploads.size();
    }

    public  static class ClassesViewHolder extends RecyclerView.ViewHolder{
    ImageView thumbnail,lock_icon;
    TextView topic_title;
    ProgressBar progressBar;
    CardView cardView;
        public ClassesViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.thumbnail);
            topic_title=itemView.findViewById(R.id.topic_title);
            progressBar = itemView.findViewById(R.id.progress_bar);
            cardView=itemView.findViewById(R.id.classes_topic_card);
            lock_icon=itemView.findViewById(R.id.lock_icon);
        }
    }
}
