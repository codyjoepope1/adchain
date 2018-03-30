package com.adchain.adapters;


import android.app.Activity;

import com.unity3d.ads.UnityAds;
import com.unity3d.ads.mediation.IUnityAdsExtendedListener;

import java.util.HashMap;
import java.util.Map;

public class UnityRouter {
    private static String sCurrentPlacementId;

    private static final UnityAdsListener sUnityAdsListener = new UnityAdsListener();
    private static Map<String, IUnityAdsExtendedListener> mUnityAdsListeners = new HashMap<>();

    static boolean initUnityAds(String gameId, String placementId, Activity launcherActivity) {
        if (gameId == null || gameId.isEmpty()) {
            return false;
        }

        UnityAds.initialize(launcherActivity, gameId, sUnityAdsListener);
        return true;
    }


    static void showAd(Activity activity, String placementId) {
        sCurrentPlacementId = placementId;
        UnityAds.show(activity, placementId);
    }

    static void addListener(String placementId, IUnityAdsExtendedListener unityListener) {
        mUnityAdsListeners.put(placementId, unityListener);
    }

    static void removeListener(String placementId) {
        mUnityAdsListeners.remove(placementId);
    }

    private static class UnityAdsListener implements IUnityAdsExtendedListener {
        @Override
        public void onUnityAdsReady(String placementId) {
            IUnityAdsExtendedListener listener = mUnityAdsListeners.get(placementId);
            if (listener != null) {
                listener.onUnityAdsReady(placementId);
            }
        }

        @Override
        public void onUnityAdsStart(String placementId) {
            IUnityAdsExtendedListener listener = mUnityAdsListeners.get(placementId);
            if (listener != null) {
                listener.onUnityAdsStart(placementId);
            }
        }

        @Override
        public void onUnityAdsFinish(String placementId, UnityAds.FinishState finishState) {
            IUnityAdsExtendedListener listener = mUnityAdsListeners.get(placementId);
            if (listener != null) {
                listener.onUnityAdsFinish(placementId, finishState);
            }
        }

        @Override
        public void onUnityAdsClick(String placementId) {
            IUnityAdsExtendedListener listener = mUnityAdsListeners.get(placementId);
            if (listener != null) {
                listener.onUnityAdsClick(placementId);
            }
        }

        // @Override
        public void onUnityAdsPlacementStateChanged(String placementId, UnityAds.PlacementState oldState, UnityAds.PlacementState newState) {
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String message) {
            IUnityAdsExtendedListener listener = mUnityAdsListeners.get(sCurrentPlacementId);
            if (listener != null) {
                listener.onUnityAdsError(unityAdsError, message);
            }
        }
    }
}