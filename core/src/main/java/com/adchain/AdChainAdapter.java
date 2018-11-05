package com.adchain;

import android.app.Activity;
import android.support.annotation.MainThread;


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
    private boolean loaded;

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

            if (loaded) {
                log("displaying");
                this.rootChain.increaseDisplayedAdCount(true);
                isDisplayed = true;
                rootChain.appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isAdLoaded()) {
                            showAd();
                        }else {
                            loge("triggered loaded but not loaded");
                        }
                    }
                });

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
                        this.rootChain.triggerAdChainListener(getClass(), AdChainListener.ComplationType.TIMEOUT);
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
            rootChain.appExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    destroy();
                }
            });
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
        rootChain.appExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
        if (next != null) {
            next.initChain();
        }
    }

    @MainThread
    public final void clicked() {
        rootChain.adClicked(getClass());
    }

    @MainThread
    public final void loaded() {
        if (this.isClosed)
            return;

        this.loaded = true;

        rootChain.adLoaded(getClass());
        rootChain.setClickViewAsVisible();
        rootChain.appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                log("loaded");
                if (adConfiguration != null && !adConfiguration.showAd()) {
                    log("will not shown (It is disabled from configuration).");
                    isClosed = true;
                    rootChain.increaseDisplayedAdCount();
                }
                rootChain.startChain();
            }
        });
    }

    @MainThread
    public final void error(final String message) {
        if (this.isClosed)
            return;
        this.loaded = false;

        rootChain.setClickViewAsVisible();
        rootChain.appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                loge("error: " + (message == null ? "" : message));

                timedOut = true;
                isClosed = true;
//        this.rootChain.increaseDisplayedAdCount();
                rootChain.triggerAdChainListener(AdChainAdapter.this.getClass(), AdChainListener.ComplationType.ERROR);
                rootChain.startChain();
            }
        });

    }

    @MainThread
    public final void closed() {
        if (this.isClosed)
            return;
        this.loaded = false;

        rootChain.appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                log("closed");
                isClosed = true;

                rootChain.triggerAdChainListener(AdChainAdapter.this.getClass(), AdChainListener.ComplationType.CLOSED);
                rootChain.startChain();
            }
        });
    }

    void setRootChain(AdChain rootChain) {
        this.rootChain = rootChain;
    }

    public final void log(String message) {
        rootChain.log(getClass().getSimpleName() + getExtra() + " " + message);
    }

    public final void loge(String message) {
        rootChain.loge(getClass().getSimpleName() + getExtra() + " " + message);
    }

    public final void logv(String message) {
        rootChain.logv(getClass().getSimpleName() + getExtra() + " " + message);
    }

    private String getExtra() {
        if (this instanceof TestAdAdapter) {
            return ((TestAdAdapter) this).name;
        }
        return "";
    }

    public final Activity getActivity() {
        return rootChain.getActivity();
    }

    protected final boolean isShowTestAds() {
        return rootChain.isShowTestAds();
    }

}
