package com.kessi.allstatussaver;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.kessi.allstatussaver.utils.LkSupport;
import com.kessi.allstatussaver.utils.Utils;

public class KLikeActivity extends AppCompatActivity {
    ImageView backBtn;
    LinearLayout tikBtn;

    EditText linkEdt;
    TextView downloadBtn;

    ImageView help1, help2, help3, help4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klike);


        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(KLikeActivity.this)
                .load(ContextCompat.getDrawable(this,R.drawable.like_help1))
                .into(help1);

        Glide.with(KLikeActivity.this)
                .load(R.drawable.likee_help2)
                .into(help2);

        Glide.with(KLikeActivity.this)
                .load(R.drawable.likee_help3)
                .into(help3);

        Glide.with(KLikeActivity.this)
                .load(ContextCompat.getDrawable(this,R.drawable.intro04))
                .into(help4);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkAvailable(KLikeActivity.this)) {
                    if (linkEdt.getText().toString().trim().length() == 0) {
                        Toast.makeText(KLikeActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {

                        //ads
                        if (!AdManager.isloadFbAd) {
                            AdManager.adCounter++;
                            AdManager.showInterAd(KLikeActivity.this, null);
                        } else {
                            AdManager.adCounter++;
                            AdManager.showMaxInterstitial(KLikeActivity.this, null);
                        }

                        final String url = linkEdt.getText().toString();
                        if (url.contains("likee")) {
                            Log.e("likee", url);
                            LkSupport.context = KLikeActivity.this;
                            new LkSupport.PreProcess().execute(url);
                            linkEdt.getText().clear();
                        } else {
                            Toast.makeText(KLikeActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(KLikeActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tikBtn = findViewById(R.id.tweatBtn);
        tikBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openLikee();


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
            AdManager.initAd(KLikeActivity.this);
            AdManager.loadBannerAd(KLikeActivity.this, adContainer);
            AdManager.adptiveBannerAd(KLikeActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(KLikeActivity.this);
            AdManager.maxBannerAdaptive(KLikeActivity.this, adaptiveAdContainer);
            AdManager.maxInterstital(KLikeActivity.this);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }



    private void openLikee() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("video.like");
            this.startActivity(i);
        } catch (Exception var4) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "video.like")));
        }

    }


}
