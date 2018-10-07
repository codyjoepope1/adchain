package com.adchain.adapters;


import android.text.TextUtils;

import com.adchain.AdChainAdapter;
import com.adchain.AdConfiguration;
import com.adchain.config.RemoteConfigHelper;

import io.presage.Presage;
import io.presage.ads.PresageInterstitial;

/**
 * Created by Gust on 19.12.2017.
 */
public class OguryAdAdapter extends AdChainAdapter {
    private final String apiKey;
    private PresageInterstitial presageInterstitial;

    public static OguryAdAdapter configureAndCreate(final String remoteConfigEnableKey, String remoteConfigApiKeyKey) {
        String adUnitId = RemoteConfigHelper.getConfigs().getString(remoteConfigApiKeyKey);
        return checkAndCreate(remoteConfigEnableKey, adUnitId);
    }

    public static OguryAdAdapter checkAndCreate(final String remoteConfigEnableKey, String apiKey) {
        if (TextUtils.isEmpty(apiKey)) {
            return null;
        }
        return new OguryAdAdapter(apiKey, new AdConfiguration() {
            @Override
            public boolean showAd() {
                return RemoteConfigHelper.areAdsEnabled() && RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
            }
        });
    }

    public static OguryAdAdapter create(String apiKey) {
        if (TextUtils.isEmpty(apiKey))
            return null;
        return new OguryAdAdapter(apiKey, null);
    }

    private OguryAdAdapter(String apiKey, AdConfiguration adConfiguration) {
        super(adConfiguration);
        this.apiKey = apiKey;
    }

    @Override
    public void init() {
        Presage.getInstance().setContext(getActivity());
        Presage.getInstance().start();
        presageInterstitial = new PresageInterstitial(getActivity(), this.apiKey);
        presageInterstitial.setPresageInterstitialCallback(new Handler());
        presageInterstitial.load();
    }

    @Override
    public boolean isAdLoaded() {
        return presageInterstitial.canShow();
    }

    @Override
    public void showAd() {
        presageInterstitial.show();
    }


    @Override
    public void destroy() {
        if (presageInterstitial != null) {
            presageInterstitial.destroy();
            presageInterstitial = null;
        }
    }


    private class Handler implements PresageInterstitial.PresageInterstitialCallback {
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
            clicked();
        }
    }
}
