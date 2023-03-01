package com.kessi.allstatussaver;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.kessi.allstatussaver.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class KRopoActivity extends AppCompatActivity {

    ImageView backBtn;
    LinearLayout kRopoBtn;

    EditText linkEdt;
    TextView downloadBtn;

    ImageView help1, help2, help3, help4;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kropo);

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(KRopoActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.roposo01))
                .into(help1);

        Glide.with(KRopoActivity.this)
                .load(R.drawable.roposo02)
                .into(help2);

        Glide.with(KRopoActivity.this)
                .load(R.drawable.roposo03)
                .into(help3);

        Glide.with(KRopoActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.intro04))
                .into(help4);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(KRopoActivity.this)) {
                    if (linkEdt.getText().toString().trim().length() == 0) {
                        Toast.makeText(KRopoActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        url = linkEdt.getText().toString();
                        if (url.contains("roposo.com")) {
                            new RopoAsync().execute(url);
                            linkEdt.getText().clear();
                        } else {
                            Toast.makeText(KRopoActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                        }

                        //ads
                        if (!AdManager.isloadFbAd) {
                            AdManager.adCounter++;
                            AdManager.showInterAd(KRopoActivity.this, null);
                        } else {
                            AdManager.adCounter++;
                            AdManager.showMaxInterstitial(KRopoActivity.this, null);
                        }
                    }
                }else {
                    Toast.makeText(KRopoActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        kRopoBtn = findViewById(R.id.kRopoBtn);
        kRopoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKRoposo();
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
            AdManager.initAd(KRopoActivity.this);
            AdManager.loadBannerAd(KRopoActivity.this, adContainer);
            AdManager.adptiveBannerAd(KRopoActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(KRopoActivity.this);
            AdManager.maxBannerAdaptive(KRopoActivity.this, adaptiveAdContainer);
            AdManager.maxInterstital(KRopoActivity.this);
        }

    }

    private void openKRoposo() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("com.roposo.android");
            this.startActivity(i);
        } catch (Exception e) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "com.roposo.android")));
        }

    }

    class RopoAsync extends AsyncTask<String, Void, Document>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayLoader(KRopoActivity.this);
        }

        Document document;
        @Override
        protected Document doInBackground(String... strings) {
            try {
                document = Jsoup.connect(strings[0]).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            Utils.dismissLoader();
            try
            {
                url = document.select("meta[property=\"og:video\"]").last().attr("content");
                if (!url.equals("")) {

                    try {

                        String timeStamp = String.valueOf(System.currentTimeMillis());
                        String file = "roposo" + "_" + timeStamp;
                        String ext = "mp4";
                        String fileName = file + "." + ext;

                        Utils.downloader(KRopoActivity.this, url, Utils.roposoDirPath, fileName);

                        url = "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}