package com.adnan.lgstudies.Activity;

import static com.adnan.lgstudies.Adapters.NetworkUtils.isNetworkAvailable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.adnan.lgstudies.Adapters.AdsUnit;
import com.adnan.lgstudies.Adapters.CustomWebChromeClient;
import com.adnan.lgstudies.Adapters.NetworkUtils;
import com.adnan.lgstudies.Fragments.CommentFragment;
import com.adnan.lgstudies.R;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Class_Play_Activity extends AppCompatActivity {

    /* -------------------- VARIABLES -------------------- */

    private WebView webView;
    private TextView topic_title;
    private ImageView whatsapp, comment, document;

    private String video_Id;
    private String mobile;

    private RewardedAd rewardedAd;
    private Handler adReloadHandler;

    private DatabaseReference check_batch;
    private final long adReloadInterval = 5 * 60 * 1000;

    /* -------------------- ACTIVITY -------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prevent screenshots / recording
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(R.layout.activity_class_play);

        initViews();
        getIntentData();
        setupWebView();
        setupFirebaseCheck();
        setupNetworkCheck();
        setupCommentFragment();
        setupButtons();
    }

    /* -------------------- INITIALIZATION -------------------- */

    private void initViews() {
        webView = findViewById(R.id.webView);
        topic_title = findViewById(R.id.topic_title);
        whatsapp = findViewById(R.id.share);
        comment = findViewById(R.id.comment_icon);
        document = findViewById(R.id.document_icon);
    }

    private void getIntentData() {
        video_Id = getIntent().getStringExtra("video_id");
        topic_title.setText(getIntent().getStringExtra("topic_title"));

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        mobile = preferences.getString("login", null);
    }

    /* -------------------- WEBVIEW -------------------- */

    private void setupWebView() {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // SINGLE WebViewClient (IMPORTANT)
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false; // allow loading
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                playVideoFromAndroid(video_Id);
            }
        });

        // SINGLE WebChromeClient (BEST FULLSCREEN)
        webView.setWebChromeClient(new CustomWebChromeClient(this));

        webView.loadUrl("https://lachitglobalstudies.kesug.com/player.html");
    }

    private void playVideoFromAndroid(String videoId) {
        webView.evaluateJavascript(
                "playFromAndroid('" + videoId + "');",
                null
        );
    }

    /* -------------------- FIREBASE + ADS -------------------- */

    private void setupFirebaseCheck() {
        check_batch = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(mobile)
                .child("Courses");

        check_batch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    adReloadHandler = new Handler(Looper.getMainLooper());
                    scheduleAdReload();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, AdsUnit.rewardedId, adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        showAd();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        loadRewardedAd();
                    }
                });
    }

    private void showAd() {
        rewardedAd.show(this, rewardItem -> {});

        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {}

            @Override
            public void onAdDismissedFullScreenContent() {}
        });
    }

    private void scheduleAdReload() {
        adReloadHandler.postDelayed(() -> {
            loadRewardedAd();
            scheduleAdReload();
        }, adReloadInterval);
    }

    /* -------------------- UI HELPERS -------------------- */

    private void setupNetworkCheck() {
        if (!isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetSnackbar(findViewById(android.R.id.content));
        }
    }

    private void setupCommentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_fragment, new CommentFragment());
        ft.commit();
    }

    private void setupButtons() {
        whatsapp.setOnClickListener(v -> shareApp());
        comment.setOnClickListener(v -> setupCommentFragment());
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this awesome learning app: https://play.google.com/store/apps/details?id=com.adnan.lachitgs"
        );
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    /* -------------------- LIFECYCLE -------------------- */

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adReloadHandler != null) {
            adReloadHandler.removeCallbacksAndMessages(null);
        }

        if (webView != null) {
            webView.destroy();
        }
    }
}
