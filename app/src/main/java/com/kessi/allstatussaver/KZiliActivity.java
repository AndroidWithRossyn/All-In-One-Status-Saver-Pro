package com.kessi.allstatussaver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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


public class KZiliActivity extends AppCompatActivity {

    ImageView backBtn;
    LinearLayout kZiliBtn;

    EditText linkEdt;
    TextView downloadBtn;

    ImageView help1, help2, help3, help4;
    String url;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kzili);

        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(KZiliActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.zili01))
                .into(help1);

        Glide.with(KZiliActivity.this)
                .load(R.drawable.zili1)
                .into(help2);

        Glide.with(KZiliActivity.this)
                .load(R.drawable.zili2)
                .into(help3);

        Glide.with(KZiliActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.intro04))
                .into(help4);

        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(KZiliActivity.this)) {
                    if (linkEdt.getText().toString().trim().length() == 0) {
                        Toast.makeText(KZiliActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        url = linkEdt.getText().toString();
                        if (url.contains("zili")) {
                            startLoad();
                            linkEdt.getText().clear();
                        } else {
                            Toast.makeText(KZiliActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                        }

                        //ads
                        if (!AdManager.isloadFbAd) {
                            AdManager.adCounter++;
                            AdManager.showInterAd(KZiliActivity.this, null);
                        } else {
                            AdManager.adCounter++;
                            AdManager.showMaxInterstitial(KZiliActivity.this, null);
                        }
                    }
                }else {
                    Toast.makeText(KZiliActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        kZiliBtn = findViewById(R.id.kZiliBtn);
        kZiliBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKZili();
            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        webView = findViewById(R.id.webView);


        LinearLayout adContainer = findViewById(R.id.banner_container);
        LinearLayout adaptiveAdContainer = findViewById(R.id.banner_adaptive_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(KZiliActivity.this);
            AdManager.loadBannerAd(KZiliActivity.this, adContainer);
            AdManager.adptiveBannerAd(KZiliActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(KZiliActivity.this);
            AdManager.maxBannerAdaptive(KZiliActivity.this, adaptiveAdContainer);
            AdManager.maxInterstital(KZiliActivity.this);
        }

    }

    private void openKZili() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("com.funnypuri.client");
            this.startActivity(i);
        } catch (Exception e) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "com.funnypuri.client")));
        }

    }

    void startLoad(){
        Utils.displayLoader(KZiliActivity.this);
        if (url.contains("audiomack")) {
            String[] urlarray = url.split("/");
            url = "https://audiomack.com/embed/song/" + urlarray[3] + "/" + urlarray[5] + "?background=1";
        }
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
        webView.addJavascriptInterface(new ScriptInterface(), "HTMLOUT");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getAllowFileAccess();
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:window.HTMLOUT.download(''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");
                    }
                },1000);
            }
        });
        webView.loadUrl(url);
    }

    class ScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void download(final String videoUrl) {
            Utils.dismissLoader();
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String file = "zili" + "_" + timeStamp;
            String ext = "mp4";
            String fileName = file + "." + ext;

            Utils.downloader(KZiliActivity.this, videoUrl, Utils.ziliDirPath, fileName);

            url = "";
        }
    }
}