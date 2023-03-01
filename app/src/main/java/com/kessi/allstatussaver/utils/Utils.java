package com.kessi.allstatussaver.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.kessi.allstatussaver.R;
import com.kessi.allstatussaver.TikActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class Utils {

    public static File downloadWhatsAppDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Whatsapp");
    public static File downloadWABusiDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/WABusiness");

    public static String instaDirPath = "All Saver/Instagram/";
    public static File downloadInstaDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Instagram");

    public static String twitterDirPath = "All Saver/Twitter/";
    public static File downloadTwitterDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Twitter");

    public static String vimeoDirPath = "All Saver/Vimeo/";
    public static File downloadVimeoDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Vimeo");

    public static String tiktokDirPath = "All Saver/Tiktok/";
    public static File downloadTiktokDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Tiktok");

    public static File downloadLikeeDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Likee");

    public static String fbDirPath = "All Saver/Facebook/";
    public static File downloadFBDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Facebook");

    public static String snackDirPath = "All Saver/SnackVideo/";
    public static File downloadSnackDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/SnackVideo");

    public static String sChatDirPath = "All Saver/ShareChat/";
    public static File downloadSChatkDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/ShareChat");

    public static String roposoDirPath = "All Saver/Roposo/";
    public static File downloadRoposoDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Roposo");

    public static String chinagriDirPath = "All Saver/Chinagri/";
    public static File downloadChinagriDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Chinagri");

    public static String mojDirPath = "All Saver/Moj/";
    public static File downloadMojDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Moj");

    public static String mxTakaTakDirPath = "All Saver/MxTakaTak/";
    public static File downloadMxTakaTakDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/MxTakaTak");

    public static String mitronDirPath = "All Saver/Mitron/";
    public static File downloadMitronDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Mitron");

    public static String ziliDirPath = "All Saver/Zili/";
    public static File downloadZiliDir = new File(Environment.getExternalStorageDirectory() + "/Download/All Saver/Zili");

    private static boolean doesPackageExist(String targetPackage, Context context) {
        try {
            context.getPackageManager().getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void mPlayer(String filepath, Activity activity) {
        File file = new File(filepath);
        Uri videoURI = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getApplicationContext()
                .getPackageName() + ".provider", file);
        Intent intent;
        if (!Utils.getBack(filepath, "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            try {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(videoURI, "video/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (doesPackageExist("com.google.android.gallery3d", activity)) {
                    intent.setClassName("com.google.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                } else if (doesPackageExist("com.android.gallery3d", activity)) {
                    intent.setClassName("com.android.gallery3d", "com.android.gallery3d.app.MovieActivity");
                } else if (doesPackageExist("com.cooliris.media", activity)) {
                    intent.setClassName("com.cooliris.media", "com.cooliris.media.MovieView");
                }
                activity.startActivity(intent);
            } catch (Exception e) {
                try {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(videoURI, "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivity(intent);
                } catch (Exception e2) {
                    Toast.makeText(activity, "Sorry, Play video not working properly , try again!", Toast.LENGTH_LONG).show();
                }
            }
        } else if (!Utils.getBack(filepath, "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(videoURI, "audio/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(intent);
        } else if (!Utils.getBack(filepath, "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
            try {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(videoURI, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(intent);
            } catch (Exception e3) {
                Toast.makeText(activity, "Sorry. We can't Display Images. try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static String getBack(String paramString1, String paramString2) {
        Matcher localMatcher = Pattern.compile(paramString2).matcher(paramString1);
        if (localMatcher.find()) {
            return localMatcher.group(1);
        }
        return "";
    }


    public static void mShare(String filepath, Activity activity) {
        File fileToShare = new File(filepath);
        if (isImageFile(filepath)) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.setType("image/*");
            Uri photoURI = FileProvider.getUriForFile(
                    activity.getApplicationContext(), activity.getApplicationContext()
                            .getPackageName() + ".provider", fileToShare);
            share.putExtra(Intent.EXTRA_STREAM,
                    photoURI);
            activity.startActivity(Intent.createChooser(share, "Share via"));

        } else if (isVideoFile(filepath)) {

            Uri videoURI = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getApplicationContext()
                    .getPackageName() + ".provider", fileToShare);
            Intent videoshare = new Intent(Intent.ACTION_SEND);
            videoshare.setType("*/*");
            videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            videoshare.putExtra(Intent.EXTRA_STREAM, videoURI);

            activity.startActivity(videoshare);
        }

    }

    public static void shareFile(Context context, boolean isVideo, String path) {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        if (isVideo)
            share.setType("Video/*");
        else
            share.setType("image/*");

        Uri uri;
        if (path.startsWith("content")) {
            uri = Uri.parse(path);
        } else {
            uri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider", new File(path));
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(share);
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void mediaScanner(Context context, String filePath, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context, new String[]{new File(DIRECTORY_DOWNLOADS + "/" + filePath + fileName).getAbsolutePath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else {
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(DIRECTORY_DOWNLOADS + "/" + filePath + fileName))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static AlertDialog alertDialog = null;

    public static void displayLoader(Context context) {
        if (alertDialog == null) {

            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View view = layoutInflaterAndroid.inflate(R.layout.dialog_loading, null);
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setView(view);

            alertDialog = alert.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
        }

    }

    public static void dismissLoader() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public static void downloader(Context context, String downloadURL, String path, String fileName) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "" + context.getString(R.string.dl_started), Toast.LENGTH_SHORT).show();
            }
        });

        String desc = context.getString(R.string.downloading);
//        String timeStamp = String.valueOf(System.currentTimeMillis());
//        String file = "tiktok_" + "_" + timeStamp;
//        String ext = "mp4";
//        String name = file + "." + ext;
        Uri Download_Uri = Uri.parse(downloadURL);


        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(true);
        request.setTitle(context.getString(R.string.app_name));
        request.setVisibleInDownloadsUi(true);
        request.setDescription(desc);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, path + fileName);
        dm.enqueue(request);

        Utils.mediaScanner(context, path, fileName);
    }

    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static boolean isVideoFile(Context context, String path) {
        if (path.startsWith("content")) {
            DocumentFile fromTreeUri = DocumentFile.fromSingleUri(context, Uri.parse(path));
            String mimeType = fromTreeUri.getType();
            return mimeType != null && mimeType.startsWith("video");
        } else {
            String mimeType = URLConnection.guessContentTypeFromName(path);
            return mimeType != null && mimeType.startsWith("video");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean copyFileInSavedDir(Context context, String sourceFile, boolean isWApp) {

        String finalPath = getDir(isWApp).getAbsolutePath();

        String pathWithName = finalPath + File.separator + new File(sourceFile).getName();
        Uri destUri = Uri.fromFile(new File(pathWithName));

        InputStream is = null;
        OutputStream os = null;
        try {
            Uri uri= Uri.parse(sourceFile);
            is = context.getContentResolver().openInputStream(uri);
            os = context.getContentResolver().openOutputStream(destUri, "w");

            byte[] buffer = new byte[1024];

            int length;
            while ((length = is.read(buffer)) > 0)
                os.write(buffer, 0, length);

            is.close();
            os.flush();
            os.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(destUri);
            context.sendBroadcast(intent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    static File getDir(boolean isWApp) {

        File rootFile = downloadWhatsAppDir;
        if (!isWApp){
            rootFile = downloadWABusiDir;
        }
        rootFile.mkdirs();

        return rootFile;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean download(Context context, String sourceFile, boolean isWApp) {
        if (copyFileInSavedDir(context, sourceFile, isWApp)) {
            return true;
        } else {
            return false;
        }
    }
}
