package com.kessi.allstatussaver.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonObject;
import com.kessi.allstatussaver.interfaces.SnackInterface;
import com.kessi.allstatussaver.service.RetroClient;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;


public class SVSupport {
    static String downloadUrl;
    public static Context context;
    public static class getSnackUrlAsync extends AsyncTask<String, Void, Document> {
        Document document;
        @Override
        protected void onPreExecute() {
            Utils.displayLoader(context);
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... strings) {
            try {
                document = Jsoup.connect(strings[0]).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return document;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            try {
                String data = "";
                Iterator<Element> elementIterator = document.select("script").iterator();

                do {
                    if (!elementIterator.hasNext()) {
                        break;
                    }
                    data = elementIterator.next().data();
                } while (!data.contains("window.__INITIAL_STATE__"));

                String ss = data.substring(data.indexOf("{"), data.indexOf("};")) + "}";

                if (!document.equals("")) {
                    try {
                        JSONObject object = new JSONObject(ss);
                        downloadUrl = object.getJSONObject("sharePhoto").getString("mp4Url");

                        getVideoInfoFromUrl(object.getString("shortUrl"));
                        downloadUrl = "";

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.dismissLoader();
                    }
                }


            } catch (Exception e) {
                Utils.dismissLoader();
                e.printStackTrace();
                Toast.makeText(context, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("NewApi")
    public static void getVideoInfoFromUrl(String shortUrl) {
        URI mUri;
        try {
            mUri = new URI(shortUrl);
        } catch (Exception e) {
            e.printStackTrace();
            mUri = null;
            Utils.dismissLoader();
        }
        assert mUri != null;
        String[] mPath = mUri.getPath().split("/");
        String uPath = mPath[mPath.length - 1];
        ArrayList<String> infoList = new ArrayList();
        infoList.add("mod=OnePlus(ONEPLUS A5000)");
        infoList.add("lon=0");
        infoList.add("country_code=in");
        String mydid = "did=" +
                "ANDROID_" + Settings.Secure.getString(context.getContentResolver(), "android_id");

        infoList.add(mydid);
        infoList.add("app=1");
        infoList.add("oc=UNKNOWN");
        infoList.add("egid=");
        infoList.add("ud=0");
        infoList.add("c=GOOGLE_PLAY");
        infoList.add("sys=KWAI_BULLDOG_ANDROID_9");
        infoList.add("appver=2.7.1.153");
        infoList.add("mcc=0");
        infoList.add("language=en-in");
        infoList.add("lat=0");
        infoList.add("ver=2.7");


        ArrayList dataList = new ArrayList(infoList);

        String mShortKey = "shortKey=" +
                uPath;
        dataList.add(mShortKey);

        String mMyOs = "os=" +
                "android";
        dataList.add(mMyOs);
        String client_key = "client_key=" +
                "8c46a905";
        dataList.add(client_key);

        try {
            Collections.sort(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.dismissLoader();
        }


        String mClockData = yxcorp.gifshow.util.CPU.getClockData(context, TextUtils.join("", dataList).getBytes(StandardCharsets.UTF_8), 0);

        String mVideoUrl = "https://g-api.snackvideo.com/rest/bulldog/share/get?" + TextUtils.join("&", infoList);


        downloadService(mVideoUrl, mShortKey, mMyOs, mClockData, client_key);


    }

    static void downloadService(String mVideoUrl, String mShortKey, String mMyOs, String mClockData, String client_key) {


        SnackInterface snackInterface = RetroClient.getClient().create(SnackInterface.class);


        Call<JsonObject> objectCall = snackInterface.getsnackvideoresult(mVideoUrl + "&" + mShortKey + "&" + mMyOs + "&sig=" + mClockData + "&" + client_key);


        objectCall.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {


                downloadUrl = response.body().getAsJsonObject("photo").get("main_mv_urls").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();


                if (!downloadUrl.equals("")) {
                    try {
                        Utils.dismissLoader();
                        String timeStamp = String.valueOf(System.currentTimeMillis());
                        String file = "snack" + "_" + timeStamp;
                        String ext = "mp4";
                        String fileName = file + "." + ext;
                        Utils.downloader(context, downloadUrl, Utils.snackDirPath, fileName);
                        downloadUrl = "";
                    } catch (Exception e) {
                        Utils.dismissLoader();
                        e.printStackTrace();
                        Toast.makeText(context, "Url not exists!!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Utils.dismissLoader();
            }
        });

    }

}
