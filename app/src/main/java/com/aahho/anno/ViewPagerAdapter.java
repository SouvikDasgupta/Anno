package com.aahho.anno;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aahho.anno.model.Users;

/**
 * Created by souvikdas on 26/9/17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private CharSequence[] titles;
    private Users actor;

    public ViewPagerAdapter(FragmentManager fm, final CharSequence[] titles, final Users actor) {
        super(fm);
        this.titles = titles;
        this.actor = actor;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", actor);
        if(position == 0){
            fragment = new AnnoActiveUserFragment();
            fragment.setArguments(bundle);
            return fragment;
        } else if(position == 1){
//            fragment = new AnnoPersonalChatFragment();
//            fragment.setArguments(bundle);
//            return fragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return titles[position];
    }

}
