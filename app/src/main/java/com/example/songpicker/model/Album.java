package com.example.songpicker.model;


public class Album {

    /**
     * The unique Id of the album
     */
    public String mAlbumId;

    /**
     * The name of the album
     */
    public String mAlbumName;

    /**
     * The album artist
     */
    public String mArtistName;

    /**
     * The number of songs in the album
     */
    public String mSongNumber;

    /**
     * The year the album was released
     */
    public String mYear;

    /**
     * Constructor of <code>Album</code>
     * 
     * @param albumId The Id of the album
     * @param albumName The name of the album
     * @param artistName The album artist
     * @param songNumber The number of songs in the album
     * @param albumYear The year the album was released
     */
    public Album(final String albumId, final String albumName, final String artistName,
            final String songNumber, final String albumYear) {
        super();
        mAlbumId = albumId;
        mAlbumName = albumName;
        mArtistName = artistName;
        mSongNumber = songNumber;
        mYear = albumYear;
    }

    public String getmAlbumId() {
        return mAlbumId;
    }

    public void setmAlbumId(String mAlbumId) {
        this.mAlbumId = mAlbumId;
    }

    public String getmAlbumName() {
        return mAlbumName;
    }

    public void setmAlbumName(String mAlbumName) {
        this.mAlbumName = mAlbumName;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public void setmArtistName(String mArtistName) {
        this.mArtistName = mArtistName;
    }

    public String getmSongNumber() {
        return mSongNumber;
    }

    public void setmSongNumber(String mSongNumber) {
        this.mSongNumber = mSongNumber;
    }

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }
}
