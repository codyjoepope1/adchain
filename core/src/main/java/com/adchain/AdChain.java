package com.adchain;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


/**
 * Created by Gust on 30.01.2017.
 */
public class AdChain {
    String TAG = "AdChain";

    private Object startChainLock = new Object();

    private Activity context;
    private IAdChain adChain;

    private boolean nextStepBarrier;
    private boolean stepByStepMode;
    private boolean penultimateAdClosed;
    private boolean reloadable;

    private AdChainListener adChainListener;

    private boolean loggingEnabled;

    private int totalAdCount;
    private int displayedAdCount;
    private Intent intent;

    private Runnable timeoutRunnable;
    private Handler timeoutTimer;
    private long timeout;

    AdChain(Activity context) {
        this.context = context;
        this.totalAdCount = 0;
        this.displayedAdCount = 0;
        this.nextStepBarrier = true;
        TAG = "AdChain_" + context.getClass().getSimpleName();

        context.getApplication().registerActivityLifecycleCallbacks(LIFECYCLE_CALLBACKS);
        log("IAdChain init");
    }


    public void showAds() {
        log("starting Ad Chain");
        nextStepBarrier = false;

        if (adChain == null) {
            startActivity();
            finishActivity();
            triggerAdChainListener();
            return;
        }

        startChain();
    }

    void startActivity() {
        if (!penultimateAdClosed && !reloadable) {
            penultimateAdClosed = true;

            if (intent != null) {
                logv("starting next activity");
                getActivity().startActivity(intent);
            }
        }
    }

    void finishActivity() {
        if (intent != null && !reloadable) {
            logv("finishing current activity");
            getActivity().finish();
        }
    }

    void log(String message) {
        if (loggingEnabled)
            Log.d(TAG, message);
    }

    void loge(String message) {
        Log.e(TAG, message);
    }

    void logv(String message) {
        if (loggingEnabled)
            Log.v(TAG, message);
    }

    void triggerAdChainListener() {
        if (this.adChainListener != null) {
            this.adChainListener.adCompleted(displayedAdCount, totalAdCount, isLastAd());
        }
    }

    void increaseAdCountTotal() {
        this.totalAdCount++;
    }

    void increaseDisplayedAdCount() {
        this.displayedAdCount++;
        if (!hasNextStepBarrier() && isLastAd()) {
            startActivity();
        }
        log("Ad position: " + displayedAdCount + "/" + totalAdCount);
    }

    private boolean isActivityEquals(Activity activity) {
        return activity.getClass().getName().equals(getActivity().getClass().getName());
    }

    boolean isLastAd() {
        return displayedAdCount == totalAdCount;
    }


    void setAdChainListener(AdChainListener adChainListener) {
        this.adChainListener = adChainListener;
    }

    void startChain() {
        synchronized (startChainLock) {
            this.adChain.startChain();
        }
    }

    void reloadChain() {
        if (reloadable) {
            nextStepBarrier = true; // run only step by step mode now.
            adChain.destroyChain();
            displayedAdCount = 0;
            adChain.initChain();
        }
    }


    Activity getActivity() {
        return this.context;
    }

    IAdChain getAdChain() {
        return this.adChain;
    }


    void setAdChain(IAdChain adChain) {
        this.adChain = adChain;
    }


    void setNextStepBarrier(boolean nextStepBarrier) {
        this.nextStepBarrier = nextStepBarrier;
    }

    boolean hasNextStepBarrier() {
        return this.nextStepBarrier;
    }


    void setStepByStepMode(boolean stepByStepMode) {
        this.stepByStepMode = stepByStepMode;
    }

    boolean isStepByStepMode() {
        return stepByStepMode;
    }

    Intent getIntent() {
        return this.intent;
    }

    void setIntent(Intent intent) {
        this.intent = intent;
    }

    void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }

    private final Application.ActivityLifecycleCallbacks LIFECYCLE_CALLBACKS = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }


        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (isActivityEquals(activity)) {
                if (adChain != null) {
                    Log.d(TAG, "destroy()");
                    adChain.destroyChain();
                }
                context.getApplication().unregisterActivityLifecycleCallbacks(LIFECYCLE_CALLBACKS);
            }
        }
    };

    void setReloadable(boolean reloadable) {
        this.reloadable = reloadable;
    }

    void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getTimeout() {
        return this.timeout;
    }

    void initChain() {
        log("AdChain init");
        adChain.initChain();
        log("AdChain init2");
        startTimeoutTimer();
    }

    private void stopTimeoutTimer() {
        if (timeoutRunnable != null && timeoutTimer != null) {
            timeoutTimer.removeCallbacks(timeoutRunnable);
        }
    }

    private void startTimeoutTimer() {
        stopTimeoutTimer();
        if (timeout > 0) {
            timeoutTimer = new Handler();
            timeoutRunnable = new Runnable() {
                @Override
                public void run() {
                    adChain.timeout();
                    startChain();
                }
            };
            timeoutTimer.postDelayed(timeoutRunnable, this.timeout);
        }
    }
}
