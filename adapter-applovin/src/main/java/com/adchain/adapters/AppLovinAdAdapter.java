package com.adchain.adapters;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.adchain.AdChainAdapter;
import com.adchain.AdConfiguration;
import com.adchain.config.RemoteConfigHelper;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkSettings;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.MediationInterstitialAdapter;
import com.google.android.gms.ads.mediation.MediationInterstitialListener;

import java.util.LinkedList;
import java.util.Queue;


public class AppLovinAdAdapter extends AdChainAdapter implements MediationInterstitialAdapter, AppLovinAdLoadListener, AppLovinAdDisplayListener, AppLovinAdClickListener, AppLovinAdVideoPlaybackListener {
    private static Queue<AppLovinAd> GLOBAL_INTERSTITIAL_AD;
    private static final Object GLOBAL_INTERSTITIAL_ADS_LOCK = new Object();
    private final String sdkKey;
    private AppLovinSdk sdk;

    public static AppLovinAdAdapter configureAndCreate(final String remoteConfigEnableKey, String remoteConfigSdkKeyKey) {
        String sdkKey = RemoteConfigHelper.getConfigs().getString(remoteConfigSdkKeyKey);
        return checkAndCreate(remoteConfigEnableKey, sdkKey);
    }

    public static AppLovinAdAdapter checkAndCreate(final String remoteConfigEnableKey, String sdkKey) {
        if (TextUtils.isEmpty(sdkKey)) {
            return null;
        }
        return new AppLovinAdAdapter(sdkKey, new AdConfiguration() {
            @Override
            public boolean showAd() {
                return RemoteConfigHelper.areAdsEnabled() && RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
            }
        });
    }

    public static AppLovinAdAdapter create(String sdkKey) {
        if (TextUtils.isEmpty(sdkKey))
            return null;
        return new AppLovinAdAdapter(sdkKey, null);
    }

    private AppLovinAdAdapter(String sdkKey, AdConfiguration adConfiguration) {
        super(adConfiguration);
        this.sdkKey = sdkKey;
    }


    @Override
    public void init() {
        if (sdk == null) {
            AppLovinSdkSettings appLovinSdkSettings = new AppLovinSdkSettings();
            sdk = AppLovinSdk.getInstance(sdkKey, appLovinSdkSettings, getActivity());
        }
        sdk.getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, this);
    }


    @Override
    public boolean isAdLoaded() {
        synchronized (GLOBAL_INTERSTITIAL_ADS_LOCK) {
            if (GLOBAL_INTERSTITIAL_AD != null && !GLOBAL_INTERSTITIAL_AD.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void showAd() {
        final AppLovinAd preloadedAd = dequeueAd();
        if (preloadedAd != null) {
            final AppLovinInterstitialAdDialog interstitialAd = AppLovinInterstitialAd.create(sdk, getActivity());
            interstitialAd.setAdLoadListener(this);
            interstitialAd.setAdDisplayListener(this);
            interstitialAd.setAdClickListener(this);
            interstitialAd.setAdVideoPlaybackListener(this);
            interstitialAd.showAndRender(preloadedAd);
        } else {
            super.closed();
        }
    }


    @Override
    public void destroy() {
        sdk = null;
    }

    @Override
    public void adReceived(AppLovinAd appLovinAd) {
        logv("adReceived ");
        enqueueAd(appLovinAd);
        super.loaded();
    }

    @Override
    public void failedToReceiveAd(int i) {
        logv("failedToReceiveAd");
        super.error(":" + i);
    }

    @Override
    public void requestInterstitialAd(Context context, MediationInterstitialListener mediationInterstitialListener, Bundle bundle, MediationAdRequest mediationAdRequest, Bundle bundle1) {
        logv("requestInterstitialAd");
    }

    @Override
    public void showInterstitial() {
        logv("showInterstitial");

    }

    @Override
    public void onDestroy() {
        logv("onDestroy");
    }

    @Override
    public void onPause() {
        logv("onPause");

    }

    @Override
    public void onResume() {
        logv("onResume");

    }

    @Override
    public void adDisplayed(AppLovinAd appLovinAd) {
        logv("adDisplayed");
//        super.closed();

    }

    @Override
    public void adHidden(AppLovinAd appLovinAd) {
        logv("adHidden");
        super.closed();
    }

    @Override
    public void adClicked(AppLovinAd appLovinAd) {
        logv("adClicked");
        clicked();
    }

    @Override
    public void videoPlaybackBegan(AppLovinAd appLovinAd) {
        logv("videoPlaybackBegan");

    }

    @Override
    public void videoPlaybackEnded(AppLovinAd appLovinAd, double v, boolean b) {
        logv("videoPlaybackEnded");
    }


    private static AppLovinAd dequeueAd() {
        synchronized (GLOBAL_INTERSTITIAL_ADS_LOCK) {
            AppLovinAd preloadedAd = null;

            if (GLOBAL_INTERSTITIAL_AD != null && !GLOBAL_INTERSTITIAL_AD.isEmpty()) {
                preloadedAd = GLOBAL_INTERSTITIAL_AD.poll();
            }

            return preloadedAd;
        }
    }

    private static void enqueueAd(final AppLovinAd ad) {
        synchronized (GLOBAL_INTERSTITIAL_ADS_LOCK) {
            if (GLOBAL_INTERSTITIAL_AD == null) {
                GLOBAL_INTERSTITIAL_AD = new LinkedList<>();
            }

            GLOBAL_INTERSTITIAL_AD.offer(ad);
        }
    }
}
