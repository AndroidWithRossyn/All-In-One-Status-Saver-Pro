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

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class KMitroActivity extends AppCompatActivity {

    ImageView backBtn;
    LinearLayout kMitronBtn;

    EditText linkEdt;
    TextView downloadBtn;

    ImageView help1, help2, help3, help4, help5;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kmitro);

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);
        help5 = findViewById(R.id.help5);

        Glide.with(KMitroActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.mitron01))
                .into(help1);

        Glide.with(KMitroActivity.this)
                .load(R.drawable.mitron1)
                .into(help2);

        Glide.with(KMitroActivity.this)
                .load(R.drawable.mitron2)
                .into(help3);

        Glide.with(KMitroActivity.this)
                .load(R.drawable.mitron3)
                .into(help4);

        Glide.with(KMitroActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.intro04))
                .into(help5);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(KMitroActivity.this)) {
                    if (linkEdt.getText().toString().trim().length() == 0) {
                        Toast.makeText(KMitroActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        url = linkEdt.getText().toString();
                        if (url.contains("mitron.tv")) {
                            new MitroAsync().execute(url);
                            linkEdt.getText().clear();
                        } else {
                            Toast.makeText(KMitroActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                        }

                        //ads
                        if (!AdManager.isloadFbAd) {
                            AdManager.adCounter++;
                            AdManager.showInterAd(KMitroActivity.this, null);
                        } else {
                            AdManager.adCounter++;
                            AdManager.showMaxInterstitial(KMitroActivity.this, null);
                        }
                    }
                }else {
                    Toast.makeText(KMitroActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        kMitronBtn = findViewById(R.id.kMitronBtn);
        kMitronBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKMitron();
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
            AdManager.initAd(KMitroActivity.this);
            AdManager.loadBannerAd(KMitroActivity.this, adContainer);
            AdManager.adptiveBannerAd(KMitroActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(KMitroActivity.this);
            AdManager.maxBanner(KMitroActivity.this, adContainer);
            AdManager.maxBannerAdaptive(KMitroActivity.this, adaptiveAdContainer);
        }

    }


    private void openKMitron() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("com.mitron.tv");
            this.startActivity(i);
        } catch (Exception e) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "com.mitron.tv")));
        }

    }

    class MitroAsync extends AsyncTask<String, Void, Document>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayLoader(KMitroActivity.this);
        }

        Document document;
        @Override
        protected Document doInBackground(String... strings) {
            String splitUrl;
            try {
                String url = strings[0];
                if (url.contains("api.mitron.tv")) {
                    String[] split = url.split("=");
                    splitUrl = "https://web.mitron.tv/video/" + split[split.length - 1];
                } else {
                    splitUrl = strings[0];
                }
                document = Jsoup.connect(splitUrl).get();
            } catch (IOException e) {
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
                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();
                if (!html.equals("")) {
                    url = String.valueOf(new JSONObject(html).getJSONObject("props").getJSONObject("pageProps").getJSONObject("video").get("videoUrl"));

                    if (!url.equals("")) {

                        try {

                            String timeStamp = String.valueOf(System.currentTimeMillis());
                            String file = "mitron" + "_" + timeStamp;
                            String ext = "mp4";
                            String fileName = file + "." + ext;

                            Utils.downloader(KMitroActivity.this, url, Utils.mitronDirPath, fileName);

                            url = "";


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}