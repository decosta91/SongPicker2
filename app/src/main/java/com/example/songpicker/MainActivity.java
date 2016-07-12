package com.example.songpicker;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.songpicker.adapters.SongVIewPagerAdapter;
import com.example.songpicker.utils.ImageLoader.ImageExtractor;

import butterknife.Bind;

public class MainActivity extends AppCompatActivity {

    private SongVIewPagerAdapter songVIewPagerAdapter;

    Toolbar toolbar;
    SearchView searchView;

    @Bind(R.id.viewPagerSongs)
    ViewPager viewPager;

    @Bind(R.id.tabLayoutSongs)
    TabLayout tabLayout;

    int mImageThumbSize;
    ImageExtractor mImageExtractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_songs);
        toolbar.setTitle("My Songs");
      //  toolbar.inflateMenu(R.menu.songs_menu);
        setSupportActionBar(toolbar);
        setUpViewPager();


    }

    private void setUpViewPager() {

       tabLayout=(TabLayout) findViewById(R.id.tabLayoutSongs);
        viewPager=(ViewPager)findViewById(R.id.viewPagerSongs);
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this,R.color.tab_selector));
        songVIewPagerAdapter=new SongVIewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(songVIewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayoutListener(tabLayout,viewPager));
        viewPager.setOffscreenPageLimit(4);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.songs_menu,menu);
        return true;
    }

}
