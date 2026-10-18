package com.adnan.lgstudies.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.Activity.PaymetActivity;
import com.adnan.lgstudies.Activity.Pdf_View_Activity;
import com.adnan.lgstudies.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    Context context;
    ArrayList<Notes> notesItem;

    ProgressDialog progressDialog;
RewardedAd rewardedAd;
   private List<String> downloadedPdfList;
   long downloadId;

    public NotesAdapter(Context context, ArrayList<Notes> notesItem) {

        this.context = context;
        this.notesItem = notesItem;
        this.downloadedPdfList=retrieveDownloadedFiles();

    }

    private List<String> retrieveDownloadedFiles() {
        List<String> downloadedFiles=new ArrayList<>();
        File directory =new File(context.getExternalFilesDir(null)+"/Lachit GS");
        if(directory.exists()){
            File[] files=directory.listFiles();
            if(files!=null){
                for (File file:files){
                    if (file.isFile()){
                        downloadedFiles.add(file.getName());
                      
                    }
                }
            }
        }
        return downloadedFiles;
    }

    @NonNull
    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_list_item, parent, false);
        return new NotesViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesViewHolder holder, int position) {
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Wait...");

        if (notesItem.get(position).isPremium) {
            holder.pdf_name.setText(notesItem.get(position).getName());
            holder.pdf_lock.setImageResource(R.drawable.baseline_arrow_circle_down_24);
            if (downloadedPdfList.contains(notesItem.get(position).getName()+".pdf")){
                holder.pdf_lock.setImageResource(R.drawable.progress_results);
            }
            holder.pdf_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!downloadedPdfList.contains(notesItem.get(position).getName()+".pdf")){
                        downloadId=downloadFile(context,notesItem.get(position).getName(),".pdf","Lachit GS",notesItem.get(position).getPdf_link());

                        Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show();
                    }else {
                       openPdf(notesItem.get(position).getName()+".pdf");
                    }
                }
            });
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!downloadedPdfList.contains(notesItem.get(position).getName()+".pdf")){
                        downloadId=downloadFile(context,notesItem.get(position).getName(),".pdf","Lachit GS",notesItem.get(position).getPdf_link());

                        Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show();
                    }else {
                        openPdf(notesItem.get(position).getName()+".pdf");
                    }
                }
            });

        } else {
            holder.pdf_name.setText(notesItem.get(position).getName());
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

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity, PaymetActivity.class);
                intent.putExtra("course_name", activity.getIntent().getStringExtra("course_name"));
                intent.putExtra("price", activity.getIntent().getStringExtra("price"));
                intent.putExtra("description", activity.getIntent().getStringExtra("course_name") + " Fees");
                intent.putExtra("Batches", ((Activity) context).getIntent().getStringExtra("Batches"));
                intent.putExtra("course_name", activity.getIntent().getStringExtra("course_name"));
                intent.putExtra("Batches", ((Activity) context).getIntent().getStringExtra("Batches"));
                intent.putExtra("price", activity.getIntent().getStringExtra("price"));
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
        Intent intent = new Intent(activity, Pdf_View_Activity.class);
        intent.putExtra("filename",notesItem.get(position).getName());
        intent.putExtra("fileurl", notesItem.get(position).getPdf_link());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        downloadedPdfList.add(fileName+fileExtension);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        notifyDataSetChanged();
        return downloadManager.enqueue(request);
    }


    private void openPdf(String pdfName) {
        File pdfFile = new File(context.getExternalFilesDir(null), "Lachit GS/" + pdfName);
        if (pdfFile.exists()) {
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Add this line
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public int getItemCount() {
        return notesItem.size();
    }



    public class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView  pdf_name;
        ImageView pdf_lock;
        CardView cardView;
        ProgressBar progressBar;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            pdf_name=itemView.findViewById(R.id.pdf_file_name);
            pdf_lock=(ImageView) itemView.findViewById(R.id.download_pdf_lock);
            cardView=(CardView) itemView.findViewById(R.id.card);
            progressBar=itemView.findViewById(R.id.progress_bar);
        }
    }
}