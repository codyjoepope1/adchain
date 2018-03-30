package com.adchain.adapters;


import android.text.TextUtils;

import com.adchain.AdChainAdapter;
import com.adchain.AdConfiguration;
import com.adchain.config.RemoteConfigHelper;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.mediation.IUnityAdsExtendedListener;

/**
 * Created by Gust on 19.12.2017.
 */
public class UnityAdAdapter extends AdChainAdapter implements IUnityAdsListener, IUnityAdsExtendedListener {
    private final String gameId;
    private final String placementId;

    public static UnityAdAdapter configureAndCreate(final String remoteConfigEnableKey, String remoteConfigGameIdKey, String remoteConfigPlacementIdKey) {
        String gameId = RemoteConfigHelper.getConfigs().getString(remoteConfigGameIdKey);
        String placementId = RemoteConfigHelper.getConfigs().getString(remoteConfigPlacementIdKey);
        return checkAndCreate(remoteConfigEnableKey, gameId, placementId);
    }

    public static UnityAdAdapter checkAndCreate(final String remoteConfigEnableKey, String gameId, String placementId) {
        if (TextUtils.isEmpty(gameId) || TextUtils.isEmpty(placementId)) {
            return null;
        }
        return new UnityAdAdapter(gameId, placementId, new AdConfiguration() {
            @Override
            public boolean showAd() {
                return RemoteConfigHelper.areAdsEnabled() && RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
            }
        });
    }

    public static UnityAdAdapter create(String gameId, String placementId) {
        if (TextUtils.isEmpty(gameId) || TextUtils.isEmpty(placementId))
            return null;
        return new UnityAdAdapter(gameId, placementId, null);
    }

    private UnityAdAdapter(String gameId, String placementId, AdConfiguration adConfiguration) {
        super(adConfiguration);
        this.gameId = gameId;
        this.placementId = placementId;
    }

    @Override
    public void init() {
        UnityRouter.addListener(placementId, this);

        if (!UnityAds.isInitialized()) {
            UnityRouter.initUnityAds(gameId, placementId, getActivity());
            UnityAds.setDebugMode(isShowTestAds());
        }
    }

    @Override
    public boolean isAdLoaded() {
        return UnityAds.isReady(placementId);
    }

    @Override
    public void showAd() {
        UnityRouter.showAd(getActivity(), placementId);
    }

    @Override
    public void destroy() {
        UnityRouter.removeListener(placementId);
    }

    @Override
    public void onUnityAdsReady(String placementId) {
        logv("onUnityAdsReady: " + placementId);
        loaded();
    }

    @Override
    public void onUnityAdsStart(String placementId) {

    }

    @Override
    public void onUnityAdsFinish(String placementId, UnityAds.FinishState finishState) {
        if (finishState == UnityAds.FinishState.ERROR) {
            logv("playback error: " + placementId);
            error("playback: " + placementId);
        } else {
            logv("onUnityAdsFinish: " + placementId);
            closed();
        }
        UnityRouter.removeListener(placementId);
    }

    @Override
    public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String message) {
        logv("onUnityAdsError: " + placementId);
        error("code:" + unityAdsError + " / " + message);
        UnityRouter.removeListener(placementId);
    }


    @Override
    public void onUnityAdsClick(String placementId) {

    }

    @Override
    public void onUnityAdsPlacementStateChanged(String placementId, UnityAds.PlacementState placementState, UnityAds.PlacementState placementState1) {

    }
}
