package it.pedrazzi.marco.savemyphoto.Galleria;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCachePhoto
{

    private final static double PERC = 0.2; //Impostare quanta memoria viene riservata alla cache dei media
    private LruCache<Long, Bitmap> mMemoryCache;

    public MemoryCachePhoto()
    {
        // Ottiene la massima disponibilità di memoria dalla virtual machine,
        // se viene superata viene lanciata un eccezione che viene gestita dalla classe Lrucache
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        final int cacheSize = (int) (maxMemory * PERC);

        mMemoryCache = new LruCache<Long, Bitmap>(cacheSize)
        {
            @Override
            protected int sizeOf(Long key, Bitmap bitmap)
            {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    //ritorna la bitmap dalla cache
    public Bitmap get(long lng)
    {
        synchronized (mMemoryCache) //evita accessi concorrenziali thread lock
        {
            return mMemoryCache.get(lng);
        }
    }

    //mette un bitmap nella cache
    public void put(Long lng, Bitmap bitmap)
    {
        synchronized (mMemoryCache) //evita accessi concorrenziali thread lock
        {
            if (mMemoryCache.get(lng) == null) {
                mMemoryCache.put(lng, bitmap);
            }
        }
    }

    public void Clear()
    {
        mMemoryCache.evictAll();
    }


}