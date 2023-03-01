package com.kessi.allstatussaver.interfaces;

public interface VideoDownloader {

//    String createDirectory();

    String getVideoId(String link);

    void DownloadVideo();
}
