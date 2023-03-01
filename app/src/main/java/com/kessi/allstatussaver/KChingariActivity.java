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

public class KChingariActivity extends AppCompatActivity {

    ImageView backBtn;
    LinearLayout kChingBtn;

    EditText linkEdt;
    TextView downloadBtn;

    ImageView help1, help2, help3, help4, help5;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kchingri);

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);
        help5 = findViewById(R.id.help5);

        Glide.with(KChingariActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.chingari0))
                .into(help1);

        Glide.with(KChingariActivity.this)
                .load(R.drawable.chingari1)
                .into(help2);

        Glide.with(KChingariActivity.this)
                .load(R.drawable.chingari2)
                .into(help3);

        Glide.with(KChingariActivity.this)
                .load(R.drawable.chingari3)
                .into(help4);

        Glide.with(KChingariActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.intro04))
                .into(help5);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(KChingariActivity.this)) {
                    if (linkEdt.getText().toString().trim().length() == 0) {
                        Toast.makeText(KChingariActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        url = linkEdt.getText().toString();
                        if (url.contains("chingari.io")) {
                            url = url.substring(url.indexOf("https://chingari.io/"),
                                    url.indexOf("For more such entertaining"));
                            new ChinAsync().execute(url);
                            linkEdt.getText().clear();
                        } else {
                            Toast.makeText(KChingariActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                        }

                        //ads
                        if (!AdManager.isloadFbAd) {
                            AdManager.adCounter++;
                            AdManager.showInterAd(KChingariActivity.this, null);
                        } else {
                            AdManager.adCounter++;
                            AdManager.showMaxInterstitial(KChingariActivity.this, null);
                        }
                    }
                }else {
                    Toast.makeText(KChingariActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        kChingBtn = findViewById(R.id.kChingBtn);
        kChingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKChingari();
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
            AdManager.initAd(KChingariActivity.this);
            AdManager.loadBannerAd(KChingariActivity.this, adContainer);
            AdManager.adptiveBannerAd(KChingariActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(KChingariActivity.this);
            AdManager.maxBanner(KChingariActivity.this, adContainer);
            AdManager.maxBannerAdaptive(KChingariActivity.this, adaptiveAdContainer);
        }


    }

    private void openKChingari() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("io.chingari.app");
            this.startActivity(i);
        } catch (Exception e) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "io.chingari.app")));
        }

    }

    class ChinAsync extends AsyncTask<String, Void, Document>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayLoader(KChingariActivity.this);
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
                        String file = "chingari" + "_" + timeStamp;
                        String ext = "mp4";
                        String fileName = file + "." + ext;

                        Utils.downloader(KChingariActivity.this, url, Utils.chinagriDirPath, fileName);

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