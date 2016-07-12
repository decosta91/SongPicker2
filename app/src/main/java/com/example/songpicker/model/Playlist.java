package com.example.songpicker.model;


import java.util.List;

public class Playlist {

    /**
     * The unique Id of the playlist
     */
    public String mPlaylistId;

    /**
     * The playlist name
     */
    public String mPlaylistName;

    public List<Song> songList;

    public String albumArt;

    public boolean hasAlbumArt=false;

    public boolean isHasAlbumArt() {
        return hasAlbumArt;
    }

    public void setHasAlbumArt(boolean hasAlbumArt) {
        this.hasAlbumArt = hasAlbumArt;
    }

    /**
     * Constructor of <code>Genre</code>
     * 
     * @param playlistId The Id of the playlist
     * @param playlistName The playlist name
     */
    public Playlist(final String playlistId, final String playlistName) {
        super();
        mPlaylistId = playlistId;
        mPlaylistName = playlistName;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public String getmPlaylistId() {
        return mPlaylistId;
    }

    public void setmPlaylistId(String mPlaylistId) {
        this.mPlaylistId = mPlaylistId;
    }

    public String getmPlaylistName() {
        return mPlaylistName;
    }

    public void setmPlaylistName(String mPlaylistName) {
        this.mPlaylistName = mPlaylistName;
    }
}
