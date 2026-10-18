package com.adnan.lgstudies.Adapters;

import android.annotation.SuppressLint;

import android.content.ActivityNotFoundException;
import android.content.Context;

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

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.R;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DownloadedPDFAdapter extends RecyclerView.Adapter<DownloadedPDFAdapter.NotesViewHolder> {
    Context context;

   private List<String> downloadedPdfList;

    public DownloadedPDFAdapter(Context context) {

        this.context = context;
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
    public DownloadedPDFAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_list_item, parent, false);
        return new NotesViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull DownloadedPDFAdapter.NotesViewHolder holder, int position) {

    holder.pdf_name.setText(downloadedPdfList.get(position));
    holder.pdf_lock.setImageResource(R.drawable.progress_results);
    holder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
openPdf(downloadedPdfList.get(position));
        }
    });

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
        return downloadedPdfList.size();
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