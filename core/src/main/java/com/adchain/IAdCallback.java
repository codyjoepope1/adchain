package com.adchain;

import android.support.annotation.MainThread;

/**
 * Created by Gust on 19.12.2017.
 */
public interface IAdCallback {
    @MainThread
    void init();

    @MainThread
    boolean isAdLoaded();

    @MainThread
    void showAd();

    @MainThread
    void destroy();

}
