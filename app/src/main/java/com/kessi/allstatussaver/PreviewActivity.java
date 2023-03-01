package com.kessi.allstatussaver;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;





import android.widget.TextView;
import android.widget.Toast;

import com.kessi.allstatussaver.adapter.FullscreenImageAdapter;
import com.kessi.allstatussaver.model.DataModel;
import com.kessi.allstatussaver.utils.AdManager;
import com.kessi.allstatussaver.utils.LayManager;
import com.kessi.allstatussaver.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<DataModel> imageList;
    int position;


    LinearLayout menu_save, menu_share, menu_delete;
    FullscreenImageAdapter fullscreenImageAdapter;
    String  statusdownload;



    ImageView backIV;
    LinearLayout bottomLay;
    TextView headerTxt;
    String folderPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        headerTxt = findViewById(R.id.headerTxt);

        backIV = findViewById(R.id.backIV);

        bottomLay = findViewById(R.id.bottomLay);
        LinearLayout.LayoutParams botmparam = LayManager.setLinParams(PreviewActivity.this, 1080, 307);
        bottomLay.setLayoutParams(botmparam);

        viewPager = findViewById(R.id.viewPager);

        menu_save = findViewById(R.id.menu_save);

        menu_share = findViewById(R.id.menu_share);

        menu_delete = findViewById(R.id.menu_delete);

        LinearLayout.LayoutParams btnParam = LayManager.setLinParams(PreviewActivity.this, 300, 100);
        menu_save.setLayoutParams(btnParam);
        menu_share.setLayoutParams(btnParam);
        menu_delete.setLayoutParams(btnParam);

        imageList = getIntent().getParcelableArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);
        statusdownload = getIntent().getStringExtra("statusdownload");
        folderPath = getIntent().getStringExtra("folderpath");

        if (statusdownload.equals("download")) {

            bottomLay.setWeightSum(2.0f);
            menu_save.setVisibility(View.GONE);
        } else {

            bottomLay.setWeightSum(3.0f);
            menu_save.setVisibility(View.VISIBLE);
        }

        fullscreenImageAdapter = new FullscreenImageAdapter(PreviewActivity.this, imageList);
        viewPager.setAdapter(fullscreenImageAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!AdManager.isloadFbAd) {
                    AdManager.adCounter++;
                    AdManager.showInterAd(PreviewActivity.this, null);
                } else {
                    AdManager.adCounter++;
                    AdManager.showMaxInterstitial(PreviewActivity.this, null);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        menu_save.setOnClickListener(clickListener);
        menu_share.setOnClickListener(clickListener);
        menu_delete.setOnClickListener(clickListener);
        backIV.setOnClickListener(clickListener);

        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (!AdManager.isloadFbAd) {
            //admob
            AdManager.initAd(PreviewActivity.this);
            AdManager.loadBannerAd(PreviewActivity.this, adContainer);
            AdManager.loadInterAd(PreviewActivity.this);
        } else {
            //MAX + Fb banner Ads
            AdManager.initMAX(PreviewActivity.this);
            AdManager.maxBanner(PreviewActivity.this, adContainer);
            AdManager.maxInterstital(PreviewActivity.this);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backIV:
                    onBackPressed();
                    break;
                case R.id.menu_save:
                    if (imageList.size() > 0) {

                        //ads
                        if (!AdManager.isloadFbAd) {
                            AdManager.adCounter++;
                            AdManager.showInterAd(PreviewActivity.this, null);
                        } else {
                            AdManager.adCounter++;
                            AdManager.showMaxInterstitial(PreviewActivity.this, null);
                        }

                        try {
                            Utils.download(PreviewActivity.this, imageList.get(viewPager.getCurrentItem()).getFilePath(), getIntent().getBooleanExtra("isWApp", false));
                            Toast.makeText(PreviewActivity.this, "Status saved successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(PreviewActivity.this, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        finish();
                    }
                    break;

                case R.id.menu_share:
                    if (imageList.size() > 0) {
                        Utils.shareFile(PreviewActivity.this, Utils.isVideoFile(PreviewActivity.this, imageList.get(viewPager.getCurrentItem()).getFilePath()),imageList.get(viewPager.getCurrentItem()).getFilePath());
                    } else {
                        finish();
                    }
                    break;

                case R.id.menu_delete:
                    if (imageList.size() > 0) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreviewActivity.this);
                        alertDialog.setTitle("Delete");
                        alertDialog.setMessage("Sure to Delete this Image?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                int currentItem = 0;

                                if (statusdownload.equals("download")) {
                                    File file = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());
                                    if (file.exists()) {
                                        boolean del = file.delete();
                                        delete(currentItem);
                                    }
                                }else {
                                    DocumentFile fromTreeUri = DocumentFile.fromSingleUri(PreviewActivity.this, Uri.parse(imageList.get(viewPager.getCurrentItem()).getFilePath()));
                                    if (fromTreeUri.exists()) {
                                        boolean del = fromTreeUri.delete();
                                        delete(currentItem);
                                    }
                                }
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();

                        if (!AdManager.isloadFbAd) {
                            AdManager.adCounter++;
                            AdManager.showInterAd(PreviewActivity.this, null);
                        } else {
                            AdManager.adCounter++;
                            AdManager.showMaxInterstitial(PreviewActivity.this, null);
                        }
                    } else {
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    void delete(int currentItem){
        if (imageList.size() > 0 && viewPager.getCurrentItem() < imageList.size()) {
            currentItem = viewPager.getCurrentItem();
        }
        imageList.remove(viewPager.getCurrentItem());
        fullscreenImageAdapter = new FullscreenImageAdapter(PreviewActivity.this, imageList);
        viewPager.setAdapter(fullscreenImageAdapter);

        Intent intent = new Intent();
        setResult(10, intent);

        if (imageList.size() > 0) {
            viewPager.setCurrentItem(currentItem);
        } else {
            finish();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
