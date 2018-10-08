package com.adchain;

import android.support.annotation.MainThread;

/**
 * Created by Gust on 19.12.2017.
 */
public class AdChainListener {

    public enum ComplationType {
        NULL_CHAIN, TIMEOUT, CLOSED, ERROR
    }

    @MainThread
    public void adCompleted(Class<? extends AdChainAdapter> adapter, ComplationType type, int order, int total, boolean isLastAd){}

    @MainThread
    public void adClicked(Class<? extends AdChainAdapter> adapter){}

    @MainThread
    public void adLoaded(Class<? extends AdChainAdapter> adapter){}
}
