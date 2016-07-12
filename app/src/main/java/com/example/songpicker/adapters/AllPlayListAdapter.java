package com.example.songpicker.adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.songpicker.R;
import com.example.songpicker.SongsList;
import com.example.songpicker.model.*;
import com.example.songpicker.utils.ImageLoader.ImageExtractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StanoevskiDejan on 7/2/2016.
 */

public class AllPlayListAdapter extends RecyclerView.Adapter<AllPlayListAdapter.PlayListViewHolder>{
    List<Playlist> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private ImageExtractor mExtractor;

    public AllPlayListAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        layoutInflater=LayoutInflater.from(context);
    }

    public void setImageExtractor(ImageExtractor mExtractor) {
        this.mExtractor = mExtractor;
    }

    @Override
    public PlayListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.artist_cardview,parent,false);
        PlayListViewHolder holder=new PlayListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PlayListViewHolder holder, int position) {
        Playlist current=data.get(position);
        holder.setData(current,position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(Playlist playlist){
        data.add(playlist);
        notifyDataSetChanged();
    }

    public class PlayListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView playListName;
        ImageView playListArt;
        int position;
        Playlist curPlaylist;

        public PlayListViewHolder(View itemView) {
            super(itemView);
            playListName=(TextView)itemView.findViewById(R.id.artist_name);
            playListArt=(ImageView)itemView.findViewById(R.id.artist_image);
            itemView.setOnClickListener(this);
        }

        public void setData(Playlist current, int position) {
            this.playListName.setText(current.getmPlaylistName());
            if(current.hasAlbumArt)
                mExtractor.loadImage(current.getAlbumArt(), this.playListArt);
                //Picasso.with(context).load(current.getData_art()).into(this.albumArt);
            else
                // mExtractor.loadImage(R.drawable.song_art,this.albumArt);
                this.playListArt.setImageResource(R.drawable.song_art);
            this.position=position;
            this.curPlaylist=current;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,SongsList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            ArrayList<Song> lista=new ArrayList<>();
            lista.addAll(curPlaylist.getSongList());
            bundle.putParcelableArrayList("data",lista);
            bundle.putString("image",curPlaylist.getAlbumArt());
            bundle.putString("name",curPlaylist.getmPlaylistName());
            intent.putExtras(bundle);
            context.getApplicationContext().startActivity(intent);
        }
    }
}
