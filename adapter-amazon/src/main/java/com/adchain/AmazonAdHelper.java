package com.adchain;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.adchain.config.RemoteConfigHelper;
import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;

/**
 * Created by Gust on 19.12.2017.
 */
public class AmazonAdHelper {

    public static final String TAG = "AmazonAdHelper";

    public static AdLayout checkAndLoadBanner(Context context, LinearLayout adContainer, String remoteConfigEnableKey, String appKey) {
        boolean enable = RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
        return loadBanner(context, adContainer, enable, appKey);
    }

    public static AdLayout configureAndLoadBanner(Context context, LinearLayout adContainer, String remoteConfigEnableKey, String remoteConfigAppKeyKey) {
        String appKey = RemoteConfigHelper.getConfigs().getString(remoteConfigAppKeyKey);
        return checkAndLoadBanner(context, adContainer, remoteConfigEnableKey, appKey);
    }

    public static AdLayout loadBanner(Context context, final LinearLayout adContainer, boolean enabled, String appKey) {
        if (!enabled) {
            adContainer.setVisibility(View.GONE);
            return null;
        }
        adContainer.setVisibility(View.VISIBLE);

        AdRegistration.setAppKey(appKey);

        final AdLayout adView = new AdLayout(context);
        adView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        adView.enableAutoShow();
        adView.setListener(new AdListener() {
            @Override
            public void onAdLoaded(Ad ad, AdProperties adProperties) {

            }

            @Override
            public void onAdFailedToLoad(Ad ad, AdError adError) {
                Log.e(TAG, "onAdFailedToLoad: " + adError.getCode().name() + "."+ adError.getMessage());
                adContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAdExpanded(Ad ad) {

            }

            @Override
            public void onAdCollapsed(Ad ad) {

            }

            @Override
            public void onAdDismissed(Ad ad) {

            }
        });
        adView.loadAd();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        adContainer.addView(adView, lp);
        return adView;
    }
}
