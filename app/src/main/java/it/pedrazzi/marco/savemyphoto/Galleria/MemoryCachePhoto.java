package it.pedrazzi.marco.savemyphoto.Galleria;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCachePhoto {//TODO Rivedere e Commentare!!!

    // Set how much memory is reserved for the cache of the images
    private final static double PERC = 0.2;
    private LruCache<Long, Bitmap> mMemoryCache;

    public MemoryCachePhoto() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        final int cacheSize = (int) (maxMemory * PERC);

        mMemoryCache = new LruCache<Long, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Long key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public Bitmap get(long lng) {
        return mMemoryCache.get(lng);
    }

    public void put(Long lng, Bitmap bitmap) {
        if (get(lng) == null) {
            mMemoryCache.put(lng, bitmap);
        }
    }

}