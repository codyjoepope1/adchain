package com.adchain;

/**
 * Created by Gust on 19.12.2017.
 */
public interface IAdCallback {
    void init();

    boolean isAdLoaded();

    void showAd();

    void destroy();

}
