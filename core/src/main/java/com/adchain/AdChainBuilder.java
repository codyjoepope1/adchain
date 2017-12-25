package com.adchain;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.util.Log;
import android.view.View;

/**
 * Created by Gust on 19.12.2017.
 */
public final class AdChainBuilder {
    private AdChain adc;
    private boolean autoShow;
    private boolean reloadable;
    private Long timeoutLimit = 15000L;

    public AdChainBuilder(Activity context) {
        adc = new AdChain(context);
        boolean debug = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        setLogging(debug);
    }

    public AdChain build() {
        if (adc.getIntent() != null && this.reloadable) {
            adc.loge("Its not allowed to use 'startActivityAfterAdsFinished' and 'reloadAfterChainFinished' together. Reloadable property ignored.");
        } else if (this.reloadable) {
            adc.setReloadable(true);
        }

        if (adc.getAdChain() == null) {
            Log.e(adc.TAG, "No AdChainAdapter found, all might be set as AdConfiguration=false.");
        } else {
            adc.setTimeout(timeoutLimit);
            adc.initChain();
        }

        if (autoShow)
            adc.showAds();
        return adc;
    }

    /**
     * logging feature of library. default value is enabled for debug, disabled for prod.
     */
    public AdChainBuilder setLogging(boolean enable) {
        if (adc.getAdChain() != null) {
            Log.e(adc.TAG, "call setLogging function before add()");
        }
        adc.setLoggingEnabled(enable);
        return this;
    }


    public AdChainBuilder setAdChainListener(AdChainListener adChainListener) {
        adc.setAdChainListener(adChainListener);
        return this;
    }


    /**
     * add interstitial ads within order.
     * NOTE: call this method before 'build()' method
     */
    public AdChainBuilder add(AdChainAdapter nextAdChain) {
        if (nextAdChain != null) {
            nextAdChain.setRootChain(adc);
//            nextAdChain.init();
            adc.increaseAdCountTotal();
            if (adc.getAdChain() == null)
                adc.setAdChain(nextAdChain);
            else
                adc.getAdChain().setNextAd(nextAdChain);
        }
        return this;
    }

    /**
     * starts activity with intent before last ad displaying
     */
    public AdChainBuilder startActivityAfterAdsFinished(Intent intent) {
        adc.setIntent(intent);
        return this;
    }

    /**
     * starts activity before last ad displaying
     */
    public AdChainBuilder startActivityAfterAdsFinished(Class nextActivityClass) {
        adc.setIntent(new Intent(adc.getActivity(), nextActivityClass));
        return this;
    }


    /**
     * trigger show method automatically
     */
    public AdChainBuilder showAuto() {
        this.autoShow = true;
        return this;
    }

    /**
     * show one ad per 'showAds()' call. After all ads; if reloadable, reload chain again.
     */
    public AdChainBuilder withStepByStep(boolean reloadable) {
        adc.setStepByStepMode(true);

        this.reloadable = reloadable;
        return this;
    }

    /**
     * show one ad per 'showAds()' call.
     */
    public AdChainBuilder withStepByStep() {
        return withStepByStep(false);
    }


    /**
     * set views clickListener and shows ads when clicked
     */
    public AdChainBuilder showOnClick(int triggerViewId, long enableDelay) {
        final View view = adc.getActivity().findViewById(triggerViewId);
        view.setVisibility(View.INVISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adc.isStepByStepMode()) {
                    view.setEnabled(false);
                    view.setVisibility(View.INVISIBLE);
                }
                adc.showAds();
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                view.setVisibility(View.VISIBLE);
            }
        }, enableDelay);
        return this;
    }


    /**
     * in millisecond, default value is 15000.
     * 0 for no timeout
     */
    public AdChainBuilder timeout(long timeoutMili) {
        if (timeoutMili < 1000)
            adc.log("The value you enter for timeout is too small. You must enter the time in milliseconds.");
        this.timeoutLimit = timeoutMili;
        return this;
    }
}