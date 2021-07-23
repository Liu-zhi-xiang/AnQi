package com.motorbike.anqi.adapter;


import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class ViewpagerFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> mTitles;
    private FragmentManager fragmentManager;


    public ViewpagerFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        fragmentManager=fm;
        fragmentList = fragments;
        mTitles = titles;
    }
    @Override
    public Fragment getItem(int position) {
        if (fragmentList!=null){
            return fragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (fragmentList!=null){
            return fragmentList.size();
        }
        return 0;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {

        return super.instantiateItem(arg0, arg1);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        super.destroyItem(container, position, object);
    }


}
