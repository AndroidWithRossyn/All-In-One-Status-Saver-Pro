package com.kessi.allstatussaver.model;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.kessi.allstatussaver.interfaces.VideoDownloader;
import com.kessi.allstatussaver.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class VimeoVideoDownloader implements VideoDownloader {

    private Context context;
    private String VideoURL;
    private String VideoTitle;

    public VimeoVideoDownloader(Context context, String videoURL) {
        this.context = context;
        VideoURL = videoURL;
    }

    @Override
    public String getVideoId(String link) {
        return link;
    }

    @Override
    public void DownloadVideo() {
        new Data().execute(getVideoId(VideoURL));
    }

    @SuppressLint("StaticFieldLeak")
    private class Data extends AsyncTask<String, String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayLoader(context);
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String buffer = "No URL";
                String Line;
                while ((Line = reader.readLine()) != null)
                {
                    if(Line.contains("og:title"))
                    {
                    }
                    if(Line.contains("og:video:url"))
                    {
                        Line = Line.substring(Line.indexOf("og:video:url"));
                        Line = Line.substring(ordinalIndexOf(Line,"\"",1)+1,ordinalIndexOf(Line,"\"",2));
                        if(!Line.contains("https"))
                        {
                            Line = Line.replace("http","https");
                        }
                        boolean checkURL = URLUtil.isValidUrl(Line);
                        if(checkURL)
                        {
                            HttpURLConnection connection11;
                            BufferedReader reader1;
                            URL url1 = new URL(Line);
                            try {
                                connection11 = (HttpURLConnection) url1.openConnection();
                                connection11.connect();

                                InputStream stream1 = connection11.getInputStream();
                                reader1 = new BufferedReader(new InputStreamReader(stream1));

                                StringBuilder builder = new StringBuilder();
                                String str;
                                while ((str = reader1.readLine()) != null)
                                {
                                    builder.append(str);
                                }
                                String hello = builder.toString();
                                int Counter = countOccurences(hello,"video/mp4");
                                if(Counter>0) {
                                    for (int i = 0; i < Counter; i++) {
                                        String FinalURL = hello.substring(ordinalIndexOf(hello,"video/mp4",i));
                                        FinalURL = FinalURL.substring(FinalURL.indexOf("url")+1);
                                        FinalURL = FinalURL.substring(FinalURL.indexOf("\"")+1,FinalURL.indexOf("}"));
                                        if(FinalURL.contains("360p"))
                                        {
                                            FinalURL = FinalURL.substring(FinalURL.indexOf("\"")+1,ordinalIndexOf(FinalURL,"\"",1));
                                            Line = FinalURL;
                                            break;
                                        }
                                        else {
                                            FinalURL = FinalURL.substring(FinalURL.indexOf("\"")+1,ordinalIndexOf(FinalURL,"\"",1));
                                            Line = FinalURL;
                                        }
                                    }
                                }

                            } catch (IOException e) {
                                Line = "Wrong Video URL";
                            }
                        }
                        buffer = Line;
                        break;
                    }
                    else {
                        buffer= "Wrong Video URL";
                    }

                }
                return buffer;
            } catch (IOException e) {
                return "Invalid Video URL or Check Internet Connection";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Utils.dismissLoader();
            if(URLUtil.isValidUrl(s))
            {
                    VideoTitle = "VimeoVideo" + new Date().toString()+".mp4";
                try {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s));
                    request.allowScanningByMediaScanner();
                    request.setDescription("Downloading")
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                            .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, Utils.vimeoDirPath + VideoTitle)
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setVisibleInDownloadsUi(true)
                            .setTitle(VideoTitle);
                    DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    assert manager != null;
                    long downLoadID = manager.enqueue(request);
                    Toast.makeText(context, "Start Video Downloading....", Toast.LENGTH_SHORT).show();
                    Utils.mediaScanner(context, Utils.vimeoDirPath,VideoTitle);
                } catch (Exception e) {
                    if (Looper.myLooper()==null)
                        Looper.prepare();
                    Toast.makeText(context, "Video Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
            else {
                if (Looper.myLooper()==null)
                    Looper.prepare();
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }
    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }
    private static int countOccurences(String str, String word)
    {
        // split the string by spaces in a
        String a[] = str.split("\"");

        // search for pattern in a
        int count = 0;
        for (int i = 0; i < a.length; i++)
        {
            // if match found increase count
            if (word.equals(a[i]))
                count++;
        }

        return count;
    }

}
