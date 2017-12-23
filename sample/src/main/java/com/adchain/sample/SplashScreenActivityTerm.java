package com.adchain.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.adchain.AdChainBuilder;
import com.adchain.adapters.AdmobAdAdapter;
import com.adchain.adapters.AmazonAdAdapter;
import com.adchain.adapters.FacebookAdAdapter;
import com.adchain.adapters.FlurryAdAdapter;
import com.adchain.sample.ads.RCUtils;


public class SplashScreenActivityTerm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_sceren_term);


        new AdChainBuilder(this)
                .showOnClick(R.id.button, 2000L) // it bind onClickListener to button and shows ads when clicked
                .add(FacebookAdAdapter.configureAndCreate(RCUtils.SHOW_FACEB_2, RCUtils.ID_FACEB_2))
                .add(AdmobAdAdapter.configureAndCreate(RCUtils.SHOW_ADMOB_2, RCUtils.ID_ADMOB_2))
                .add(AmazonAdAdapter.checkAndCreate(RCUtils.SHOW_AMAZON_2, getString(R.string.amazon_2)))
                .add(FlurryAdAdapter.checkAndCreate(RCUtils.SHOW_FLURRY_2, getString(R.string.flurry_2)))
                .startActivityAfterAdsFinished(MainActivity.class)
                .build();

    }

}
