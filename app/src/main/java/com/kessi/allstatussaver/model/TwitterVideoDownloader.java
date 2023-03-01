package com.kessi.allstatussaver.model;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.kessi.allstatussaver.interfaces.VideoDownloader;
import com.kessi.allstatussaver.utils.Utils;

import org.json.JSONObject;
import java.util.Date;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class TwitterVideoDownloader implements VideoDownloader {

    private Context context;
    private String VideoURL;
    private String VideoTitle;

    public TwitterVideoDownloader(Context context, String videoURL) {
        this.context = context;
        VideoURL = videoURL;
    }


    @Override
    public String getVideoId(String link) {
        if(link.contains("?"))
        {
            link = link.substring(link.indexOf("status"));
            link = link.substring(link.indexOf("/")+1,link.indexOf("?"));
        }
        else {
            link = link.substring(link.indexOf("status"));
            link = link.substring(link.indexOf("/")+1);
        }
        return link;
    }

    @Override
    public void DownloadVideo() {
        Utils.displayLoader(context);
        AndroidNetworking.post("https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php")
                .addBodyParameter("id",getVideoId(VideoURL))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String URL = response.toString();
                        if(URL.contains("url")){
                            URL = URL.substring(URL.indexOf("url"));
                            URL = URL.substring(ordinalIndexOf(URL,"\"",1)+1,ordinalIndexOf(URL,"\"",2));
                            if(URL.contains("\\"))
                            {
                                URL = URL.replace("\\","");
                            }
                            if(URLUtil.isValidUrl(URL))
                            {
                                if(VideoTitle == null || VideoTitle.equals(""))
                                {
                                    VideoTitle = "TwitterVideo" + new Date().toString()+".mp4";
                                }
                                else {
                                    VideoTitle = VideoTitle + ".mp4";
                                }
                                try {
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL));
                                    request.allowScanningByMediaScanner();
                                    request.setDescription("Downloading")
                                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                                            .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, Utils.twitterDirPath + VideoTitle)
                                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                            .setVisibleInDownloadsUi(true)
                                            .setTitle(VideoTitle);
                                    DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                                    assert manager != null;
                                    long downLoadID = manager.enqueue(request);
                                    Toast.makeText(context, "Start Video Downloading....", Toast.LENGTH_SHORT).show();
                                    Utils.mediaScanner(context, Utils.twitterDirPath,VideoTitle);

                                    Utils.dismissLoader();
                                } catch (Exception e) {
                                    if (Looper.myLooper()==null)
                                        Looper.prepare();
                                    Toast.makeText(context, "Video Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                    Utils.dismissLoader();
                                }
                            }
                            else {
                                if (Looper.myLooper()==null)
                                    Looper.prepare();
                                Toast.makeText(context, "No Video Found", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                Utils.dismissLoader();
                            }
                        }
                        else {
                            if (Looper.myLooper()==null)
                                Looper.prepare();
                            Toast.makeText(context, "No Video Found", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            Utils.dismissLoader();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (Looper.myLooper()==null)
                            Looper.prepare();
                        Toast.makeText(context, "Invalid Video URL", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Utils.dismissLoader();
                    }
                });
    }
    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }
}
