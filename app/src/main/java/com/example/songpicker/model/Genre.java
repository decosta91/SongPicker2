package com.example.songpicker.model;

import java.util.List;

public class Genre {

    /**
     * The unique Id of the genre
     */
    public String mGenreId;

    /**
     * The genre name
     */
    public String mGenreName;

    public long album_art;

    public List<Song> songs;

    public String data_art;

    public boolean hasAlbumArt=false;

    public String getData_art() {
        return data_art;
    }

    public void setData_art(String data_art) {
        this.data_art = data_art;
    }

    public boolean isHasAlbumArt() {
        return hasAlbumArt;
    }

    public void setHasAlbumArt(boolean hasAlbumArt) {
        this.hasAlbumArt = hasAlbumArt;
    }

    public List<Song> getSongs() {

        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Genre(final String genreId, final String genreName) {
        super();
        mGenreId = genreId;
        mGenreName = genreName;
    }


    public long getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(long album_art) {
        this.album_art = album_art;
    }

    public String getmGenreId() {
        return mGenreId;
    }

    public void setmGenreId(String mGenreId) {
        this.mGenreId = mGenreId;
    }

    public String getmGenreName() {
        return mGenreName;
    }

    public void setmGenreName(String mGenreName) {
        this.mGenreName = mGenreName;
    }
}
