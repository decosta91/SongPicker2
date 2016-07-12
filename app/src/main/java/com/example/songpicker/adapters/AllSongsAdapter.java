package com.example.songpicker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.songpicker.R;
import com.example.songpicker.model.Song;
import com.example.songpicker.utils.ImageLoader.ImageExtractor;
import com.example.songpicker.utils.ImageLoader.ImageFetcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StanoevskiDejan on 6/28/2016.
 */

public class AllSongsAdapter extends RecyclerView.Adapter<AllSongsAdapter.SongViewHolder> {
    List<Song> data;
    private LayoutInflater layoutInflater;
    private Context context;

    private ImageExtractor mExtractor;
    private ImageFetcher mFetcher;

    public AllSongsAdapter(Context context) {
        this.data=new ArrayList<>();
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }

    public void setImageExtractor(ImageExtractor extractor) {
        this.mExtractor = extractor;
    }

    public void setImageFetcher(ImageFetcher fetcher) {
        this.mFetcher = fetcher;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Informacija","SongsAdapter-onCreateView");
        View view=layoutInflater.inflate(R.layout.song_list_item,parent,false);
        SongViewHolder holder=new SongViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Log.d("Informacija","SongsAdapter-onBindView");
        Song current=data.get(position);
        holder.songTitle.setText(current.getTitle());
        holder.songArtist.setText(current.getArtist());
        mExtractor.loadImage(current.getData(), holder.albumArt);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addSongs(List<Song> songs){
        data.addAll(songs);
        notifyDataSetChanged();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        TextView songArtist;
        ImageView albumArt;

        public SongViewHolder(View itemView) {
            super(itemView);
            songArtist=(TextView) itemView.findViewById(R.id.new_text_view_song_artist);
            songTitle=(TextView)itemView.findViewById(R.id.new_text_view_song_title);
            albumArt=(ImageView)itemView.findViewById(R.id.new_song_image_view);

        }
    }
}
