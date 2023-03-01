package com.kessi.allstatussaver.fragment;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.kessi.allstatussaver.R;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.LayManager;
import com.kessi.allstatussaver.utils.PrefManager;
import com.kessi.allstatussaver.utils.Utils;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class UrlDownloadFragment extends Fragment {
//    TextView imageButton;
//    EditText editText;
    FBHomeFragment homeFragment;
    private PrefManager pref;
    private static final String ARG_POSITION = "position";

    ImageView help1, help2, help3;
    public static UrlDownloadFragment newInstance(int position) {
        UrlDownloadFragment f = new UrlDownloadFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.urldownload, container, false);
        homeFragment = new FBHomeFragment();
//        imageButton = rootView.findViewById(R.id.download);
//        editText = (EditText) rootView.findViewById(R.id.editText);
        pref = new PrefManager(getContext());
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String URL = editText.getText().toString();
//                if (URL.equals("")) {
//                    Toast.makeText(getActivity(), "Please Paste or Enter The Link", Toast.LENGTH_SHORT).show();
//                } else if (!URL.contains("fbcdn.net")) {
//                    Toast.makeText(getActivity(), "Invalid URL!", Toast.LENGTH_SHORT).show();
//                } else {
//                    downloadVideo(editText.getText().toString());
//                }
//            }
//        });

        help1 = rootView.findViewById(R.id.help1);
        help2 = rootView.findViewById(R.id.help2);
        help3 = rootView.findViewById(R.id.help3);

        Glide.with(getActivity())
                .load(R.drawable.facebook_step1)
                .into(help1);

        Glide.with(getActivity())
                .load(R.drawable.facebook_step2)
                .into(help2);

        Glide.with(getActivity())
                .load(ContextCompat.getDrawable(getActivity(),R.drawable.facebook_step3))
                .into(help3);

        LinearLayout adaptiveAdContainer = rootView.findViewById(R.id.banner_adaptive_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(getActivity());
            AdManager.adptiveBannerAd(getActivity(), adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(getActivity());
            AdManager.maxBannerAdaptive(getActivity(), adaptiveAdContainer);
        }
        return rootView;
    }

    private void downloadVideo(String pathvideo) {
        Log.e("pre path", pathvideo);
        if (pathvideo.contains("story")) {
            homeFragment.getUrlfromUrlDownload(pathvideo);
        } else {
//            editText.setText("");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathvideo));
            request.allowScanningByMediaScanner();
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            int Number = pref.getFileName();
            request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, Utils.fbDirPath+"Video-"+Number+".mp4");
            DownloadManager dm = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
            ArrayList<String> urldownloadFragmentList = (new FBHomeFragment()).getList();
            if (urldownloadFragmentList.contains(pathvideo)) {
                Toast.makeText(getActivity().getApplicationContext(), "The Video is Already Downloading", Toast.LENGTH_LONG).show();
            } else {
                urldownloadFragmentList.add(pathvideo);
                dm.enqueue(request);
                Toast.makeText(getActivity().getApplicationContext(), "Downloading Video-" + Number + ".mp4", Toast.LENGTH_LONG).show();
                Number++;
                pref.setFileName(Number);

                Utils.mediaScanner(getActivity(), Utils.fbDirPath,"Video-"+Number+".mp4");
            }
        }
    }

}