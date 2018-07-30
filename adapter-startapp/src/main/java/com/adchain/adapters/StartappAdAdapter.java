package com.adchain.adapters;

import android.text.TextUtils;

import com.adchain.AdChainAdapter;
import com.adchain.AdConfiguration;
import com.adchain.config.RemoteConfigHelper;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.VideoListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

public class StartappAdAdapter extends AdChainAdapter implements VideoListener, AdEventListener {
    private final String appId;
    private StartAppAd startAppAd = null;

    public static StartappAdAdapter checkAndCreate(final String remoteConfigEnableKey, String appId) {
        if (TextUtils.isEmpty(appId))
            return null;
        return new StartappAdAdapter(appId, new AdConfiguration() {
            @Override
            public boolean showAd() {
                return RemoteConfigHelper.areAdsEnabled() && RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
            }
        });
    }

    public static StartappAdAdapter create(String appId) {
        if (TextUtils.isEmpty(appId))
            return null;
        return new StartappAdAdapter(appId, null);
    }

    private StartappAdAdapter(String appId, AdConfiguration adConfiguration) {
        super(adConfiguration);
        this.appId = appId;
    }

    @Override
    public void init() {
        if (startAppAd == null) {
            StartAppSDK.init(getActivity(), this.appId, true);
        }

        startAppAd = new StartAppAd(getActivity());
        startAppAd.setVideoListener(this);
        startAppAd.loadAd(this);
    }

    @Override
    public boolean isAdLoaded() {
        return startAppAd.isReady();
    }

    @Override
    public void showAd() {
        startAppAd.showAd();
    }


    @Override
    public void destroy() {
        startAppAd = null;
    }


    @Override
    public void onVideoCompleted() {
        logv("onVideoCompleted");
        closed();

    }

    @Override
    public void onReceiveAd(Ad ad) {
        logv("onReceiveAd");
        loaded();
    }

    @Override
    public void onFailedToReceiveAd(Ad ad) {
        logv("onFailedToReceiveAd");
        error(ad.getErrorMessage());

    }
}
