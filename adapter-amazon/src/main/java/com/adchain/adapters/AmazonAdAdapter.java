package com.adchain.adapters;


import android.text.TextUtils;

import com.adchain.AdChainAdapter;
import com.adchain.AdConfiguration;
import com.adchain.config.RemoteConfigHelper;
import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.InterstitialAd;

/**
 * Created by Gust on 19.12.2017.
 */
public class AmazonAdAdapter extends AdChainAdapter implements AdListener {
    private final String amazonAppKey;
    private InterstitialAd ad;

    public static AmazonAdAdapter configureAndCreate(final String remoteConfigEnableKey, String remoteConfigAmazonAppKeyKey) {
        String amazonAppKey = RemoteConfigHelper.getConfigs().getString(remoteConfigAmazonAppKeyKey);
        return checkAndCreate(remoteConfigEnableKey, amazonAppKey);
    }

    public static AmazonAdAdapter checkAndCreate(final String remoteConfigEnableKey, String amazonAppKey) {
        if (TextUtils.isEmpty(amazonAppKey)) {
            return null;
        }
        return new AmazonAdAdapter(amazonAppKey, new AdConfiguration() {
            @Override
            public boolean showAd() {
                return RemoteConfigHelper.areAdsEnabled() && RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
            }
        });
    }

    public static AmazonAdAdapter create(String amazonAppKey) {
        if (TextUtils.isEmpty(amazonAppKey))
            return null;
        return new AmazonAdAdapter(amazonAppKey, null);
    }

    private AmazonAdAdapter(String amazonAppKey, AdConfiguration adConfiguration) {
        super(adConfiguration);
        this.amazonAppKey = amazonAppKey;
    }

    @Override
    public void init() {
        AdRegistration.registerApp(getActivity());
        AdRegistration.enableLogging(isLoggingEnabled());
        AdRegistration.enableTesting(isShowTestAds());
        AdRegistration.setAppKey(amazonAppKey);
        ad = new InterstitialAd(getActivity());
        if (rootChain.getTimeout() > 0)
            ad.setTimeout((int) rootChain.getTimeout());
        ad.setListener(this);
        ad.loadAd();
    }

    @Override
    public boolean isAdLoaded() {
        return ad.isReady();
    }

    @Override
    public void showAd() {
        ad.showAd();
    }

    @Override
    public void destroy() {
        ad = null;
    }

    @Override
    public void onAdLoaded(final Ad ad, final AdProperties adProperties) {
        logv("onAdLoaded");
        loaded();
    }

    @Override
    public void onAdFailedToLoad(final Ad view, final AdError error) {
        logv("onAdFailedToLoad");
        error(error.getMessage());
    }

    @Override
    public void onAdDismissed(final Ad ad) {
        logv("onAdDismissed");
        closed();
    }

    @Override
    public void onAdExpanded(Ad ad) {
        logv("onAdExpanded");
        clicked();
    }

    @Override
    public void onAdCollapsed(Ad ad) {
        logv("onAdCollapsed");
    }

    // todo click event not exist ??
}
