package com.motorbike.anqi.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Fan on 2016/8/22.
 * fragment的适配器
 */
public class MessagesFragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> list;
    FragmentManager fm;

    public MessagesFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {

        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}