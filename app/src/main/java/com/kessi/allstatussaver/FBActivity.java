package com.kessi.allstatussaver;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kessi.allstatussaver.fragment.FBHomeFragment;
import com.kessi.allstatussaver.fragment.UrlDownloadFragment;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.LayManager;

import java.util.ArrayList;
import java.util.List;

public class FBActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);


        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        View v = LayoutInflater.from(FBActivity.this).inflate(R.layout.tab_layout, null);
        TextView tab_title = v.findViewById(R.id.tab_title);
        tab_title.setBackgroundResource(imageUnPress[0]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(FBActivity.this, 400, 110);
        tab_title.setText("My Account");
        tab_title.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        tab_title.setLayoutParams(param);
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
            AdManager.initAd(FBActivity.this);
            AdManager.loadBannerAd(FBActivity.this, adContainer);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(FBActivity.this);
            AdManager.maxBanner(FBActivity.this, adContainer);
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private int[] imageUnPress = {R.drawable.tab_btn_press, R.drawable.tab_btn_press};
    private int[] imagePress = {R.drawable.tab_btn_unpress, R.drawable.tab_btn_unpress};

    public View getTabView(int position) {
        View v = LayoutInflater.from(FBActivity.this).inflate(R.layout.tab_layout, null);
        TextView tab_title =  v.findViewById(R.id.tab_title);
        tab_title.setBackgroundResource(imagePress[position]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(FBActivity.this, 400, 110);

        if (position == 0) {
            tab_title.setText("My Account");
            tab_title.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        } else {
            tab_title.setText("Help");
            tab_title.setTextColor(getResources().getColor(R.color.tab_unpress_text_color));
        }
        tab_title.setLayoutParams(param);


        return v;
    }


    public View getTabViewUn(int position) {
        View v = LayoutInflater.from(FBActivity.this).inflate(R.layout.tab_layout, null);
        TextView tab_title = v.findViewById(R.id.tab_title);
        tab_title.setBackgroundResource(imageUnPress[position]);
        LinearLayout.LayoutParams param;
        param = LayManager.setLinParams(FBActivity.this, 400, 110);
        if (position == 0) {
            tab_title.setText("My Account");
            tab_title.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        } else {
            tab_title.setText("Help");
            tab_title.setTextColor(getResources().getColor(R.color.tab_press_text_color));
        }
        tab_title.setLayoutParams(param);


        return v;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FBHomeFragment(), "My Account");
        adapter.addFragment(new UrlDownloadFragment(), "Help");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private String[] titles = {"Home", "Videos List", "Paste Link"};

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return FBHomeFragment.newInstance(position);
                }
                case 1: {
                    return UrlDownloadFragment.newInstance(position);
                }

            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
