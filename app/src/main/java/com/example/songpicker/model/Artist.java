package com.example.songpicker.model;

import java.util.List;

public class Artist {

    /**
     * The unique Id of the artist
     */
    public String mArtistId;

    /**
     * The artist name
     */
    public String mArtistName;



    public List<Song> songs;

    public String data_art;

    public boolean hasAlbumArt=false;

    public boolean isHasAlbumArt() {
        return hasAlbumArt;
    }

    public void setHasAlbumArt(boolean hasAlbumArt) {
        this.hasAlbumArt = hasAlbumArt;
    }

    public Artist(final String artistId, final String artistName) {
        super();
        mArtistId = artistId;
        mArtistName = artistName;

    }

    public String getData_art() {
        return data_art;
    }

    public void setData_art(String data_art) {
        this.data_art = data_art;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getmArtistId() {
        return mArtistId;
    }

    public void setmArtistId(String mArtistId) {
        this.mArtistId = mArtistId;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public void setmArtistName(String mArtistName) {
        this.mArtistName = mArtistName;
    }



}
