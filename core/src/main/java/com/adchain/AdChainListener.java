package com.adchain;

import android.support.annotation.MainThread;

/**
 * Created by Gust on 19.12.2017.
 */
public class AdChainListener {

    @MainThread
    public void adCompleted(int order, int total, boolean isLastAd){}

    @MainThread
    public void adClicked(Class<? extends AdChainAdapter> type){}

    @MainThread
    public void adLoaded(Class<? extends AdChainAdapter> type){}
}
