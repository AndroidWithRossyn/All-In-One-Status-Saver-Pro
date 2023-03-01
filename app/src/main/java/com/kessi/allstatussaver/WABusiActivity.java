package com.kessi.allstatussaver;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kessi.allstatussaver.adapter.WAPagerAdapter;
import com.kessi.allstatussaver.fragment.WAStatusFragment;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.LayManager;
import com.google.android.gms.ads.AdView;

import java.io.File;

public class WABusiActivity extends AppCompatActivity implements View.OnClickListener {

    WAPagerAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    AdView mAdView;

    ImageView backBtn;
    LinearLayout  waBusiBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wawhats);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);

        String mBaseFolderPath = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.foldername) + File.separator;
        if (!new File(mBaseFolderPath).exists()) {
            new File(mBaseFolderPath).mkdir();
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        adapter = new WAPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        View v = LayoutInflater.from(WABusiActivity.this).inflate(R.layout.custom_tab, null);
        TextView img = v.findViewById(R.id.imgView);
        img.setBackgroundResource(imageUnPress[0]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(WABusiActivity.this, 400, 110);
        img.setText(R.string.recent_status);
        img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        img.setLayoutParams(param);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setCustomView(null);
        tab.setCustomView(v);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabViewUn(tab.getPosition()));


//                if (tab.getPosition() == 0) {
//                    ((WAStatusFragment) WABusiActivity.this.getSupportFragmentManager().findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + tab.getPosition())).loadMedia();
//                }


                if (!AdManager.isloadFbAd) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(WABusiActivity.this, null);
                } else {
                    AdManager.adCounter++;
                    AdManager.showMaxInterstitial(WABusiActivity.this, null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabView(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setCurrentItem(0);

        waBusiBtn = findViewById(R.id.waBusiBtn);
        waBusiBtn.setOnClickListener(this);

        LinearLayout adContainer = findViewById(R.id.banner_container);
            if (!AdManager.isloadFbAd) {
                //admob
                AdManager.initAd(WABusiActivity.this);
                AdManager.loadBannerAd(WABusiActivity.this, adContainer);
                AdManager.loadInterAd(WABusiActivity.this);
            } else {
                //MAX + Fb banner Ads
                AdManager.initMAX(WABusiActivity.this);
                AdManager.maxBanner(WABusiActivity.this, adContainer);
                AdManager.maxInterstital(WABusiActivity.this);
            }

    }


    private int[] imageUnPress = {R.drawable.tab_btn_press, R.drawable.tab_btn_press};
    private int[] imagePress = {R.drawable.tab_btn_unpress, R.drawable.tab_btn_unpress};

    public View getTabView(int position) {
        View v = LayoutInflater.from(WABusiActivity.this).inflate(R.layout.custom_tab, null);
        TextView img = v.findViewById(R.id.imgView);
        img.setBackgroundResource(imagePress[position]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(WABusiActivity.this, 400, 110);

        if (position == 0) {
            img.setText(R.string.recent_status);
            img.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        } else {
            img.setText(R.string.help);
            img.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        }
        img.setLayoutParams(param);


        return v;
    }


    public View getTabViewUn(int position) {
        View v = LayoutInflater.from(WABusiActivity.this).inflate(R.layout.custom_tab, null);
        TextView img = v.findViewById(R.id.imgView);
        img.setBackgroundResource(imageUnPress[position]);

        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(WABusiActivity.this, 400, 110);
        if (position == 0) {
            img.setText(R.string.recent_status);
            img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        } else {
            img.setText(R.string.help);
            img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        }

        img.setLayoutParams(param);


        return v;
    }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.waBusiBtn:
                Intent localIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b");
                boolean installed = appInstalledOrNot("com.whatsapp.w4b");
                if (installed) {
                    try {
                        startActivity(localIntent);
                    } catch (ActivityNotFoundException localActivityNotFoundException) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp.w4b")));
                    }
                } else {
                    Toast.makeText(this, "WA Business not install in your device!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}
