package com.adnan.lgstudies.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.adnan.lgstudies.R;

import java.net.URLEncoder;


public class DocumentViewFragment extends Fragment {


private  ProgressDialog progressDialog;
WebView pdf_view;

public DocumentViewFragment(){


}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_document_view, container, false);
        Bundle bundle=getArguments();
        if (bundle!=null){
            String name=bundle.getString("name");
            String pdf_link=bundle.getString("pdf_link");
            progressDialog=new ProgressDialog(requireContext());
            progressDialog.setTitle(name);
            progressDialog.setTitle(name);
            progressDialog.setMessage("Opening...!!!");
            pdf_view=(WebView)view.findViewById(R.id.view_pdf_file);

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
                url= URLEncoder.encode(pdf_link,"UTF-8");

            }catch (Exception e){

            }
            pdf_view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);

        }



        return view;
    }
}