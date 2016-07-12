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
import com.example.songpicker.model.Genre;
import com.example.songpicker.model.Song;
import com.example.songpicker.utils.ImageLoader.ImageExtractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StanoevskiDejan on 6/30/2016.
 */

public class AllGenresAdapter extends RecyclerView.Adapter<AllGenresAdapter.GenresHolder> {
    List<Genre> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private ImageExtractor mExtractor;


    public AllGenresAdapter(Context context) {
        this.data=new ArrayList<>();
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
    }

    public void setImageExtractor(ImageExtractor extractor) {
        this.mExtractor = extractor;
    }

    @Override
    public AllGenresAdapter.GenresHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Informacija","AllGenres-oncreateView");
        View view=layoutInflater.inflate(R.layout.artist_cardview,parent,false);
        GenresHolder holder=new GenresHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AllGenresAdapter.GenresHolder holder, int position) {
        Log.d("Informacija","AllGenres-onBindView");
        Genre current=data.get(position);
        holder.setData(current,position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(Genre genre){
        data.add(genre);
        notifyDataSetChanged();
    }

    public class GenresHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameGenre;
        ImageView albumArt;
        int position;
        Genre curGenre;

        public GenresHolder(View itemView) {
            super(itemView);
            nameGenre=(TextView) itemView.findViewById(R.id.artist_name);
            albumArt=(ImageView)itemView.findViewById(R.id.artist_image);
            itemView.setOnClickListener(this);
        }

        public void setData(Genre current, int position) {
            this.nameGenre.setText(current.getmGenreName());
            if(current.hasAlbumArt)
                mExtractor.loadImage(current.getData_art(), this.albumArt);
            //Picasso.with(context).load(current.getData_art()).into(this.albumArt);
            else
               // mExtractor.loadImage(R.drawable.song_art,this.albumArt);
            this.albumArt.setImageResource(R.drawable.song_art);
            this.position=position;
            this.curGenre=current;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,SongsList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            ArrayList<Song> lista=new ArrayList<>();
            lista.addAll(curGenre.getSongs());
            bundle.putParcelableArrayList("data",lista);
            bundle.putString("image",curGenre.getData_art());
            bundle.putString("name",curGenre.getmGenreName());
            // bundle.putParcelable("data", curArtist.getSongs());
            intent.putExtras(bundle);
            //startActivity(intent);
            context.getApplicationContext().startActivity(intent);
        }
    }
}
