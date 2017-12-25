package com.adchain.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.adchain.AdChainAdapter;
import com.adchain.AdConfiguration;
import com.adchain.config.RemoteConfigHelper;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

/**
 * Created by Gust on 19.12.2017.
 * AdSettings.addTestDevice("1c954c8e4afd92dec43247f1d695d4d0");
 */
public class FacebookAdAdapter extends AdChainAdapter implements InterstitialAdListener {
    private final String adAudienceId;
    private InterstitialAd ad;
    private boolean isFaceAppIdSet;

    public static FacebookAdAdapter configureAndCreate(final String remoteConfigEnableKey, String remoteConfigAdAudienceIdKey) {
        String adAudienceId = RemoteConfigHelper.getConfigs().getString(remoteConfigAdAudienceIdKey);
        return checkAndCreate(remoteConfigEnableKey, adAudienceId);
    }

    public static FacebookAdAdapter checkAndCreate(final String remoteConfigEnableKey, String adAudienceId) {
        if (TextUtils.isEmpty(adAudienceId)) {
            return null;
        }
        return new FacebookAdAdapter(adAudienceId, new AdConfiguration() {
            @Override
            public boolean showAd() {
                return RemoteConfigHelper.areAdsEnabled() && RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
            }
        });
    }

    public static FacebookAdAdapter create(String adAudienceId) {
        if (TextUtils.isEmpty(adAudienceId))
            return null;
        return new FacebookAdAdapter(adAudienceId, null);
    }

    private FacebookAdAdapter(String adAudienceId, AdConfiguration adConfiguration) {
        super(adConfiguration);
        this.adAudienceId = adAudienceId;
    }

    private void setFaceAppId(Context c, String appId) {
        isFaceAppIdSet = true;
        try {
            ApplicationInfo info = c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = info.metaData;
            bundle.putString("com.facebook.sdk.ApplicationId", appId);
        } catch (PackageManager.NameNotFoundException e) {
            loge("Facebook ApplicationId setting error" + e.getMessage());
        }
    }

    @Override
    public void init() {
        if (!isFaceAppIdSet && adAudienceId.contains("_"))
            setFaceAppId(getActivity(), adAudienceId.split("_")[0]);
        ad = new com.facebook.ads.InterstitialAd(getActivity(), adAudienceId);
        ad.setAdListener(this);
        ad.loadAd();
    }


    @Override
    public boolean isAdLoaded() {
        return ad.isAdLoaded();
    }

    @Override
    public void showAd() {
        ad.show();
    }


    @Override
    public void destroy() {
        ad.destroy();
        ad = null;
    }

    @Override
    public void onInterstitialDismissed(Ad ad) {
        logv("onInterstitialDismissed");
        super.closed();
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        logv("onError");
        super.error(adError.getErrorCode() + ":" + adError.getErrorMessage());
    }

    @Override
    public void onAdLoaded(Ad ad) {
        logv("onAdLoaded");
        super.loaded();
    }

    @Override
    public void onInterstitialDisplayed(Ad ad) {
        logv("onInterstitialDisplayed");
    }

    @Override
    public void onAdClicked(Ad ad) {
        logv("onAdClicked");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        logv("onLoggingImpression");
    }
}
