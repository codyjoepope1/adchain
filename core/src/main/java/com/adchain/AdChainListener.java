package com.adchain;

import android.support.annotation.MainThread;

/**
 * Created by Gust on 19.12.2017.
 */
public interface AdChainListener {
    //    void lastAdClosed();
//    void penultimateAdClosed();
    @MainThread
    void adCompleted(int order, int total, boolean isLastAd);
}
