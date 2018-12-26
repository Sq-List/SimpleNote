package com.sqlist.simplenote.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by asus on 2018/1/8.
 */

public class FragAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"备忘录", "小工具"};

    public FragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        if (arg0 == 0)
        {
            return new FirstFragment();
        }
        else if (arg0 == 1)
        {
            return new SecondFragment();
        }
        return new FirstFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
