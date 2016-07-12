package com.example.songpicker.fragments;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.songpicker.R;
import com.example.songpicker.adapters.AllPlayListAdapter;
import com.example.songpicker.model.Playlist;
import com.example.songpicker.model.Song;
import com.example.songpicker.utils.ImageLoader.ImageCache;
import com.example.songpicker.utils.ImageLoader.ImageExtractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StanoevskiDejan on 7/2/2016.
 */

public class AllPlaylistFragment extends Fragment {
    int mImageThumbSize;
    ImageExtractor mImageExtractor;
    AllPlayListAdapter adapter;

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
                inflater.inflate(R.layout.recyclerview_artis,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView=(RecyclerView)getView().findViewById(R.id.artist_recycler_view);
        adapter=new AllPlayListAdapter(getContext());
        adapter.setImageExtractor(mImageExtractor);
        recyclerView.setAdapter(adapter);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2); // (Context context, int spanCount)
        recyclerView.setLayoutManager(mGridLayoutManager);
        setUpPlayList();
    }

    private void setUpPlayList() {
        List<Playlist> playlists = new ArrayList<>();

        String[] projection = new String[]{
                BaseColumns._ID,
                MediaStore.Audio.PlaylistsColumns.NAME

        };
        Uri externalContentUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        Cursor cursorExt = getActivity().getContentResolver().query(externalContentUri, projection,
                null, null, MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER);

   /*     if(cursorExt==null)
            Log.d("Ima",Integer.toString(cursorExt.getCount()));
        else
            Log.d("Ima",Integer.toString(cursorExt.getCount()));

        cursorExt.close();
*/

        while (cursorExt.moveToNext()) {
            String id = cursorExt.getString(0);
            String name=cursorExt.getString(1);

            Playlist playlist = new Playlist(id, name);
            new GetPLAYLIST().execute(playlist);
        }
        cursorExt.close();


    }

    class GetPLAYLIST extends AsyncTask<Playlist,Void, List<Song>> {
        Playlist playlist;

        @Override
        protected List<Song> doInBackground(Playlist... params) {
            playlist=params[0];
            return getSongsForPlayList(playlist,playlist.getmPlaylistId());
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            playlist.setSongList(songs);
            adapter.addItem(playlist);
        }
    }

    private List<Song> getSongsForPlayList(Playlist playlist, String playListId) {
        Cursor song_artist=getCursorForSongs(getContext(),playListId);

        List<Song> songList=new ArrayList<>();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (song_artist != null && song_artist.moveToFirst()) {
            do {

                String artist = song_artist.getString(0);
                String title = song_artist.getString(1);
                String data = song_artist.getString(4);
                if(!playlist.hasAlbumArt) {
                    retriever.setDataSource(data);
                    if (retriever.getEmbeddedPicture() != null)
                    {
                        playlist.setHasAlbumArt(true);
                        playlist.setAlbumArt(data);
                    }
                }
                long album_id=song_artist.getLong(2);
                long duration = song_artist.getLong(3);

                Song song = new Song(title,data, artist, duration,album_id);
                songList.add(song);
            } while (song_artist.moveToNext());
        }
        // Close the cursor
        if (song_artist != null) {
            retriever.release();
            song_artist.close();
            song_artist = null;
        }
        return songList;

    }

    private Cursor getCursorForSongs(Context context, String id) {

        final StringBuilder mSelection = new StringBuilder();
        mSelection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "=1");
        mSelection.append(" AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''"); //$NON-NLS-2$

        String[] projection = new String[]{
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.DATA

        };
        return context.getContentResolver().query(
                MediaStore.Audio.Playlists.Members.getContentUri("external", Long.valueOf(id)),
               projection, mSelection.toString(), null,
                MediaStore.Audio.Playlists.Members.DEFAULT_SORT_ORDER);
    }

}



