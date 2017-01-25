package me.makeachoice.library.android.base.view.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class NetworkImageLoaderHelper {
    private static NetworkImageLoaderHelper sInstance;

    public static NetworkImageLoaderHelper getInstance(Context context) {
        //Log.d("Choice", "ImageLoaderHelper.getInstance");
        if (sInstance == null) {
            //Log.d("Choice", "     instance NULL");
            sInstance = new NetworkImageLoaderHelper(context.getApplicationContext());
        }

        //Log.d("Choice", "     return instance");
        return sInstance;
    }

    private final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(20);
    private ImageLoader mImageLoader;

    private NetworkImageLoaderHelper(Context applicationContext) {
        RequestQueue queue = Volley.newRequestQueue(applicationContext);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                mImageCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return mImageCache.get(key);
            }
        };
        mImageLoader = new ImageLoader(queue, imageCache);


    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
