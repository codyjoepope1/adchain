package com.adchain;

import android.app.Activity;


/**
 * Created by Gust on 19.12.2017.
 */
public abstract class AdChainAdapter implements IAdChain, IAdCallback {
    protected AdChain rootChain;
    private IAdChain next;
    private AdConfiguration adConfiguration;

    private boolean isClosed;
    private boolean isDisplayed;
    private boolean timedOut;

    public AdChainAdapter(AdConfiguration adConfiguration) {
        this.adConfiguration = adConfiguration;
    }

    @Override
    public final void setNextAd(IAdChain nextAdChain) {
        if (this.next == null)
            this.next = nextAdChain;
        else
            this.next.setNextAd(nextAdChain);
    }

    public final void startChain() {
        if (this.rootChain.hasNextStepBarrier())
            return;
        if (isClosed) {
            if (next != null) {
                next.startChain();
            } else {
                rootChain.setNextStepBarrier(true); // stop all other startChain calls

                rootChain.startActivity();
                rootChain.finishActivity();

                rootChain.reloadChain();
            }
        } else {
            if (isDisplayed)
                return;

            if (isAdLoaded()) {
                log("displaying");
                this.rootChain.increaseDisplayedAdCount();
                isDisplayed = true;
                showAd();

                if (rootChain.isStepByStepMode() && !rootChain.isLastAd())
                    rootChain.setNextStepBarrier(true);
            } else {
                if (rootChain.isStepByStepMode()) {
                    timedOut = true;
                }
                if (timedOut) {
                    boolean realTimeout = !this.isClosed; // real timeout or error event
                    this.isClosed = true;

                    this.rootChain.increaseDisplayedAdCount();
                    if (realTimeout) {
                        loge("timeout. Ad is cancelled."); // real timeout
                        this.rootChain.triggerAdChainListener();
                    }
                    this.rootChain.startChain();
                } else
                    log("not loaded yet, let me check again.");
            }
        }
    }

    public final void timeout() {
        timedOut = true;
        if (next != null) {
            next.timeout();
        }
    }

    public final void destroyChain() {
        if (!isClosed)
            this.rootChain.increaseDisplayedAdCount();
        isClosed = true;
        try {
            destroy();
        } catch (Throwable e) {
            loge(e.getMessage());
        }
        if (next != null) {
            next.destroyChain();
        }
    }


    public final void initChain() {
        isClosed = false;
        isDisplayed = false;
        init();
        if (next != null) {
            next.initChain();
        }
    }


    public final void loaded() {
        if (this.isClosed)
            return;
        log("loaded");
        if (this.adConfiguration != null && !this.adConfiguration.showAd()) {
            log("will not shown (It is disabled from configuration).");
            isClosed = true;
            this.rootChain.increaseDisplayedAdCount();
        }

        this.rootChain.startChain();
    }

    public final void error(String message) {
        if (this.isClosed)
            return;
        loge("error: " + (message == null ? "" : message));

        this.timedOut = true;
        this.isClosed = true;
//        this.rootChain.increaseDisplayedAdCount();
        this.rootChain.triggerAdChainListener();
        this.rootChain.startChain();
    }

    public final void closed() {
        if (this.isClosed)
            return;
        log("closed");

        isClosed = true;
        this.rootChain.triggerAdChainListener();
        this.rootChain.startChain();
    }

    void setRootChain(AdChain rootChain) {
        this.rootChain = rootChain;
    }

    public void log(String message) {
        rootChain.log(getClass().getSimpleName() + getExtra() + " " + message);
    }

    public void loge(String message) {
        rootChain.loge(getClass().getSimpleName() + getExtra() + " " + message);
    }

    public void logv(String message) {
        rootChain.logv(getClass().getSimpleName() + getExtra() + " " + message);
    }

    private String getExtra() {
        if (this instanceof TestAdAdapter) {
            return ((TestAdAdapter) this).name;
        }
        return "";
    }

    public Activity getActivity() {
        return rootChain.getActivity();
    }

    protected boolean isLoggingEnabled() {
        return rootChain.isLoggingEnabled();
    }

    protected boolean isShowTestAds() {
        return rootChain.isShowTestAds();
    }

}
