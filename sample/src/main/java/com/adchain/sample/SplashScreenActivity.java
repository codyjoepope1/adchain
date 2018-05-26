package com.adchain.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.adchain.AdChainBuilder;
import com.adchain.TestAdAdapter;
import com.adchain.adapters.AdmobAdAdapter;
import com.adchain.adapters.AmazonAdAdapter;
import com.adchain.adapters.FacebookAdAdapter;
import com.adchain.adapters.FlurryAdAdapter;
import com.adchain.config.RemoteConfigHelper;
import com.adchain.sample.ads.RCUtils;


public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreenActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.splash_screen);

        RemoteConfigHelper.init(RCUtils.getDefaults(), BuildConfig.DEBUG);

        final boolean adsEnabled = true; // if user purchased some ads remove package, set false.
        RemoteConfigHelper.setAdsEnabled(adsEnabled);

        new AdChainBuilder(this)
                .showAuto()
                .add(AdmobAdAdapter.configureAndCreate(RCUtils.SHOW_ADMOB_1, RCUtils.ID_ADMOB_1))
                .add(FlurryAdAdapter.checkAndCreate(RCUtils.SHOW_FLURRY_1, getString(R.string.flurry_1)))
                .add(AmazonAdAdapter.checkAndCreate(RCUtils.SHOW_AMAZON_1, getString(R.string.amazon_1)))
                .add(FacebookAdAdapter.create("YOUR_PLACEMENT_ID"))
//                .add(TestAdAdapter.create(" AD1 ", 100))
//                .add(TestAdAdapter.create(" AD2 ", 7000))
//                .add(TestAdAdapter.error(" AD3(E) ", 2000))
                .startActivityAfterAdsFinished(SplashScreenActivityTerm.class)
                .build();

    }


}
