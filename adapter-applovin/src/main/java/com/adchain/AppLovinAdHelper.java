package com.adchain;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adchain.config.RemoteConfigHelper;
import com.applovin.adview.AppLovinAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;


/**
 * Created by Gust on 25.12.2017.
 * <p>
 * AppLovinSdk.getInstance( getApplicationContext() )
 * .getSettings()
 * .setTestAdsEnabled( true );
 */
public class AppLovinAdHelper {

    public static final String TAG = "AppLovinAdHelper";

    public static AppLovinAdView checkAndLoadBanner(Context context, LinearLayout adContainer, String remoteConfigEnableKey) {
        boolean enable = RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
        return loadBanner(context, adContainer, enable);
    }

    public static AppLovinAdView loadBanner(Context context, final LinearLayout bannerContainer, boolean enabled) {
        if (!enabled) {
            bannerContainer.setVisibility(View.GONE);
            return null;
        }
        bannerContainer.setVisibility(View.VISIBLE);

        AppLovinSdk.initializeSdk(context.getApplicationContext());

        final AppLovinAdView adView = new AppLovinAdView(AppLovinAdSize.BANNER, context);
        adView.setAdLoadListener(new AppLovinAdLoadListener() {
            @Override
            public void adReceived(AppLovinAd appLovinAd) {
            }

            @Override
            public void failedToReceiveAd(int i) {
                Log.e(TAG, "failedToReceiveAd" + i);
                bannerContainer.setVisibility(View.GONE);
            }
        });
        adView.loadNextAd();

        bannerContainer.addView(adView, new android.widget.FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        // Load an ad!
        adView.loadNextAd();
        return adView;
    }


}
