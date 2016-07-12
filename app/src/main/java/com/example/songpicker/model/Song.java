package com.example.songpicker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by StanoevskiDejan on 6/28/2016.
 */

public class Song implements Parcelable{
    private String title;

    private String data;

    private String artist;

    private  long album_id;

    private long duration;

    public Song() {
    }

    public Song(String title, String data, String artist, long duration,long albumid) {
        this.title = title;
        this.data = data;
        this.artist = artist;
        this.duration = duration;
        this.album_id=albumid;
    }

    public Song(String title, String artist, long duration, long album_id) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.album_id=album_id;
    }

    protected Song(Parcel in) {
        title = in.readString();
        data = in.readString();
        artist = in.readString();
        album_id = in.readLong();
        duration = in.readLong();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(data);
        dest.writeString(artist);
        dest.writeLong(album_id);
        dest.writeLong(duration);
    }
}
