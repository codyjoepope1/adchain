package com.adchain.adapters;

import android.text.TextUtils;

import com.adchain.AdChainAdapter;
import com.adchain.AdConfiguration;
import com.adchain.config.RemoteConfigHelper;
import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdInterstitial;
import com.flurry.android.ads.FlurryAdInterstitialListener;
import com.flurry.android.ads.FlurryAdTargeting;

/**
 * Created by Gust on 19.12.2017.
 * don't forget to initialize FlurryAgent in Application
 */
public class FlurryAdAdapter extends AdChainAdapter implements FlurryAdInterstitialListener {
    private final String mAdSpaceName;
    private FlurryAdInterstitial mFlurryAdInterstitial = null;

    public static FlurryAdAdapter checkAndCreate(final String remoteConfigEnableKey, String mAdSpaceName) {
        if (TextUtils.isEmpty(mAdSpaceName))
            return null;
        return new FlurryAdAdapter(mAdSpaceName, new AdConfiguration() {
            @Override
            public boolean showAd() {
                return RemoteConfigHelper.areAdsEnabled() && RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
            }
        });
    }

    public static FlurryAdAdapter create(String mAdSpaceName) {
        if (TextUtils.isEmpty(mAdSpaceName))
            return null;
        return new FlurryAdAdapter(mAdSpaceName, null);
    }

    private FlurryAdAdapter(String mAdSpaceName, AdConfiguration adConfiguration) {
        super(adConfiguration);
        this.mAdSpaceName = mAdSpaceName;
    }

    @Override
    public void init() {
        mFlurryAdInterstitial = new FlurryAdInterstitial(getActivity(), mAdSpaceName);
        mFlurryAdInterstitial.setListener(this);

        FlurryAdTargeting adTargeting = new FlurryAdTargeting();
        adTargeting.setEnableTestAds(isShowTestAds());
        mFlurryAdInterstitial.setTargeting(adTargeting);
        mFlurryAdInterstitial.fetchAd();
    }

    @Override
    public boolean isAdLoaded() {
        return mFlurryAdInterstitial.isReady();
    }

    @Override
    public void showAd() {
        mFlurryAdInterstitial.displayAd();
    }


    @Override
    public void destroy() {
        if (mFlurryAdInterstitial != null) {
            mFlurryAdInterstitial.destroy();
            mFlurryAdInterstitial = null;
        }
    }


    @Override
    public void onFetched(FlurryAdInterstitial adInterstitial) {
        logv("onFetched");
        loaded();
    }

    @Override
    public void onClose(FlurryAdInterstitial flurryAdInterstitial) {
        logv("onClose");
        closed();
    }

    @Override
    public void onError(FlurryAdInterstitial flurryAdInterstitial, FlurryAdErrorType flurryAdErrorType, int i) {
        logv("onError");
        error(flurryAdErrorType.name());
    }

    @Override
    public void onAppExit(FlurryAdInterstitial flurryAdInterstitial) {
        logv("onAppExit");
    }

    @Override
    public void onClicked(FlurryAdInterstitial flurryAdInterstitial) {
        logv("onClicked");
        clicked();
    }

    @Override
    public void onVideoCompleted(FlurryAdInterstitial flurryAdInterstitial) {
        logv("onVideoCompleted");
        closed();
    }

    @Override
    public void onRendered(FlurryAdInterstitial flurryAdInterstitial) {
        logv("onRendered");
    }

    @Override
    public void onDisplay(FlurryAdInterstitial flurryAdInterstitial) {
        logv("onDisplay");
    }

}
