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
import com.adnan.lgstudies.Activity.BatchesActivity;
import com.adnan.lgstudies.Activity.TestStartActivity;
import com.adnan.lgstudies.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;

public class TestSeriesAdapter extends RecyclerView.Adapter<TestSeriesAdapter.TestSeriesViewHolder> {
    Context context;
    TestSeriesData testSeriesData;
    ArrayList<TestSeriesData>datalist;
    ProgressDialog progressDialog;
RewardedAd rewardedAd;
    public TestSeriesAdapter(Context context, ArrayList<TestSeriesData> datalist) {
        this.context = context;
        this.datalist = datalist;

    }

    @NonNull
    @Override
    public TestSeriesAdapter.TestSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.test_series_item_view,parent,false);
        return new TestSeriesViewHolder(view);
    }
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull TestSeriesAdapter.TestSeriesViewHolder holder,  int position) {
progressDialog=new ProgressDialog(context);
progressDialog.setMessage("Wait...");
      testSeriesData =datalist.get(position);
        if (testSeriesData.isPremium){
            holder.lock_icon.setVisibility(View.INVISIBLE);
            holder.test_series_title.setText(testSeriesData.getTitle());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, TestStartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("test_series",datalist.get(position).getTitle());
                    intent.putExtra("batch_name",datalist.get(position).getBatch_name());
                    context.startActivity(intent);

                }
            });

        }else {
            holder.test_series_title.setText(testSeriesData.getTitle());
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
                Intent intent=new Intent(activity, BatchesActivity.class);
                intent.putExtra("course_name",activity.getIntent().getStringExtra("batch_name"));
                intent.putExtra("Batches","New Batches");
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
                ShowRewardedAd(activity,position);
            }
        });

        alertDialog.show();
    }

    private void ShowRewardedAd(AppCompatActivity activity,int position) {

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
                    Intents(position);
                }


            });

        }
    }

    private void Intents(int position) {
        Intent intent=new Intent(context, TestStartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("test_series",datalist.get(position).getTitle());
        intent.putExtra("batch_name",datalist.get(position).getBatch_name());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class TestSeriesViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView lock_icon;
        TextView test_series_title;
        ProgressBar progressBar;
        public TestSeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.test_series_card);
            lock_icon=itemView.findViewById(R.id.test_series_lock);
            test_series_title=itemView.findViewById(R.id.test_series_title);
            progressBar=itemView.findViewById(R.id.progress_bar);

        }
    }
}
