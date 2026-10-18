package com.adnan.lgstudies.Adapters;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.adnan.lgstudies.R;

public class CustomWebChromeClient extends WebChromeClient {
    private View customView;
    private CustomViewCallback customViewCallback;
    private int originalOrientation;
    private ConstraintLayout fullscreenContainer;
    private LinearLayout bottom_contain;
    private Activity activity;



    public CustomWebChromeClient(Activity activity) {
        this.activity = activity;
        this.fullscreenContainer = activity.findViewById(R.id.full_screen_view_container);
       // this.cardView=activity.findViewById(R.id.all_btn_option);
        this.bottom_contain=activity.findViewById(R.id.bottom_container);


    }

    @Override
    public void onHideCustomView() {
        if (customView == null) {
            return;
        }

        fullscreenContainer.removeView(customView);
        //fullscreenContainer.setVisibility(View.GONE);
       // cardView.setVisibility(View.VISIBLE);
        bottom_contain.setVisibility(View.VISIBLE);

        customView = null;
        customViewCallback.onCustomViewHidden();

        activity.setRequestedOrientation(originalOrientation);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (customView != null) {
            onHideCustomView();

            return;
        }

        customView = view;
        originalOrientation = activity.getRequestedOrientation();
        customViewCallback = callback;

        fullscreenContainer.addView(customView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
       // cardView.setVisibility(View.GONE);
        bottom_contain.setVisibility(View.GONE);
        fullscreenContainer.setVisibility(View.VISIBLE);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
