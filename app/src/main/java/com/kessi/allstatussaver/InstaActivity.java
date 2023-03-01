package com.kessi.allstatussaver;

import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.InstaDownload;
import com.kessi.allstatussaver.utils.Utils;

public class InstaActivity extends AppCompatActivity {
    ImageView backBtn;
    LinearLayout instaBtn;

    EditText linkEdt;
    TextView downloadBtn;
    ImageView help1, help2, help3, help4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(InstaActivity.this)
                .load(ContextCompat.getDrawable(this,R.drawable.intro01))
                .into(help1);

        Glide.with(InstaActivity.this)
                .load(R.drawable.intro02)
                .into(help2);

        Glide.with(InstaActivity.this)
                .load(R.drawable.intro03)
                .into(help3);

        Glide.with(InstaActivity.this)
                .load(ContextCompat.getDrawable(this,R.drawable.intro04))
                .into(help4);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(InstaActivity.this)) {
                    if (linkEdt.getText().toString().trim().length() == 0) {
                        Toast.makeText(InstaActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        final String url = linkEdt.getText().toString();

                        if (!Patterns.WEB_URL.matcher(url).matches() && !url.contains("instagram")) {
                            Toast.makeText(InstaActivity.this, R.string.invalid, Toast.LENGTH_SHORT).show();
                        } else {
                            InstaDownload.INSTANCE.startInstaDownload(url, InstaActivity.this);
                            linkEdt.getText().clear();
                        }

                        //ads
                        if (!AdManager.isloadFbAd) {
                            AdManager.adCounter++;
                            AdManager.showInterAd(InstaActivity.this, null);
                        } else {
                            AdManager.adCounter++;
                            AdManager.showMaxInterstitial(InstaActivity.this, null);
                        }
                    }
                }else {
                    Toast.makeText(InstaActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }
            }


        });


        instaBtn = findViewById(R.id.instaBtn);
        instaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchInstagram();


            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayout adContainer = findViewById(R.id.banner_container);
        LinearLayout adaptiveAdContainer = findViewById(R.id.banner_adaptive_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(InstaActivity.this);
            AdManager.loadBannerAd(InstaActivity.this, adContainer);
            AdManager.adptiveBannerAd(InstaActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(InstaActivity.this);
            AdManager.maxBannerAdaptive(InstaActivity.this, adaptiveAdContainer);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void launchInstagram() {
        String instagramApp = "com.instagram.android";
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(instagramApp);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.instagram_not_found, Toast.LENGTH_SHORT).show();
        }
    }



}
