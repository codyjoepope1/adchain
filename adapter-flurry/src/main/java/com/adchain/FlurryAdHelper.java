package com.adchain;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.adchain.config.RemoteConfigHelper;
import com.flurry.android.ads.FlurryAdBanner;
import com.flurry.android.ads.FlurryAdBannerListener;
import com.flurry.android.ads.FlurryAdErrorType;

/**
 * Created by Gust on 19.12.2017.
 */
public class FlurryAdHelper {

    public static final String TAG = "FlurryAdHelper";

    public static FlurryAdBanner checkAndLoadBanner(Context context, LinearLayout mBanner, String remoteConfigEnableKey, String mAdSpaceName) {
        boolean enable = RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
        return loadBanner(context, mAdSpaceName, mBanner, enable);
    }

    public static FlurryAdBanner configureAndLoadBanner(Context context, LinearLayout adContainer, String remoteConfigEnableKey, String remoteConfigmAdSpaceNameKey) {
        String appKey = RemoteConfigHelper.getConfigs().getString(remoteConfigmAdSpaceNameKey);
        return checkAndLoadBanner(context, adContainer, remoteConfigEnableKey, appKey);
    }

    public static FlurryAdBanner loadBanner(Context context, String mAdSpaceName, LinearLayout mBanner) {
        return loadBanner(context, mAdSpaceName, mBanner, true);
    }

    private static FlurryAdBanner loadBanner(Context context, String mAdSpaceName, final LinearLayout mBanner, boolean enabled) {
        if (!enabled) {
            mBanner.setVisibility(View.GONE);
            return null;
        }
        mBanner.setVisibility(View.VISIBLE);

        FlurryAdBanner mFlurryAdBanner = new FlurryAdBanner(context, mBanner, mAdSpaceName);

        mFlurryAdBanner.fetchAndDisplayAd();
        mFlurryAdBanner.setListener(new FlurryAdBannerListener() {
            @Override
            public void onFetched(FlurryAdBanner flurryAdBanner) {

            }

            @Override
            public void onRendered(FlurryAdBanner flurryAdBanner) {

            }

            @Override
            public void onShowFullscreen(FlurryAdBanner flurryAdBanner) {

            }

            @Override
            public void onCloseFullscreen(FlurryAdBanner flurryAdBanner) {

            }

            @Override
            public void onAppExit(FlurryAdBanner flurryAdBanner) {

            }

            @Override
            public void onClicked(FlurryAdBanner flurryAdBanner) {

            }

            @Override
            public void onVideoCompleted(FlurryAdBanner flurryAdBanner) {

            }

            @Override
            public void onError(FlurryAdBanner flurryAdBanner, FlurryAdErrorType flurryAdErrorType, int i) {
                mBanner.setVisibility(View.GONE);
                Log.e(TAG, "onError: " + i + ":" + flurryAdErrorType.name());

            }
        });
        return mFlurryAdBanner;
    }
}
