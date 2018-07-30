package com.adchain.adapters;


import android.text.TextUtils;

import com.adchain.AdChainAdapter;
import com.adchain.AdConfiguration;
import com.adchain.config.RemoteConfigHelper;

import io.presage.Presage;
import io.presage.common.AdConfig;
import io.presage.interstitial.PresageInterstitial;
import io.presage.interstitial.PresageInterstitialCallback;

/**
 * Created by Gust on 19.12.2017.
 */
public class OguryAdAdapter extends AdChainAdapter {
    private final String apiKey;
    private final String adUnitId;
    private PresageInterstitial presageInterstitial;

    public static OguryAdAdapter configureAndCreate(final String remoteConfigEnableKey, String remoteConfigApiKeyKey, String remoteConfigAdUnitIdKey) {
        String apiKey = RemoteConfigHelper.getConfigs().getString(remoteConfigApiKeyKey);
        String adUnitId = RemoteConfigHelper.getConfigs().getString(remoteConfigAdUnitIdKey);
        return checkAndCreate(remoteConfigEnableKey, adUnitId, apiKey);
    }

    public static OguryAdAdapter checkAndCreate(final String remoteConfigEnableKey, final String adUnitId, String apiKey) {
        if (TextUtils.isEmpty(apiKey)) {
            return null;
        }
        return new OguryAdAdapter(apiKey, adUnitId, new AdConfiguration() {
            @Override
            public boolean showAd() {
                return RemoteConfigHelper.areAdsEnabled() && RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
            }
        });
    }

    public static OguryAdAdapter create(String apiKey, String adUnitId) {
        if (TextUtils.isEmpty(apiKey))
            return null;
        return new OguryAdAdapter(apiKey, adUnitId, null);
    }

    private OguryAdAdapter(String apiKey, String adUnitId, AdConfiguration adConfiguration) {
        super(adConfiguration);
        this.apiKey = apiKey;
        this.adUnitId = adUnitId;
    }

    @Override
    public void init() {
        Presage.getInstance().start(this.apiKey, getActivity());
        AdConfig adConfig = new AdConfig(this.adUnitId);
        presageInterstitial = new PresageInterstitial(getActivity(), adConfig);
        presageInterstitial.setInterstitialCallback(new Handler());
        presageInterstitial.load();
    }

    @Override
    public boolean isAdLoaded() {
        return presageInterstitial.isLoaded();
    }

    @Override
    public void showAd() {
        presageInterstitial.show();
    }


    @Override
    public void destroy() {
        presageInterstitial = null;
    }


    private class Handler implements PresageInterstitialCallback {
        @Override
        public void onAdAvailable() {
            logv("onAdAvailable");
        }

        @Override
        public void onAdNotAvailable() {
            logv("onAdNotAvailable");
            error("onAdNotAvailable");
        }

        @Override
        public void onAdLoaded() {
            logv("onAdLoaded");
            loaded();
        }

        @Override
        public void onAdNotLoaded() {
            logv("onAdNotLoaded");
            error("onAdNotLoaded");
        }

        @Override
        public void onAdClosed() {
            logv("onAdClosed");
            closed();
        }

        @Override
        public void onAdError(int code) {
            logv("onAdError");
            error("code:" + code);
        }

        @Override
        public void onAdDisplayed() {
            logv("onAdDisplayed");
        }
    }
}
