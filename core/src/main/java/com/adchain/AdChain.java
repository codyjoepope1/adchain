package com.adchain;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.view.View;

import com.adchain.utils.AppExecutors;


/**
 * Created by Gust on 30.01.2017.
 */
public class AdChain {
    String TAG = "AdChain";

    private final Object startChainLock = new Object();
    AppExecutors appExecutors = new AppExecutors();

    private Activity context;
    private IAdChain adChain;

    private boolean nextStepBarrier;
    private boolean stepByStepMode;
    private boolean penultimateAdClosed;
    private boolean reloadable;

    private AdChainListener adChainListener;

    private boolean loggingEnabled;
    private boolean showTestAds;

    private int totalAdCount;
    private int displayedAdCount;
    private Intent intent;

    private Runnable timeoutRunnable;
    private Handler timeoutTimer;
    private long timeout;

    private View clickView;

    AdChain(Activity context) {
        this.context = context;
        this.totalAdCount = 0;
        this.displayedAdCount = 0;
        this.nextStepBarrier = true;
        TAG = "AdChain_" + context.getClass().getSimpleName();

        context.getApplication().registerActivityLifecycleCallbacks(LIFECYCLE_CALLBACKS);
        log("IAdChain init");
    }


    @MainThread
    public void showAds() {
        log("starting Ad Chain");
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                nextStepBarrier = false;

                if (adChain == null) {
                    startActivity();
                    finishActivity();
                    triggerAdChainListener();
                    return;
                }

                startChain();

            }
        });
    }

    @WorkerThread
    void startActivity() {
        if (!penultimateAdClosed && !reloadable) {
            penultimateAdClosed = true;

            if (intent != null) {
                logv("starting next activity");
                getActivity().startActivity(intent); // todo
            }
        }
    }

    @WorkerThread
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

    @WorkerThread
    void triggerAdChainListener() {
        if (this.adChainListener != null) {
            appExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    AdChain.this.adChainListener.adCompleted(displayedAdCount, totalAdCount, isLastAd());
                }
            });
        }
    }

    void increaseAdCountTotal() {
        this.totalAdCount++;
    }
    long getAdCountTotal() {
        return this.totalAdCount;
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

    @WorkerThread
    void startChain() {
        synchronized (startChainLock) {
            this.adChain.startChain();
        }
    }

    @WorkerThread
    void reloadChain() {
        if (reloadable) {
            log("Reloading all ads.");
            nextStepBarrier = true; // run only step by step mode now.
            adChain.destroyChain();
            displayedAdCount = 0;
            adChain.initChain();
        }
    }

    void setClickViewAsVisible() {
        if (clickView != null) {
            clickView.setVisibility(View.VISIBLE);
            clickView = null; // set as visible just one time
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

    public void setClickView(View clickView) {
        this.clickView = clickView;
    }

    void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }
    boolean isShowTestAds() {
        return this.showTestAds;
    }

    void setShowTestAds(boolean showTestAds) {
        this.showTestAds = showTestAds;
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
                    setClickViewAsVisible();
                    startChain();
                }
            };
            timeoutTimer.postDelayed(timeoutRunnable, this.timeout);
        }
    }
}
