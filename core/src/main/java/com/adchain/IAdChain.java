package com.adchain;

import android.support.annotation.WorkerThread;

/**
 * Created by Gust on 19.12.2017.
 */
public interface IAdChain {
    @WorkerThread
    void setNextAd(IAdChain nextAdChain);

    @WorkerThread
    void startChain();

    @WorkerThread
    void timeout();

    @WorkerThread
    void destroyChain();

    @WorkerThread
    void initChain();

}
