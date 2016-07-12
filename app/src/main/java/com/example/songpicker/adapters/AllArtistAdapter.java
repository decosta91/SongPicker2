package com.example.songpicker.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.songpicker.R;
import com.example.songpicker.SongsList;
import com.example.songpicker.model.Artist;
import com.example.songpicker.model.Song;
import com.example.songpicker.utils.ImageLoader.ImageExtractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StanoevskiDejan on 6/29/2016.
 */

public class AllArtistAdapter extends RecyclerView.Adapter<AllArtistAdapter.SongViewHolder> {
    List<Artist> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private ImageExtractor mExtractor;

    public AllArtistAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }

    public void setImageExtractor(ImageExtractor extractor) {
        this.mExtractor = extractor;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Informacija","AllArtist-createView");
        View view=layoutInflater.inflate(R.layout.artist_cardview,parent,false);
        SongViewHolder holder=new SongViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Log.d("Informacija","AllArtist-onBindView");
        Artist current=data.get(position);
        holder.setData(current,position);
       // holder.setListener();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(Artist artist){
        data.add(artist);
        notifyDataSetChanged();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameArtist;
        ImageView albumArt;
        int position;
        Artist curArtist;

        public SongViewHolder(View itemView) {
            super(itemView);
            nameArtist=(TextView) itemView.findViewById(R.id.artist_name);
            albumArt=(ImageView)itemView.findViewById(R.id.artist_image);
            itemView.setOnClickListener(this);
        }

        public void setData(Artist current, int position) {
            this.nameArtist.setText(current.getmArtistName());
            if(current.hasAlbumArt)
            mExtractor.loadImage(current.getData_art(), this.albumArt);
       //     Picasso.with(context).load(current.getData_art()).into(this.albumArt);
            else
         //   mExtractor.loadImage(R.drawable.song_art,this.albumArt);
            this.albumArt.setImageResource(R.drawable.song_art);
            this.position=position;
            this.curArtist=current;
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,SongsList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            ArrayList<Song> lista=new ArrayList<>();
            lista.addAll(curArtist.getSongs());
            bundle.putParcelableArrayList("data",lista);
            bundle.putString("image",curArtist.getData_art());
            bundle.putString("name",curArtist.getmArtistName());
           // bundle.putParcelable("data", curArtist.getSongs());
            intent.putExtras(bundle);
            //startActivity(intent);
            context.getApplicationContext().startActivity(intent);
        }
    }
}
