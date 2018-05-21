package com.adchain.sample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.adchain.AdChain;
import com.adchain.AdChainBuilder;
import com.adchain.AdmobAdHelper;
import com.adchain.AmazonAdHelper;
import com.adchain.DummyAdAdapter;
import com.adchain.adapters.AdmobAdAdapter;
import com.adchain.adapters.FlurryAdAdapter;
import com.adchain.config.RemoteConfigHelper;
import com.adchain.sample.ads.RCUtils;
import com.adchain.toneshub.ToneshubHelper;
import com.adchain.view.CustomBanner;
import com.amazon.device.ads.AdLayout;

import java.util.Random;

import io.presage.Presage;
import io.presage.ads.PresageInterstitial;

public class MainActivity extends Activity {

    private AdChain ad;
    private CustomBanner customBanner;
    private AdLayout amazonBanner;


    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_connect_main);

        Presage.getInstance().setContext(this.getBaseContext());
        Presage.getInstance().start();


        ad = new AdChainBuilder(this)
                .withStepByStep(true) // shows 1 ads per calling 'showAds()'
                .add(AdmobAdAdapter.configureAndCreate(RCUtils.SHOW_MAIN, RCUtils.ID_ADMOB_3))
                .add(DummyAdAdapter.create())
                .add(FlurryAdAdapter.checkAndCreate(RCUtils.SHOW_MAIN, getString(R.string.flurry_m)))
                .build();


        customBanner = (CustomBanner) findViewById(R.id.customBanner);
        customBanner.setAdListener(new CustomBanner.AdListener() {
            @Override
            public void onClick(String url) {
                Log.i("MainActivity", "custom banner ad clicked");
            }
        });

        AdmobAdHelper.configureAndLoadBanner(MainActivity.this, (LinearLayout) findViewById(R.id.adView), RCUtils.SHOW_BANNER, RCUtils.ID_ADMOB_BANNER);
        amazonBanner = AmazonAdHelper.checkAndLoadBanner(MainActivity.this, (LinearLayout) findViewById(R.id.adViewAmazon), RCUtils.SHOW_BANNER_AMAZON, getString(R.string.amazon_b));
        ToneshubHelper.checkAndBindToneshubButton(this, getString(R.string.toneshub_id), (Button) findViewById(R.id.llGetRingTones), RCUtils.SHOW_TONESHUB);


    }


    @Override
    public void onResume() {
        super.onResume();

        if (RemoteConfigHelper.areAdsEnabled()) {
            PresageInterstitial presageInterstitial = new PresageInterstitial(this, getString(R.string.ogury_ad_unit_id));
            presageInterstitial.adToServe();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        if (amazonBanner != null) {
            amazonBanner.destroy();
        }
    }


    public void showIntersAd() {
        // usage with probability
        if (ad != null && showMainInterstitial()) {
            ad.showAds();
        }
    }


    public static boolean showMainInterstitial() {
        int rand = new Random().nextInt(100);
        long probability = getMain_interstitial_probability();
        return rand <= probability;
    }


    private static long getMain_interstitial_probability() {
        return RemoteConfigHelper.getConfigs().getLong(RCUtils.MAIN_PROBABILITY);
    }


}


