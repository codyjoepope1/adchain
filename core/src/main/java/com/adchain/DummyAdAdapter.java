package com.adchain;

import android.os.Handler;

/**
 * Created by a on 19.12.2017.
 */
public class DummyAdAdapter extends AdChainAdapter {
    private boolean loaded;

    public static DummyAdAdapter create() {
        return new DummyAdAdapter(null);
    }

    private DummyAdAdapter(AdConfiguration adConfiguration) {
        super(adConfiguration);
    }

    @Override
    public void init() {
        loaded = true;
        loaded();
    }


    @Override
    public boolean isAdLoaded() {
        return loaded;
    }

    @Override
    public void showAd() {
        loaded = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                closed();
            }
        }, 250);
    }

    @Override
    public void destroy() {
    }

}
