package com.kessi.allstatussaver.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kessi.allstatussaver.R;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.LayManager;


public class WAGuideFragment extends Fragment {
    ImageView help1, help2, help3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View localView = inflater.inflate(R.layout.frag_wapp_guide, container, false);

        help1 = localView.findViewById(R.id.help1);
        help2 = localView.findViewById(R.id.help2);
        help3 = localView.findViewById(R.id.help3);

        Glide.with(getActivity())
                .load(ContextCompat.getDrawable(getActivity(), R.drawable.wa_help_1))
                .into(help1);

        Glide.with(getActivity())
                .load(ContextCompat.getDrawable(getActivity(), R.drawable.help_2))
                .into(help2);

        Glide.with(getActivity())
                .load(ContextCompat.getDrawable(getActivity(), R.drawable.help_3))
                .into(help3);

        LinearLayout adaptiveAdContainer = localView.findViewById(R.id.banner_adaptive_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(getActivity());
            AdManager.adptiveBannerAd(getActivity(), adaptiveAdContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(getActivity());
            AdManager.maxBanner(getActivity(), adaptiveAdContainer);
        }
        return localView;
    }
}
