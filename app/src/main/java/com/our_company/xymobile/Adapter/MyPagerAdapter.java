package com.our_company.xymobile.Adapter;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by little star on 2017/5/26.
 */

public class MyPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragList;
    private Bitmap [] bitmaps;
    private ArrayList<String>titleList;
    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragList,ArrayList<String>titleList){
        super(fm);
        this.fragList=fragList;
        this.titleList=titleList;
    }

    @Override
    public Fragment getItem(int position) {

        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
