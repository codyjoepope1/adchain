package com.adchain;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.adchain.config.RemoteConfigHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by a on 19.12.2017.
 */
public class AdmobAdHelper {
    /*
    * BANNER
    * */
    public static void checkAndLoadBanner(Context context, LinearLayout adContainer, String remoteConfigEnableKey, String adUnitId) {
        boolean enable = RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
        loadBanner(context, adContainer, adUnitId, enable);
    }

    public static void configureAndLoadBanner(Context context, LinearLayout adContainer, String remoteConfigEnableKey, String remoteConfigAdUnitIdKey) {
        String adUnitId = RemoteConfigHelper.getConfigs().getString(remoteConfigAdUnitIdKey);
        checkAndLoadBanner(context, adContainer, remoteConfigEnableKey, adUnitId);
    }

    public static void loadBanner(Context context, LinearLayout adContainer, String adUnitId) {
        loadBanner(context, adContainer, adUnitId, true);
    }

    private static void loadBanner(Context context, final LinearLayout container, String adUnitId, boolean enabled) {
        if (!enabled) {
            container.setVisibility(View.GONE);
            return;
        }
        container.setVisibility(View.VISIBLE);

        AdView mAdView = new AdView(context);
        mAdView.setAdUnitId(adUnitId);
        mAdView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
        MobileAds.initialize(context, mAdView.getAdUnitId());

        AdRequest.Builder builder = new AdRequest.Builder();
        AdRequest adRequest = builder.build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                container.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
        mAdView.loadAd(adRequest);
        container.addView(mAdView);
    }

    /*
    * NATIVE
    * */
    public static View loadNative(Context context, String adUnitId) {
        NativeExpressAdView nativeAd = new NativeExpressAdView(context);
        nativeAd.setAdSize(AdSize.LARGE_BANNER);
        nativeAd.setAdUnitId(adUnitId);

        MobileAds.initialize(context, nativeAd.getAdUnitId());

        AdRequest.Builder builder = new AdRequest.Builder();
        AdRequest adRequest = builder.build();

        nativeAd.loadAd(adRequest);
        return nativeAd;
    }
}
