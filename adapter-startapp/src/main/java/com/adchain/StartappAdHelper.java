package com.adchain;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.adchain.config.RemoteConfigHelper;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;

public class StartappAdHelper {

    public static final String TAG = "StartappAdHelper";

    public static Banner checkAndLoadBanner(Activity activity, LinearLayout mainLayout, String remoteConfigEnableKey) {
        boolean enable = RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
        return loadBanner(activity, mainLayout, enable);
    }

    public static Banner loadBanner(Activity activity, final LinearLayout mainLayout, boolean enabled) {
        if (!enabled) {
            mainLayout.setVisibility(View.GONE);
            return null;
        }
        mainLayout.setVisibility(View.GONE);
        Banner startAppBanner = new Banner(activity);
        startAppBanner.setBannerListener(new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                mainLayout.setVisibility(View.VISIBLE);
                Log.v(TAG, "onReceiveAd");
            }

            @Override
            public void onFailedToReceiveAd(View view) {
                Log.v(TAG, "onFailedToReceiveAd");
                mainLayout.setVisibility(View.GONE);
            }

            @Override
            public void onClick(View view) {
                Log.v(TAG, "onClick");
            }
        });
        startAppBanner.showBanner();

        RelativeLayout.LayoutParams bannerParameters =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
        bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
// Add to main Layout
        mainLayout.addView(startAppBanner, bannerParameters);

        return startAppBanner;
    }

}
