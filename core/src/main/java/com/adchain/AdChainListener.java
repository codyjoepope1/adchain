package com.adchain;

import android.support.annotation.MainThread;

/**
 * Created by Gust on 19.12.2017.
 */
public class AdChainListener {

    @MainThread
    void adCompleted(int order, int total, boolean isLastAd){}

    @MainThread
    void adClicked(Class<? extends AdChainAdapter> type){}

    @MainThread
    void adLoaded(Class<? extends AdChainAdapter> type){}
}
