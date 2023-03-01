package com.kessi.allstatussaver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.kessi.allstatussaver.utils.KMoSupport;
import com.kessi.allstatussaver.utils.Utils;


public class KMojActivity extends AppCompatActivity {

    ImageView backBtn;
    LinearLayout kMojBtn;

    EditText linkEdt;
    TextView downloadBtn;

    ImageView help1, help2, help3, help4;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kmoj);

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(KMojActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.moj0))
                .into(help1);

        Glide.with(KMojActivity.this)
                .load(R.drawable.moj1)
                .into(help2);

        Glide.with(KMojActivity.this)
                .load(R.drawable.moj2)
                .into(help3);

        Glide.with(KMojActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.intro04))
                .into(help4);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(KMojActivity.this)) {
                    if (linkEdt.getText().toString().trim().length() == 0) {
                        Toast.makeText(KMojActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        url = linkEdt.getText().toString();
                        if (url.contains("moj")) {
                            KMoSupport.KMojDownload(KMojActivity.this, url);
                            linkEdt.getText().clear();
                        } else {
                            Toast.makeText(KMojActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                        }

                        //ads
                        if (!AdManager.isloadFbAd) {
                            AdManager.adCounter++;
                            AdManager.showInterAd(KMojActivity.this, null);
                        } else {
                            AdManager.adCounter++;
                            AdManager.showMaxInterstitial(KMojActivity.this, null);
                        }
                    }
                }else {
                    Toast.makeText(KMojActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        kMojBtn = findViewById(R.id.kMojBtn);
        kMojBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKMoj();
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
            AdManager.initAd(KMojActivity.this);
            AdManager.loadBannerAd(KMojActivity.this, adContainer);
            AdManager.adptiveBannerAd(KMojActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(KMojActivity.this);
            AdManager.maxBannerAdaptive(KMojActivity.this, adaptiveAdContainer);
            AdManager.maxInterstital(KMojActivity.this);
        }

    }

    private void openKMoj() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("in.mohalla.video");
            this.startActivity(i);
        } catch (Exception e) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "in.mohalla.video")));
        }

    }
}