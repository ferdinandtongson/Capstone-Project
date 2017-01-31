package me.makeachoice.gymratpta.controller.viewside.viewpager;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import me.makeachoice.gymratpta.controller.manager.MaidRegistry;
import me.makeachoice.gymratpta.controller.viewside.maid.MyMaid;

/**
 * Created by Usuario on 1/28/2017.
 */

class ViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

    private String mBaseMaidKey;
    public ViewPagerAdapter(FragmentManager manager, String baseMaidKey) {
        super(manager);

        mBaseMaidKey = baseMaidKey;
    }

    @Override
    public Fragment getItem(int position) {
        MaidRegistry maidRegistry = MaidRegistry.getInstance();

        String maidKey = mBaseMaidKey + position;

        MyMaid maid = maidRegistry.requestMaid(maidKey);
        return maid.getFragment();
    }

    @Override
    public int getCount() {
        //return mFragmentList.size();
        return mFragmentTitleList.size();
    }

    public void addTitleList(String title){
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}