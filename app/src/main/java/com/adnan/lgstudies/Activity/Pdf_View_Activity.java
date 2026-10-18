package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.R;

import java.net.URLEncoder;

public class Pdf_View_Activity extends AppCompatActivity {
   WebView pdf_view;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_pdf_view);
        View rootView = findViewById(android.R.id.content); // This is the root view of your activity
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(rootView);
        }
        String name=getIntent().getStringExtra("filename");

//        long downloadId = downloadFile((Pdf_View_Activity) getApplicationContext(), name, ".pdf", "Lachit GS", pdfLink);
//        if (isFileDownloaded(getApplicationContext(), downloadId)) {
//            downloadPdf.setImageResource(R.drawable.baseline_file_download_done_24);
//        }
//            downloadPdf.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    long downloadId = downloadFile((Pdf_View_Activity) getApplicationContext(), name, ".pdf", "Lachit GS", pdfLink);
//                    Toast.makeText(Pdf_View_Activity.this, "Download started", Toast.LENGTH_SHORT).show();
//                }
//
//            });

       // pdf=findViewById(R.id.pdf_name);
      //  pdf.setText(name);
     //   pdfView = findViewById(R.id.pdfView);
        //displayPdfFromUrl(pdfLink);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(name);
        progressDialog.setMessage("Opening...!!!");
        progressDialog.show();

        pdf_view=(WebView)findViewById(R.id.view_pdf_file);
        pdf_view.getSettings().setJavaScriptEnabled(true);
        pdf_view.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });
        String url="";
        try {
            url= URLEncoder.encode(getIntent().getStringExtra("fileurl"),"UTF-8");

        }catch (Exception e){

        }
        pdf_view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);


   }

//
//    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
//        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.parse(url);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
//
//        return downloadManager.enqueue(request);
//    }
//
//    public boolean isFileDownloaded(Context context, long downloadId) {
//        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        DownloadManager.Query query = new DownloadManager.Query();
//        query.setFilterById(downloadId);
//        Cursor cursor = downloadManager.query(query);
//        if (cursor != null && cursor.moveToFirst()) {
//            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
//            int status = cursor.getInt(statusIndex);
//            boolean isDownloaded = status == DownloadManager.STATUS_SUCCESSFUL;
//            cursor.close();
//            return isDownloaded;
//        }
//        return false;
//    }

//    private void displayPdfFromUrl(String url) {
//        new RetrievePdfStream().execute(url);
//    }
//
//    @Override
//    public void onPageChanged(int page, int pageCount) {
//        // Code if you need onPageChange listener
//    }
//
//    @Override
//    public void loadComplete(int nbPages) {
//        // Code if you need loadComplete listener
//        progressDialog.dismiss();
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//        super.onPointerCaptureChanged(hasCapture);
//    }
//
//    class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {
//
//        @Override
//        protected InputStream doInBackground(String... strings) {
//            InputStream inputStream = null;
//            try {
//                URL url = new URL(strings[0]);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                if (urlConnection.getResponseCode() == 200) {
//                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return inputStream;
//        }
//
//        @Override
//        protected void onPostExecute(InputStream inputStream) {
//            pdfView.fromStream(inputStream)
//                    .defaultPage(0)
//                    .onPageChange(Pdf_View_Activity.this)
//                    .enableAnnotationRendering(true)
//                    .onLoad(Pdf_View_Activity.this)
//                    .spacing(10)
//                    .scrollHandle(new DefaultScrollHandle(Pdf_View_Activity.this))
//                    .load();
//        }
//    }
}

