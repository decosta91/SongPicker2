package com.example.songpicker;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

/**
 * Created by StanoevskiDejan on 6/27/2016.
 */

public class TabLayoutListener extends TabLayout.TabLayoutOnPageChangeListener {
    ViewPager viewPager;

    public TabLayoutListener(TabLayout tabLayout, final ViewPager viewPager) {
        super(tabLayout);
        final TabLayout.Tab artist=tabLayout.newTab().setText("Artist");
        final TabLayout.Tab songs=tabLayout.newTab().setText("Songs");
        final TabLayout.Tab playlist=tabLayout.newTab().setText("Playlist");
        final TabLayout.Tab genres=tabLayout.newTab().setText("Genres");

        tabLayout.addTab(artist,0);
        tabLayout.addTab(songs,1);
        tabLayout.addTab(playlist,2);
        tabLayout.addTab(genres,3);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



}
