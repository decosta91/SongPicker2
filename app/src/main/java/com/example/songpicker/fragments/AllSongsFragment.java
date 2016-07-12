package com.example.songpicker.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.songpicker.R;
import com.example.songpicker.adapters.AllSongsAdapter;
import com.example.songpicker.model.Song;
import com.example.songpicker.utils.ImageLoader.ImageCache;
import com.example.songpicker.utils.ImageLoader.ImageExtractor;
import com.example.songpicker.utils.ImageLoader.ImageFetcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StanoevskiDejan on 6/27/2016.
 */

public class AllSongsFragment extends Fragment {
    int mImageThumbSize;
    ImageExtractor mImageExtractor;
    ImageFetcher mImageFetcher;
    AllSongsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageThumbSize = (int) (getResources().getDisplayMetrics().density *
                getResources().getDimensionPixelSize(R.dimen.album_art_small_art));

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(),
                        getString(R.string.SMALL_THUMBS_CACHE_FOLDER));

        cacheParams.setMemCacheSizePercent(0.125f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageExtractor = new ImageExtractor(getActivity(), mImageThumbSize, getString(R.string.SMALL_THUMBS_CACHE_FOLDER));
        mImageExtractor.setLoadingImage(R.drawable.song_art);
        mImageExtractor.addImageCache(getActivity().getFragmentManager(), cacheParams);
        mImageExtractor.setImageFadeIn(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=
                inflater.inflate(R.layout.recyclerview_songs,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView=(RecyclerView)getView().findViewById(R.id.songs_recyclerview);
        adapter=new AllSongsAdapter(getContext());
        adapter.setImageExtractor(mImageExtractor);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        new GetSONGS().execute();
    }

    private List<Song> setUpSongList() {
        List<Song> localSongs = new ArrayList<>();
        String selection =
                MediaStore.Audio.Media.IS_MUSIC + " != 0"
                        + " AND ("
                        + MediaStore.Audio.Media.DATA + " like '%mp3'"
                        + " OR "
                        + MediaStore.Audio.Media.DATA + " like '%wav'"
                        + " OR "
                        + MediaStore.Audio.Media.DATA + " like '%aac'"
                        + " OR "
                        + MediaStore.Audio.Media.DATA + " like '%m4a')";

        String[] projection = new String[]{
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.DATA

        };
        Uri externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursorExt = getActivity().getContentResolver().query(externalContentUri, projection,
                selection, null, null);


        int artistIndex = cursorExt.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST);
        int titleIndex = cursorExt.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE);
        int durationIndex = cursorExt.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION);
        int trackNbIndex = cursorExt.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID);
        int dataNbIndex = cursorExt.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA);

        while (cursorExt.moveToNext()) {
            String artist = cursorExt.getString(artistIndex);
            String title = cursorExt.getString(titleIndex);
            String data = cursorExt.getString(dataNbIndex);
            long album_id=cursorExt.getLong(trackNbIndex);
            long duration = cursorExt.getLong(durationIndex);

            Song song = new Song(title,data, artist, duration,album_id);
            localSongs.add(song);
        }
        cursorExt.close();

      //  setupadapter(localSongs);
        return localSongs;
    }

    class GetSONGS extends AsyncTask<Void,Void, List<Song>> {
        @Override
        protected List<Song> doInBackground(Void... params) {
            return setUpSongList();
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            adapter.addSongs(songs);
        }
    }

    }
