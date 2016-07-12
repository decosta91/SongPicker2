package com.example.songpicker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.songpicker.FragmentHelp;
import com.example.songpicker.fragments.AllArtistFragment;
import com.example.songpicker.fragments.AllGenresFragment;
import com.example.songpicker.fragments.AllPlaylistFragment;
import com.example.songpicker.fragments.AllSongsFragment;

/**
 * Created by StanoevskiDejan on 6/27/2016.
 */

public class SongVIewPagerAdapter extends FragmentStatePagerAdapter{
    private AllSongsFragment allSongsFragment;
    private AllGenresFragment allGenresFragment;
    private AllArtistFragment allArtistFragment;
    private AllPlaylistFragment allPlaylistFragment;
    private FragmentHelp f1,f2;

    public SongVIewPagerAdapter(FragmentManager fm) {
        super(fm);
        allArtistFragment=new AllArtistFragment();
        allSongsFragment=new AllSongsFragment();
        allPlaylistFragment=new AllPlaylistFragment();
        allGenresFragment=new AllGenresFragment();
        f1=new FragmentHelp();
        f2=new FragmentHelp();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return allArtistFragment;
            case 1: return allSongsFragment;
            case 2: return allPlaylistFragment;
            case 3: return allGenresFragment;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }


}
