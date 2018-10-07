package com.adchain.adapters;


import android.text.TextUtils;

import com.adchain.AdChainAdapter;
import com.adchain.AdConfiguration;
import com.adchain.config.RemoteConfigHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Gust on 19.12.2017.
 */
public class AdmobAdAdapter extends AdChainAdapter {
    private final String adunitId;
    private InterstitialAd ad;

    public static AdmobAdAdapter configureAndCreate(final String remoteConfigEnableKey, String remoteConfigAdunitIdKey) {
        String admobId = RemoteConfigHelper.getConfigs().getString(remoteConfigAdunitIdKey);
        return checkAndCreate(remoteConfigEnableKey, admobId);
    }

    public static AdmobAdAdapter checkAndCreate(final String remoteConfigEnableKey, String adunitId) {
        if (TextUtils.isEmpty(adunitId)) {
            return null;
        }
        return new AdmobAdAdapter(adunitId, new AdConfiguration() {
            @Override
            public boolean showAd() {
                return RemoteConfigHelper.areAdsEnabled() && RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
            }
        });
    }

    public static AdmobAdAdapter create(String admobId) {
        if (TextUtils.isEmpty(admobId))
            return null;
        return new AdmobAdAdapter(admobId, null);
    }

    private AdmobAdAdapter(String admobId, AdConfiguration adConfiguration) {
        super(adConfiguration);
        this.adunitId = admobId;
    }

    @Override
    public void init() {
        ad = new InterstitialAd(getActivity());
        ad.setAdUnitId(adunitId);

        AdRequest.Builder builder = new AdRequest.Builder();
        AdRequest adRequest = builder.build();
        ad.loadAd(adRequest);
        ad.setAdListener(new CustomListener());
    }

    @Override
    public boolean isAdLoaded() {
        return ad.isLoaded();
    }

    @Override
    public void showAd() {
        ad.show();
    }

    @Override
    public void destroy() {
        ad = null;
    }

    private class CustomListener extends AdListener {
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
        public void onAdFailedToLoad(int errorCode) {
            logv("onAdFailedToLoad");
            error("code:" + errorCode);
        }

        @Override
        public void onAdClicked() {
            logv("onAdClicked");
            clicked();
        }
    }

}
