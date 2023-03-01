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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.Utils;

import org.json.JSONObject;

public class KMXActivity extends AppCompatActivity {
    ImageView backBtn;
    LinearLayout mxBtn;

    EditText linkEdt;
    TextView downloadBtn;

    ImageView help1, help2, help3, help4;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kmx);


        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(KMXActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.mx_takatak0))
                .into(help1);

        Glide.with(KMXActivity.this)
                .load(R.drawable.mx_takatak1)
                .into(help2);

        Glide.with(KMXActivity.this)
                .load(R.drawable.mx_takatak2)
                .into(help3);

        Glide.with(KMXActivity.this)
                .load(ContextCompat.getDrawable(this,R.drawable.intro04))
                .into(help4);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadClick();
            }
        });



        mxBtn = findViewById(R.id.mxBtn);
        mxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTakatak();
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
            AdManager.initAd(KMXActivity.this);
            AdManager.loadBannerAd(KMXActivity.this, adContainer);
            AdManager.adptiveBannerAd(KMXActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(KMXActivity.this);
            AdManager.maxBannerAdaptive(KMXActivity.this, adaptiveAdContainer);
            AdManager.maxInterstital(KMXActivity.this);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void downloadClick(){
        if (Utils.isNetworkAvailable(KMXActivity.this)) {
            if (linkEdt.getText().toString().trim().length() == 0) {
                Toast.makeText(KMXActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
            } else {
                url = linkEdt.getText().toString();
                if (url.contains("mxtakatak")) {

                    Utils.displayLoader(KMXActivity.this);
                    AndroidNetworking.post(getString(R.string.mx_takatak_server))
                            .addBodyParameter("takatakurl", url)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    String downloadUrl;
                                    try {

                                        JSONObject jsonObject = new JSONObject(response.toString());

                                        downloadUrl = jsonObject.getString("videourl");

                                        Utils.dismissLoader();

                                        String timeStamp = String.valueOf(System.currentTimeMillis());
                                        String file = "MX_Takatak_" + "_" + timeStamp;
                                        String ext = "mp4";
                                        String fileName = file + "." + ext;

                                        Utils.downloader(KMXActivity.this, downloadUrl, Utils.mxTakaTakDirPath, fileName);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Utils.dismissLoader();
                                    }


                                }

                                @Override
                                public void onError(ANError error) {
                                    Utils.dismissLoader();
                                }
                            });
                    linkEdt.getText().clear();
                } else {
                    Toast.makeText(KMXActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                }

                //ads
                if (!AdManager.isloadFbAd) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(KMXActivity.this, null);
                } else {
                    AdManager.adCounter++;
                    AdManager.showMaxInterstitial(KMXActivity.this, null);
                }
            }
        }else {
            Toast.makeText(KMXActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
        }
    }


    private void openTakatak() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("com.next.innovation.takatak");
            this.startActivity(i);
        } catch (Exception var4) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "com.next.innovation.takatak")));
        }

    }


}
