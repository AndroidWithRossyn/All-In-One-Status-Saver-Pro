package com.kessi.allstatussaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.Helpers;
import com.kessi.allstatussaver.utils.SharedPrefs;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout moreapp, policy, shareapp, rateapp;
    ImageView backBtn;
    SwitchCompat modeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        modeSwitch = findViewById(R.id.modeSwitch);
        int mode = SharedPrefs.getAppNightDayMode(this);
        if (mode == AppCompatDelegate.MODE_NIGHT_YES){
            modeSwitch.setChecked(true);
        }else {
            modeSwitch.setChecked(false);
        }
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SharedPrefs.setInt(SettingsActivity.this, SharedPrefs.PREF_NIGHT_MODE,AppCompatDelegate.MODE_NIGHT_YES);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharedPrefs.setInt(SettingsActivity.this, SharedPrefs.PREF_NIGHT_MODE,AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


        moreapp = findViewById(R.id.moreapp);
        moreapp.setOnClickListener(this);
        policy = findViewById(R.id.policy);
        policy.setOnClickListener(this);
        shareapp = findViewById(R.id.shareapp);
        shareapp.setOnClickListener(this);
        rateapp = findViewById(R.id.rateapp);
        rateapp.setOnClickListener(this);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);

        LinearLayout adContainer = findViewById(R.id.banner_container);

        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(SettingsActivity.this);
            AdManager.loadBannerAd(SettingsActivity.this, adContainer);
            AdManager.loadInterAd(SettingsActivity.this);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(SettingsActivity.this);
            AdManager.maxBanner(SettingsActivity.this, adContainer);
            AdManager.maxInterstital(SettingsActivity.this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.rateapp:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;

            case R.id.shareapp:
                Helpers.mShareText("Hey my friend check out this app\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n", SettingsActivity.this);
                break;

            case R.id.policy:
                startActivityes(new Intent(SettingsActivity.this, PrivacyActivity.class));
                break;

            case R.id.moreapp:
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/dev?id=7081479513420377164&hl=en_IN")));
                break;
        }
    }

    void startActivityes(Intent intent) {
        if (!AdManager.isloadFbAd) {
            AdManager.adCounter++;
            AdManager.showInterAd(SettingsActivity.this, intent);
        } else {
            AdManager.adCounter++;
            AdManager.showMaxInterstitial(SettingsActivity.this, intent);
        }
    }

}