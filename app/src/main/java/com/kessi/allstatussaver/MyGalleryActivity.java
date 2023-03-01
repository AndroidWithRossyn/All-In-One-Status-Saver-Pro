package com.kessi.allstatussaver;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kessi.allstatussaver.adapter.GalleryPagerAdapter;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.LayManager;

public class MyGalleryActivity extends AppCompatActivity {

    ImageView backBtn;
    TextView headerTxt;

    ViewPager viewPager;
    TabLayout tabLayout;
    GalleryPagerAdapter adapter;

    private String[] imagePress =
    { "Whatsapp", "WA Business", "Instagram", "Tiktok", "Twitter", "Facebook", "Vimeo", "Likee", "SnackVideo",
            "ShareChat", "Roposo", "Chingari", "Moj", "MX TakTak", "Mitron", "Zili"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gallery);

        headerTxt = findViewById(R.id.headerTxt);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(16);
        adapter = new GalleryPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        View v = LayoutInflater.from(MyGalleryActivity.this).inflate(R.layout.gallery_tab, null);
        TextView img = v.findViewById(R.id.imgView);
        img.setText("Whatsapp");
        img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(MyGalleryActivity.this, 250, 90);
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

                if (!AdManager.isloadFbAd) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(MyGalleryActivity.this, null);
                } else {
                    AdManager.adCounter++;
                    AdManager.showMaxInterstitial(MyGalleryActivity.this, null);
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

        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(MyGalleryActivity.this);
            AdManager.loadBannerAd(MyGalleryActivity.this, adContainer);
            AdManager.loadInterAd(MyGalleryActivity.this);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(MyGalleryActivity.this);
            AdManager.maxBanner(MyGalleryActivity.this, adContainer);
            AdManager.maxInterstital(MyGalleryActivity.this);
        }

    }



    public View getTabView(int position) {
        View v = LayoutInflater.from(MyGalleryActivity.this).inflate(R.layout.gallery_tab, null);
        TextView img =  v.findViewById(R.id.imgView);
        img.setText(imagePress[position]);
        img.setBackgroundResource(R.drawable.tab_btn_unpress);
        img.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));

        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(MyGalleryActivity.this, 250, 90);
        img.setLayoutParams(param);
        return v;
    }


    public View getTabViewUn(int position) {
        View v = LayoutInflater.from(MyGalleryActivity.this).inflate(R.layout.gallery_tab, null);
        TextView img =  v.findViewById(R.id.imgView);
        img.setBackgroundResource(R.drawable.tab_btn_press);
        img.setText(imagePress[position]);
        img.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(MyGalleryActivity.this, 250, 90);
        img.setLayoutParams(param);
        return v;
    }

}
