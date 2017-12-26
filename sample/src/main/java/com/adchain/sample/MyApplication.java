package com.adchain.sample;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.adchain.config.RemoteConfigHelper;
import com.adchain.sample.ads.RCUtils;
import com.flurry.android.FlurryAgent;
import com.google.firebase.FirebaseApp;

/**
 * Created by gust 05/07/16.
 */
public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());

         new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, getString(R.string.flurry_api_key));

        RemoteConfigHelper.init(RCUtils.getDefaults(), BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
