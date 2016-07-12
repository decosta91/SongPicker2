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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.songpicker.R;
import com.example.songpicker.adapters.AllGenresAdapter;
import com.example.songpicker.model.*;
import com.example.songpicker.utils.ImageLoader.ImageCache;
import com.example.songpicker.utils.ImageLoader.ImageExtractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StanoevskiDejan on 6/30/2016.
 */

public class AllGenresFragment extends Fragment{
    int mImageThumbSize;
    ImageExtractor mImageExtractor;
    AllGenresAdapter adapter;

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

        View view=inflater.inflate(R.layout.recyclerview_artis,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView=(RecyclerView)getView().findViewById(R.id.artist_recycler_view);
        adapter=new AllGenresAdapter(getContext());
        adapter.setImageExtractor(mImageExtractor);
        recyclerView.setAdapter(adapter);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2); // (Context context, int spanCount)
        recyclerView.setLayoutManager(mGridLayoutManager);
        setUpGenresList();

    }

    private void setUpGenresList() {
        List<Genre> genres=new ArrayList<>();

        String[] projection = new String[]{
                BaseColumns._ID,
                MediaStore.Audio.GenresColumns.NAME

        };
        Uri externalContentUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;

        Cursor cursorExt = getActivity().getContentResolver().query(externalContentUri, projection,
                null, null, MediaStore.Audio.Genres.DEFAULT_SORT_ORDER);
        Genre genreUnknown=null;
        List<Song> unknownGenreSongs=new ArrayList<>();
        while (cursorExt.moveToNext()) {
            String genreId = cursorExt.getString(0);
            String genreName=cursorExt.getString(1);

            if(MediaStore.UNKNOWN_STRING.equals(genreName))
            {
                if(genreUnknown==null)
                    genreUnknown=new Genre(genreId,genreName);

            }
            else
            {
                Genre genre=new Genre(genreId,genreName);
           //     List<Song> songList=getSongsForGenres(genre,genreId);
            //    genre.setSongs(songList);
             //   genres.add(genre);
                new GetGENRE().execute(genre);
            }

        }
        if(genreUnknown!=null) {
            if(unknownGenreSongs.size()>0)
            genreUnknown.setSongs(unknownGenreSongs);
       //     genres.add(genreUnknown);
        }
        cursorExt.close();
    }

    class GetGENRE extends AsyncTask<Genre,Void, List<Song>> {
        Genre genre;

        @Override
        protected List<Song> doInBackground(Genre... params) {
            genre=params[0];
            return getSongsForGenres(genre,genre.getmGenreId());
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            genre.setSongs(songs);
            adapter.addItem(genre);
        }
    }

    private List<Song> getSongsForGenres(Genre genre, String genreId) {
        Cursor song_artist=getCursorForSongs(getContext(),genreId);

        List<Song> songList=new ArrayList<>();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (song_artist != null && song_artist.moveToFirst()) {
            do {

                String artist = song_artist.getString(0);
                String title = song_artist.getString(1);
                String data = song_artist.getString(4);
                if(!genre.hasAlbumArt) {
                    retriever.setDataSource(data);
                    if (retriever.getEmbeddedPicture() != null)
                    {
                        genre.setData_art(data);
                        genre.setHasAlbumArt(true);
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

    private Cursor getCursorForSongs(Context context, String genreId) {

        final StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Audio.Genres.Members.IS_MUSIC + "=1");
        selection.append(" AND " + MediaStore.Audio.Genres.Members.TITLE + "!=''");//$NON-NLS-2$
        return context.getContentResolver().query(
                MediaStore.Audio.Genres.Members.getContentUri("external",Long.valueOf(genreId)), new String[] {
                        /* 0 */
                        MediaStore.Audio.Genres.Members.ARTIST,
                        /* 1 */
                        MediaStore.Audio.Genres.Members.TITLE,
                        /* 2 */
                        MediaStore.Audio.Genres.Members.DURATION,
                        /* 3 */
                        MediaStore.Audio.Genres.Members.ALBUM_ID,
                        MediaStore.Audio.Genres.Members.DATA

                }, selection.toString(), null, MediaStore.Audio.Genres.Members.DEFAULT_SORT_ORDER);
    }


}



