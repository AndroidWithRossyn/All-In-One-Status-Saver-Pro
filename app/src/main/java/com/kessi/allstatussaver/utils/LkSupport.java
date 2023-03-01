package com.kessi.allstatussaver.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LkSupport {
    public static Context context;
    public static class PreProcess extends AsyncTask<String, Void, Document> {
        Document document;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayLoader(context);
        }
        @Override
        protected Document doInBackground(String... urls) {
            try {
                document = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return document;
        }
        @Override
        protected void onPostExecute(Document result) {
            Utils.dismissLoader();
            try {
                String json="";
                Matcher matcher = Pattern.compile("window\\.data \\s*=\\s*(\\{.+?\\});").matcher(result.toString());
                while (matcher.find()) {
                    json =  matcher.group().replaceFirst("window.data = ","").replace(";","");
                }
                JSONObject jsonObject = new JSONObject(json);
                String videoLink = jsonObject.getString("video_url").replace("_4","");
                if (!videoLink.equals("")) {
                    try {
                        new DownloaderAsync().execute(videoLink);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    static class DownloaderAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayLoader(context);
        }
        @Override
        protected String doInBackground(String... params) {
            int count;
            try {
                if (!Utils.downloadLikeeDir.isDirectory()){
                    Utils.downloadLikeeDir.mkdirs();
                }
                URL url = new URL(params[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                int contentLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(
                        Utils.downloadLikeeDir+"/"+ System.currentTimeMillis() + ".mp4");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/contentLength));
                    outputStream.write(data, 0, count);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Utils.dismissLoader();
            Toast.makeText(context, "Download Successfully!", Toast.LENGTH_SHORT).show();
        }

    }
}
