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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import com.example.songpicker.R;
import com.example.songpicker.adapters.AllArtistAdapter;
import com.example.songpicker.model.Artist;
import com.example.songpicker.model.Song;
import com.example.songpicker.utils.ImageLoader.ImageCache;
import com.example.songpicker.utils.ImageLoader.ImageExtractor;

/**
 * Created by StanoevskiDejan on 6/29/2016.
 */

public class AllArtistFragment extends Fragment {
    int mImageThumbSize;
    ImageExtractor mImageExtractor;
    AllArtistAdapter adapter;
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

        View view= inflater.inflate(R.layout.recyclerview_artis,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       RecyclerView recyclerView=(RecyclerView)getView().findViewById(R.id.artist_recycler_view);
        adapter=new AllArtistAdapter(getContext());
        adapter.setImageExtractor(mImageExtractor);
        recyclerView.setAdapter(adapter);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2); // (Context context, int spanCount)
        recyclerView.setLayoutManager(mGridLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setUpArtistList();

    }


        class GetARTIST extends AsyncTask<Artist,Void, List<Song>>{
            Artist artist;

            @Override
            protected List<Song> doInBackground(Artist... params) {
                artist=params[0];
                return getSongsForArtist(artist,artist.getmArtistName());
            }

            @Override
            protected void onPostExecute(List<Song> songs) {
                super.onPostExecute(songs);
                artist.setSongs(songs);
                adapter.addItem(artist);
            }
        }


    private void setUpArtistList() {
     //   List<Artist> artists = new ArrayList<>();

       String[] projection = new String[]{
               BaseColumns._ID,
               MediaStore.Audio.ArtistColumns.ARTIST

        };
        Uri externalContentUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        Cursor cursorExt = getActivity().getContentResolver().query(externalContentUri, projection,
                null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
        Artist artistUnknown=null;
        List<Song> unknownArtistSongs=new ArrayList<>();
        while (cursorExt.moveToNext()) {
            String artistid = cursorExt.getString(0);
            String artistName=cursorExt.getString(1);

            if(MediaStore.UNKNOWN_STRING.equals(artistName))
            {
                if(artistUnknown==null)
                artistUnknown=new Artist(artistid,artistName);

           //     List<Song> songList=getSongsForArtist(artistUnknown,artistName);
            //    unknownArtistSongs.addAll(songList);
           //     new GetARTIST().execute(artistUnknown);

            }
            else
            {
                Artist artist1=new Artist(artistid,artistName);
             //   List<Song> songList=getSongsForArtist(artist1,artistName);
              //  artist1.setSongs(songList);
                new GetARTIST().execute(artist1);
              //  new GetARTIST().executeOnExecutor(GetARTIST.SERIAL_EXECUTOR,artist1);
             //   adapter.addItem(artist1);
            }

        }
        if(artistUnknown!=null) {
            if(unknownArtistSongs.size()>0)
            artistUnknown.setSongs(unknownArtistSongs);
          //  adapter.add(artistUnknown);
       //     adapter.addItem(artistUnknown);
        }
        cursorExt.close();
       // setUpAdapter(artists);
    }

    private List<Song> getSongsForArtist(Artist artist1,String artistName){
        Cursor song_artist=getCursorForSongs(getContext(),artistName);

        List<Song> songList=new ArrayList<>();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (song_artist != null && song_artist.moveToFirst()) {
            do {
                int artistIndex = song_artist.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST);
                int titleIndex = song_artist.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE);
                int durationIndex = song_artist.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION);
                int trackNbIndex = song_artist.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID);
                int dataNbIndex = song_artist.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA);

                String artist = song_artist.getString(artistIndex);
                String title = song_artist.getString(titleIndex);
                String data = song_artist.getString(dataNbIndex);
                if(!artist1.hasAlbumArt) {
                    retriever.setDataSource(data);
                    if (retriever.getEmbeddedPicture() != null)
                    {
                        artist1.setData_art(data);
                        artist1.setHasAlbumArt(true);
                    }
                }
                //}
                long album_id=song_artist.getLong(trackNbIndex);
                long duration = song_artist.getLong(durationIndex);

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

    private Cursor getCursorForSongs(Context context,String artistId) {

        final StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Audio.AudioColumns.IS_MUSIC + "!=0");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''");
        selection.append(" AND " + MediaStore.Audio.AudioColumns.ARTIST + "='" + artistId+"'");

        String[] projection = new String[]{
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.DATA

        };

        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
               projection, selection.toString(), null,
                null);

    }

    private void setUpAdapter(List<Artist> artists) {
        RecyclerView recyclerView=(RecyclerView)getView().findViewById(R.id.artist_recycler_view);
        AllArtistAdapter adapter=new AllArtistAdapter(getContext());
        adapter.setImageExtractor(mImageExtractor);
        recyclerView.setAdapter(adapter);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2); // (Context context, int spanCount)
        recyclerView.setLayoutManager(mGridLayoutManager);

    }


}
