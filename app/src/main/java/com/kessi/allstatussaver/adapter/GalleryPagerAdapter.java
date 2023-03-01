package com.kessi.allstatussaver.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kessi.allstatussaver.fragment.KChinFrag;
import com.kessi.allstatussaver.fragment.KMitronFrag;
import com.kessi.allstatussaver.fragment.KMojFrag;
import com.kessi.allstatussaver.fragment.KRopoFrag;
import com.kessi.allstatussaver.fragment.KZiliFrag;
import com.kessi.allstatussaver.fragment.LikeeFrag;
import com.kessi.allstatussaver.fragment.MxTakFrag;
import com.kessi.allstatussaver.fragment.SChatFrag;
import com.kessi.allstatussaver.fragment.SnackFrag;
import com.kessi.allstatussaver.fragment.TweatFrag;
import com.kessi.allstatussaver.fragment.VimeoFrag;
import com.kessi.allstatussaver.fragment.WABusFrag;
import com.kessi.allstatussaver.fragment.WAppFrag;
import com.kessi.allstatussaver.fragment.FBFrag;
import com.kessi.allstatussaver.fragment.InsFrag;
import com.kessi.allstatussaver.fragment.TikFrag;

public class GalleryPagerAdapter extends FragmentPagerAdapter {


    public GalleryPagerAdapter(FragmentManager fm) {
        super(fm);

    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new WAppFrag();
        }else if (position == 1) {
            return new WABusFrag();
        } else if (position == 2){
            return new InsFrag();
        }  else if (position == 3){
            return new TikFrag();
        }else if (position == 4){
            return new TweatFrag();
        }else if (position == 5){
            return new FBFrag();
        }else if (position == 6){
            return new VimeoFrag();
        }else if (position == 7){
            return new LikeeFrag();
        }else if (position == 8){
            return new SnackFrag();
        }else if (position == 9){
            return new SChatFrag();
        }else if (position == 10){
            return new KRopoFrag();
        } else if (position == 11){
            return new KChinFrag();
        } else if (position == 12) {
            return new KMojFrag();
        } else if (position == 13){
            return new MxTakFrag();
        } else if (position == 14) {
            return new KMitronFrag();
        } else {
            return new KZiliFrag();
        }
    }

    @Override
    public int getCount() {
        return 12;
    }


}
