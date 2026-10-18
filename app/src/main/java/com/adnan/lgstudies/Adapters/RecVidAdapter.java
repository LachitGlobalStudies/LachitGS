package com.adnan.lgstudies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.R;

import java.util.ArrayList;

public class RecVidAdapter extends RecyclerView.Adapter<RecVidAdapter.MyViewHolder> {
Context context;
    ArrayList<RecVidItems>list;

    public RecVidAdapter(Context context, ArrayList<RecVidItems> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recomended_video_item_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecVidItems recVidItems = list.get(position);
        holder.textView.setText(recVidItems.getTitle());

        String videoId = recVidItems.getVidLink();

// Load player.html ONLY ONCE
        if (holder.webView.getTag() == null) {

            WebSettings s = holder.webView.getSettings();
            s.setJavaScriptEnabled(true);
            s.setDomStorageEnabled(true);
            s.setMediaPlaybackRequiresUserGesture(false);
            s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            holder.webView.setWebChromeClient(new WebChromeClient());

            holder.webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    // ✅ Call JS AFTER page load
                    view.evaluateJavascript(
                            "playFromAndroid('" + videoId + "');",
                            null
                    );
                }
            });

            holder.webView.loadUrl("https://lachitglobalstudies.kesug.com/playr.html");
            holder.webView.setTag("loaded"); // mark as loaded

        } else {
            // ✅ Player already loaded → just change video
            holder.webView.evaluateJavascript(
                    "playFromAndroid('" + videoId + "');",
                    null
            );
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        WebView webView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            webView=itemView.findViewById(R.id.rec_vid);
            textView=itemView.findViewById(R.id.rec_video_title);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());

        }
    }
}
