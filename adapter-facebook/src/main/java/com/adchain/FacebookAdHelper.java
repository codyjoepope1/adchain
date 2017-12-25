package com.adchain;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.adchain.config.RemoteConfigHelper;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

/**
 * Created by Gust on 19.12.2017.
 */
public class FacebookAdHelper {
    /*
    * BANNER AD LOADER
    * */

    public static com.facebook.ads.AdView checkAndLoadBanner(Context context, LinearLayout adContainer, String remoteConfigEnableKey, String facebokBannerId) {
        boolean enable = RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
        return loadBanner(context, adContainer, enable, facebokBannerId);
    }

    public static com.facebook.ads.AdView configureAndLoadBanner(Context context, LinearLayout adContainer, String remoteConfigEnableKey, String remoteConfigBannerIdKey) {
        String facebokBannerId = RemoteConfigHelper.getConfigs().getString(remoteConfigBannerIdKey);
        return checkAndLoadBanner(context, adContainer, remoteConfigEnableKey, facebokBannerId);
    }

    public static com.facebook.ads.AdView loadBanner(Context context, LinearLayout adContainer, String id) {
        return loadBanner(context, adContainer, true, id);
    }

    private static com.facebook.ads.AdView loadBanner(Context context, final LinearLayout adContainer, boolean enable, String id) {
        if (!enable) {
            adContainer.setVisibility(View.GONE);
            return null;
        }
        adContainer.setVisibility(View.VISIBLE);
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, id, AdSize.BANNER_HEIGHT_50);
        adContainer.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                adContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        adView.loadAd();
        return adView;
    }


    /*
    * NATIVE AD LOADER
    * */

    /**
     * Has no remote config enable feature
     */
    public static NativeAdsManager configureAndLoadNative(Context context, String remoteConfigNativeAdIdKey, final Callback loaded) {
        String nativeAdId = RemoteConfigHelper.getConfigs().getString(remoteConfigNativeAdIdKey);
        return loadNative(context, nativeAdId, loaded);
    }

    public static NativeAdsManager loadNative(Context context, String nativeAdId, final Callback callback) {
        final NativeAdsManager manager = new NativeAdsManager(context, nativeAdId, 5);
        manager.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {
                callback.onAdLoaded();
            }

            @Override
            public void onAdError(AdError adError) {

            }
        });
        manager.loadAds(NativeAd.MediaCacheFlag.ALL);
        return manager;
    }

    public interface Callback {
        public void onAdLoaded();
    }
}
