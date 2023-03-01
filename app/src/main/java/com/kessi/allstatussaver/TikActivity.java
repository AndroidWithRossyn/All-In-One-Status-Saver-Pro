package com.kessi.allstatussaver;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.Utils;
import com.zhkrb.cloudflare_scrape_webview.CfCallback;
import com.zhkrb.cloudflare_scrape_webview.Cloudflare;

import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TikActivity extends AppCompatActivity {
    ImageView backBtn;
    LinearLayout tikBtn;

    EditText linkEdt;
    TextView downloadBtn;

    ImageView help1, help2, help3, help4;
    String urlTik;

    WebView webViewscan;
    private static ValueCallback<Uri[]> mUploadMessageArr;
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tik);

        webViewscan = findViewById(R.id.webViewscan);


        help1 = findViewById(R.id.help1);
        help2 = findViewById(R.id.help2);
        help3 = findViewById(R.id.help3);
        help4 = findViewById(R.id.help4);

        Glide.with(TikActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.tiktok_step01))
                .into(help1);

        Glide.with(TikActivity.this)
                .load(R.drawable.tiktok_step02)
                .into(help2);

        Glide.with(TikActivity.this)
                .load(R.drawable.tiktok_step03)
                .into(help3);

        Glide.with(TikActivity.this)
                .load(ContextCompat.getDrawable(this, R.drawable.intro04))
                .into(help4);


        linkEdt = findViewById(R.id.linkEdt);
        downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadClick();
            }
        });


        tikBtn = findViewById(R.id.tikBtn);
        tikBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTok();
            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        Tik download


        LinearLayout adContainer = findViewById(R.id.banner_container);
        LinearLayout adaptiveAdContainer = findViewById(R.id.banner_adaptive_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(TikActivity.this);
            AdManager.loadBannerAd(TikActivity.this, adContainer);
            AdManager.adptiveBannerAd(TikActivity.this, adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(TikActivity.this);
            AdManager.maxBanner(TikActivity.this, adContainer);
            AdManager.maxBannerAdaptive(TikActivity.this, adaptiveAdContainer);
        }

    }


    private void downloadClick() {
        if (Utils.isNetworkAvailable(TikActivity.this)) {
            if (linkEdt.getText().toString().trim().length() == 0) {
                Toast.makeText(TikActivity.this, "Please paste url and download!!!!", Toast.LENGTH_SHORT).show();
            } else {
                urlTik = linkEdt.getText().toString();
                linkEdt.getText().clear();
                if (urlTik.contains("tiktok")) {
                    Log.e("tiktok", urlTik);
                    init();
                } else {
                    Toast.makeText(TikActivity.this, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                }

                //ads
                if (!AdManager.isloadFbAd) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(TikActivity.this, null);
                } else {
                    AdManager.adCounter++;
                    AdManager.showMaxInterstitial(TikActivity.this, null);
                }

            }
        } else {
            Toast.makeText(TikActivity.this, "Internet Connection not available!!!!", Toast.LENGTH_SHORT).show();
        }
    }


    private void openTok() {
        try {
            Intent i = this.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            this.startActivity(i);
        } catch (Exception var4) {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "com.zhiliaoapp.musically")));
        }

    }

    void init() {
        InitHandler();

        Utils.displayLoader(TikActivity.this);


        if (Build.VERSION.SDK_INT >= 24) {

            webViewscan.clearFormData();
            webViewscan.getSettings().setSaveFormData(true);
            // webViewscan.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
            webViewscan.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            // webViewscan.setWebChromeClient(new webChromeClients());
            webViewscan.setWebViewClient(new MyBrowser());

            webViewscan.getSettings().setAllowFileAccess(true);

            webViewscan.getSettings().setJavaScriptEnabled(true);
            webViewscan.getSettings().setDefaultTextEncodingName("UTF-8");
            webViewscan.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webViewscan.getSettings().setDatabaseEnabled(true);
            webViewscan.getSettings().setBuiltInZoomControls(false);
            webViewscan.getSettings().setSupportZoom(true);
            webViewscan.getSettings().setUseWideViewPort(true);
            webViewscan.getSettings().setDomStorageEnabled(true);
            webViewscan.getSettings().setAllowFileAccess(true);
            webViewscan.getSettings().setLoadWithOverviewMode(true);
            webViewscan.getSettings().setLoadsImagesAutomatically(true);
            webViewscan.getSettings().setBlockNetworkImage(false);
            webViewscan.getSettings().setBlockNetworkLoads(false);
            webViewscan.getSettings().setLoadWithOverviewMode(true);


            webViewscan.setWebChromeClient(new WebChromeClient() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    request.grant(request.getResources());
                }
            });
            webViewscan.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {

                    String nametitle = "Tiktok_video_" +
                            System.currentTimeMillis();

//                    DownloadFileMain.startDownloading(TikTokDownloadCloudBypassWebview_method_7.this, url, nametitle, ".mp4");

                    String timeStamp = String.valueOf(System.currentTimeMillis());
                    String file = "Tiktok" + "_" + timeStamp;
                    String ext = "mp4";
                    String fileName = file + "." + ext;

                    Utils.downloader(TikActivity.this, url, Utils.tiktokDirPath, fileName);

                }
            });

            webViewscan.setWebChromeClient(new WebChromeClient() {

                public void onProgressChanged(WebView view, int progress) {
//                    if (progress < 100 && progressBar.getVisibility() == View.GONE) {
//                        progressBar.setVisibility(View.VISIBLE);
//
//                    }
//
//                    progressBar.setProgress(progress);
//                    if (progress == 100) {
//                        progressBar.setVisibility(View.GONE);
//
//                    }

                    if (progress < 100 && !Utils.alertDialog.isShowing()) {
                        Utils.displayLoader(TikActivity.this);

                    }
//
//                    progressBar.setProgress(progress);
                    if (progress == 100) {
                        Utils.dismissLoader();

                    }
                }
            });

            try {
                Cloudflare cf = new Cloudflare(TikActivity.this, "http://tikdd.infusiblecoder.com/");
                //   cf.setUser_agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
                cf.setUser_agent(webViewscan.getSettings().getUserAgentString());
                cf.setCfCallback(new CfCallback() {
                    @Override
                    public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {

                        webViewscan.loadUrl(newUrl);


                    }

                    @Override
                    public void onFail(int code, String msg) {
                        //  Toast.makeText(TikTokDownloadCloudBypassWebview_method_3.this, "" + msg, Toast.LENGTH_SHORT).show();

                        webViewscan.loadUrl("http://tikdd.infusiblecoder.com/");

                    }
                });
                cf.getCookies();
            } catch (Exception e) {
                e.printStackTrace();
                webViewscan.loadUrl("http://tikdd.infusiblecoder.com/");

            }
        } else {

            webViewscan.clearFormData();
            webViewscan.getSettings().setSaveFormData(true);
            //  webViewscan.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
            webViewscan.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            //   webViewscan.setWebChromeClient(new webChromeClients());
            webViewscan.setWebViewClient(new MyBrowser());

            webViewscan.getSettings().setAllowFileAccess(true);

            webViewscan.getSettings().setJavaScriptEnabled(true);
            webViewscan.getSettings().setDefaultTextEncodingName("UTF-8");
            webViewscan.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webViewscan.getSettings().setDatabaseEnabled(true);
            webViewscan.getSettings().setBuiltInZoomControls(false);
            webViewscan.getSettings().setSupportZoom(false);
            webViewscan.getSettings().setUseWideViewPort(true);
            webViewscan.getSettings().setDomStorageEnabled(true);
            webViewscan.getSettings().setAllowFileAccess(true);
            webViewscan.getSettings().setLoadWithOverviewMode(true);
            webViewscan.getSettings().setLoadsImagesAutomatically(true);
            webViewscan.getSettings().setBlockNetworkImage(false);
            webViewscan.getSettings().setBlockNetworkLoads(false);
            webViewscan.getSettings().setLoadWithOverviewMode(true);
            webViewscan.setWebChromeClient(new WebChromeClient() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    request.grant(request.getResources());
                }
            });

            webViewscan.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {

                    String nametitle = "Tiktok_video_" +
                            System.currentTimeMillis();

//                    DownloadFileMain.startDownloading(TikTokDownloadCloudBypassWebview_method_7.this, url, nametitle, ".mp4");


                    String timeStamp = String.valueOf(System.currentTimeMillis());
                    String file = "Tiktok" + "_" + timeStamp;
                    String ext = "mp4";
                    String fileName = file + "." + ext;

                    Utils.downloader(TikActivity.this, url, Utils.tiktokDirPath, fileName);

                }
            });

            webViewscan.setWebChromeClient(new WebChromeClient() {

                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100 && !Utils.alertDialog.isShowing()) {
                        Utils.displayLoader(TikActivity.this);

                    }
//
//                    progressBar.setProgress(progress);
                    if (progress == 100) {
                        Utils.dismissLoader();

                    }
                }
            });

            try {
                Cloudflare cf = new Cloudflare(TikActivity.this, "http://tikdd.infusiblecoder.com/");
                //   cf.setUser_agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
                cf.setUser_agent(webViewscan.getSettings().getUserAgentString());
                cf.setCfCallback(new CfCallback() {
                    @Override
                    public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {

                        webViewscan.loadUrl(newUrl);


                    }

                    @Override
                    public void onFail(int code, String msg) {
                        //  Toast.makeText(TikTokDownloadCloudBypassWebview_method_3.this, "" + msg, Toast.LENGTH_SHORT).show();

                        webViewscan.loadUrl("http://tikdd.infusiblecoder.com/");

                    }
                });
                cf.getCookies();
            } catch (Exception e) {
                e.printStackTrace();
                webViewscan.loadUrl("http://tikdd.infusiblecoder.com/");

            }
        }

    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001 && Build.VERSION.SDK_INT >= 21) {
            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(i2, intent));
            mUploadMessageArr = null;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = true;
        if (keyCode == 4) {
            try {
                if (webViewscan.canGoBack()) {
                    webViewscan.goBack();
                    return z;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finishAndDismiss();
        z = super.onKeyDown(keyCode, event);
        return z;
    }

    @SuppressLint({"WrongConstant"})
    @RequiresApi(api = 21)
    public void onBackPressed() {
        finishAndDismiss();

        super.onBackPressed();

    }

    public void onPause() {
        super.onPause();
        try {
            webViewscan.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            webViewscan.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStop() {
        try {
            webViewscan.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    public boolean onSupportNavigateUp() {
        finishAndDismiss();
        return true;
    }

    @SuppressLint({"HandlerLeak"})
    private void InitHandler() {
        handler = new btnInitHandlerListner();
    }

    @SuppressLint("HandlerLeak")
    private class btnInitHandlerListner extends Handler {
        @SuppressLint({"SetTextI18n"})
        public void handleMessage(Message msg) {
        }
    }


    private class MyBrowser extends WebViewClient {
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Utils.displayLoader(TikActivity.this);
//            Log.e(TAG, "progressBar");
            super.onPageStarted(view, url, favicon);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            Log.e(TAG, "progressBar GONE");
            Utils.dismissLoader();


            String jsscript = "javascript:(function() { "

                    + "document.getElementById(\"inputboxurl\").value ='" + urlTik + "';"
                    + "document.getElementsByTagName('button')[0].click();"
                    //    + "await new Promise(resolve => setTimeout(resolve, 3000)); "
                    //  + "javascript:document.getElementsByClassName(\"pure-button pure-button-primary is-center u-bl dl-button download_link without_watermark_direct\").click(); "
                    + "})();";

            view.evaluateJavascript(jsscript, new ValueCallback() {
                public void onReceiveValue(Object obj) {
//                    Log.e(TAG, "progressBar reciveing data " + obj.toString());


                }
            });
            try {


                Handler handler1 = new Handler();

                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        Log.e(TAG, "progressBar reciveing data executed 1");


                        //    webViewscan.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('audio')[0].getAttribute(\"src\"));");


                        view.evaluateJavascript("javascript:document.getElementsByTagName('a')[0].getAttribute('href')", new ValueCallback() {
                            public void onReceiveValue(Object obj) {
//                                Log.e(TAG, "progressBar reciveing data download " + obj.toString());
                                if (obj.toString() != null && obj.toString().contains("http")) {
//                                    Log.e(TAG, "progressBar reciveing data http " + obj.toString());

                                    handler1.removeCallbacksAndMessages(null);

//                                    if (!isdownloadstarted) {
//                                        DownloadFileMain.startDownloading(TikTokDownloadCloudBypassWebview_method_7.this, obj.toString(), "Tiktok_" + System.currentTimeMillis(), ".mp4");
//                                        isdownloadstarted = true;
//
//
//                                    }

                                    String timeStamp = String.valueOf(System.currentTimeMillis());
                                    String file = "Tiktok" + "_" + timeStamp;
                                    String ext = "mp4";
                                    String fileName = file + "." + ext;

                                    Utils.downloader(TikActivity.this, obj.toString().replace("\"", ""), Utils.tiktokDirPath, fileName);


                                    //  startActivity(new Intent(TikTokDownloadWebview.this, MainActivity.class));

                                    finishAndDismiss();
                                }


                            }
                        });

                        handler1.postDelayed(this, 2000);

                    }
                }, 2000);
            } catch (Exception e) {
                finishAndDismiss();
            }


        }
    }

    void finishAndDismiss() {
        try {
            Utils.dismissLoader();
//            finish();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }




}
