package com.adchain;


import android.content.Context;
import android.util.Log;
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
 * Created by Gust on 19.12.2017.
 */
public class AdmobAdHelper {

    public static final String TAG = "AdmobAdHelper";

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
                Log.e(TAG, "onAdFailedToLoad: " + i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
        mAdView.loadAd(adRequest);
        container.addView(mAdView);
    }
}
