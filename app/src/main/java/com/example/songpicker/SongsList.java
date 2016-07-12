package com.example.songpicker;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.songpicker.adapters.AllSongsAdapter;
import com.example.songpicker.model.Song;
import com.example.songpicker.utils.ImageLoader.ImageCache;
import com.example.songpicker.utils.ImageLoader.ImageExtractor;

import java.util.ArrayList;
import java.util.List;

public class SongsList extends AppCompatActivity {
    int mImageThumbSize;
    ImageExtractor mImageExtractor;
    String imageart,nameartist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.songlist_details);

        Bundle bundle =getIntent().getExtras();
        ArrayList<Song> songList=bundle.getParcelableArrayList("data");
        imageart=bundle.getString("image");
        nameartist=bundle.getString("name");
        mImageThumbSize = (int) (getResources().getDisplayMetrics().density *
                getResources().getDimensionPixelSize(R.dimen.album_art_small_art));

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this,
                        getString(R.string.SMALL_THUMBS_CACHE_FOLDER));

        cacheParams.setMemCacheSizePercent(0.125f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageExtractor = new ImageExtractor(this, mImageThumbSize, getString(R.string.SMALL_THUMBS_CACHE_FOLDER));
        mImageExtractor.setLoadingImage(R.drawable.song_art);
        mImageExtractor.addImageCache(this.getFragmentManager(), cacheParams);
        mImageExtractor.setImageFadeIn(true);
        setUpToolbar();
        setUpAdapter(songList);
    }

    private void setUpToolbar() {
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.colapsingToolbar);
        collapsingToolbar.setTitle(nameartist);


    }

    private void setUpAdapter(List<Song> songList) {
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.artist_recycler_view);
        AllSongsAdapter adapter=new AllSongsAdapter(this);
        adapter.addSongs(songList);
        adapter.setImageExtractor(mImageExtractor);
        recyclerView.setAdapter(adapter);

        ImageView image=(ImageView)findViewById(R.id.artist_image);
        mImageExtractor.loadImage(imageart,image);

        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
