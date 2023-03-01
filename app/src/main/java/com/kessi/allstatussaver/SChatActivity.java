package com.kessi.allstatussaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import com.bumptech.glide.Glide;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SChatActivity extends AppCompatActivity {

    ImageView backBtn;
    LinearLayout sChatBtn;

    EditText linkEdt;
    TextView downloadBtn;

    ImageView help1, help2, help3, help4;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_chat);

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(SChatActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.schat1))
                .into(help1);

        Glide.with(SChatActivity.this)
                .load(R.drawable.schat2)
                .into(help2);

        Glide.with(SChatActivity.this)
                .load(R.drawable.schat3)
                .into(help3);

        Glide.with(SChatActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.intro04))
                .into(help4);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(SChatActivity.this)) {
                    if (linkEdt.getText().toString().trim().length() == 0) {
                        Toast.makeText(SChatActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        url = linkEdt.getText().toString();
                        if (url.contains("sharechat.com")) {
                            new SChatAsync().execute(url);
                            linkEdt.getText().clear();
                        } else {
                            Toast.makeText(SChatActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                        }

                        //ads
                        if (!AdManager.isloadFbAd) {
                            AdManager.adCounter++;
                            AdManager.showInterAd(SChatActivity.this, null);
                        } else {
                            AdManager.adCounter++;
                            AdManager.showMaxInterstitial(SChatActivity.this, null);
                        }
                    }
                }else {
                    Toast.makeText(SChatActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        sChatBtn = findViewById(R.id.sChatBtn);
        sChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSChat();
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
            AdManager.initAd(SChatActivity.this);
            AdManager.loadBannerAd(SChatActivity.this, adContainer);
            AdManager.adptiveBannerAd(SChatActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(SChatActivity.this);
            AdManager.maxBannerAdaptive(SChatActivity.this, adaptiveAdContainer);
            AdManager.maxInterstital(SChatActivity.this);
        }

    }

    private void openSChat() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("in.mohalla.sharechat");
            this.startActivity(i);
        } catch (Exception e) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "in.mohalla.sharechat")));
        }

    }

    class SChatAsync extends AsyncTask<String, Void, Document> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayLoader(SChatActivity.this);
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
            try {
                url = document.select("meta[property=\"og:video\"]").last().attr("content");
                if (!url.equals("")) {
                    try {

                        String timeStamp = String.valueOf(System.currentTimeMillis());
                        String file = "schat" + "_" + timeStamp;
                        String ext = "mp4";
                        String fileName = file + "." + ext;

                        Utils.downloader(SChatActivity.this, url, Utils.sChatDirPath, fileName);

                        url = "";

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SChatActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SChatActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}