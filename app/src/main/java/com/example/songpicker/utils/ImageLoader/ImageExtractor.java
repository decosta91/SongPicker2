package com.example.songpicker.utils.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by mdocevski on 23.02.2016.
 */
public class ImageExtractor extends ImageResizer {
    private static final String TAG = "ImageExtractor";
    private static final int _DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String _DISK_CACHE_DIR = "disk";
    private static final int _IO_BUFFER_SIZE = 8 * 1024;

    private DiskLruCache mDiskCache;
    private File mCacheDir;
    private boolean mDiskCacheStarting = true;
    private final Object mDiskCacheLock = new Object();
    private static final int DISK_CACHE_INDEX = 0;

    /**
     * Initialize providing a target image width and height for the processing images and
     * target cache directory.
     *
     * @param context
     * @param imageWidth
     * @param imageHeight
     * @param DISK_CACHE_DIR
     */
    public ImageExtractor(Context context, int imageWidth, int imageHeight, String DISK_CACHE_DIR) {
        super(context, imageWidth, imageHeight);
        init(context, DISK_CACHE_DIR);
    }

    /**
     * Initialize providing a target image width and height for the processing images with default
     * disk cache directory.
     *
     * @param context
     * @param imageWidth
     * @param imageHeight
     */
    public ImageExtractor(Context context, int imageWidth, int imageHeight) {
        super(context, imageWidth, imageHeight);
        init(context, _DISK_CACHE_DIR);
    }

    /**
     * Initialize providing a single target image size (used for both width and height) and
     * target cache directory.
     *
     * @param context
     * @param imageSize
     * @param DISK_CACHE_DIR
     */
    public ImageExtractor(Context context, int imageSize, String DISK_CACHE_DIR) {
        super(context, imageSize);
        init(context, DISK_CACHE_DIR);
    }

    /**
     * Initialize providing a single target image size (used for both width and height) with default
     * disk cache directory ;
     *
     * @param context
     * @param imageSize
     */
    public ImageExtractor(Context context, int imageSize) {
        super(context, imageSize);
        init(context, _DISK_CACHE_DIR);
    }


    private void init(Context context, String DISK_CACHE_DIR) {
        mCacheDir = ImageCache.getDiskCacheDir(context, DISK_CACHE_DIR);
    }

    @Override
    protected void initDiskCacheInternal() {
        super.initDiskCacheInternal();
        initDiskCache();
    }

    private void initDiskCache() {
        if (!mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }
        synchronized (mDiskCacheLock) {
            if (ImageCache.getUsableSpace(mCacheDir) > _DISK_CACHE_SIZE) {
                try {
                    mDiskCache = DiskLruCache.open(mCacheDir, 1, 1, _DISK_CACHE_SIZE);
                } catch (IOException e) {
                    mDiskCache = null;
                }
            }
            mDiskCacheStarting = false;
            mDiskCacheLock.notifyAll();
        }
    }


    @Override
    protected void clearCacheInternal() {
        super.clearCacheInternal();
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null && !mDiskCache.isClosed()) {
                try {
                    mDiskCache.delete();
//                    if (BuildConfig.DEBUG) {
//                        Log.d(TAG, "HTTP cache cleared");
//                    }
                } catch (IOException e) {
                    Log.e(TAG, "clearCacheInternal - " + e);
                }
                mDiskCache = null;
                mDiskCacheStarting = true;
                initDiskCache();
            }
        }
    }

    @Override
    protected void flushCacheInternal() {
        super.flushCacheInternal();
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null) {
                try {
                    mDiskCache.flush();
//                    if (BuildConfig.DEBUG) {
//                        Log.d(TAG, "HTTP cache flushed");
//                    }
                } catch (IOException e) {
                    Log.e(TAG, "flush - " + e);
                }
            }
        }
    }

    @Override
    protected void closeCacheInternal() {
        super.closeCacheInternal();
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null) {
                try {
                    if (!mDiskCache.isClosed()) {
                        mDiskCache.close();
                        mDiskCache = null;
//                        if (BuildConfig.DEBUG) {
//                            Log.d(TAG, "HTTP cache closed");
//                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "closeCacheInternal - " + e);
                }
            }
        }
    }

    /**
     * The main process method, which will be called by the ImageWorker in the AsyncTask background
     * thread.
     *
     * @param data The data to load the bitmap, in this case, a song resource local URI
     * @return The extracted song thumbnail
     */
    private Bitmap processBitmap(String data) {
//        if (BuildConfig.DEBUG) {
//            Log.d(TAG, "processBitmap - " + data);
//        }
        final String key = ImageCache.hashKeyForDisk(data);
        FileDescriptor fileDescriptor = null;
        FileInputStream fileInputStream = null;
        DiskLruCache.Snapshot snapshot;
        synchronized (mDiskCacheLock) {
            // Wait for disk cache to initialize
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {}
            }

            if (mDiskCache != null) {
                try {
                    snapshot = mDiskCache.get(key);
                    if (snapshot == null) {
//                        if (BuildConfig.DEBUG) {
//                            Log.d(TAG, "processBitmap, not found in  cache, extracting from audio file...");
//                        }
                        DiskLruCache.Editor editor = mDiskCache.edit(key);
                        if (editor != null) {
                            try {
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(data);
                                editor.newOutputStream(DISK_CACHE_INDEX)
                                        .write(retriever.getEmbeddedPicture());
                                editor.commit();
                            }catch (Exception e){
                                editor.abort();
                            }
                        }
                        snapshot = mDiskCache.get(key);
                    }
                    if (snapshot != null) {
                        fileInputStream =
                                (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                        fileDescriptor = fileInputStream.getFD();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "processBitmap - " + e);
                } catch (IllegalStateException e) {
                    Log.e(TAG, "processBitmap - " + e);
                } finally {
                    if (fileDescriptor == null && fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {}
                    }
                }
            }
        }

        Bitmap bitmap = null;
        if (fileDescriptor != null) {
            bitmap = decodeSampledBitmapFromDescriptor(fileDescriptor, mImageWidth,
                    mImageHeight, getImageCache());
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {}
        }
        return bitmap;
    }

    @Override
    protected Bitmap processBitmap(Object data) {
        return processBitmap(String.valueOf(data));
    }
}
