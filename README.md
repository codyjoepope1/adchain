AdChain [![](https://jitpack.io/v/gs.oak/adchain.svg)](https://jitpack.io/#gs.oak/adchain)
========
AdChain helps you to show ads.

Features
--------
AdChain's unique set of features:

* RemoteConfigHelper: Helper class for remote configs.
* Custom Banner: Helps you to show custom banners via remote config urls.
* Admob: Interstitial and banner.
* Facebook: Interstitial, banner and native.
* Amazon: Interstitial and banner.
* Flurry: Interstitial and banner.
* Ogury: Interstitial.
* Unity: Interstitial.
* AppLovin: Banner.
* Toneshub: Make button as Toneshub link and feature BlinkButton view.

Add AdChain to your project:
----------------------------
AdChain is available on Jitpack. Please ensure that you are using the latest versions by [checking here](https://jitpack.io/#gs.oak/adchain)

Add the following Gradle configuration to your Android project:
```groovy
// In your root build.gradle file:
buildscript {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' } // add repository
    }
}

// In your app projects build.gradle file:

dependencies {
    compile 'gs.oak.adchain:config:0.6.4' // use only for remote config.
    compile 'gs.oak.adchain:adapter-admob:0.6.4' // add adapters you want
}
```

Init remote config in onCreate of each Activity class
```groovy
// In onCreate of Activites  class:
HashMap<String, Object> defaults = new HashMap<>();
defaults.put("show_splash1", true);
defaults.put("show_splash2", true);
// add default configs as hashmap ...
RemoteConfigHelper.init(defaults);
```

```groovy
// In your Activity class:
new AdChainBuilder(this)
		.showAuto() //Configuration
		.add(AdmobAdAdapter.configureAndCreate("remote_config_key_to_enable_or_disable_admob_ad", "remote_config_key_of_admob_ad_unit_id"))
		.add(FlurryAdAdapter.checkAndCreate("remote_config_key_to_enable_or_disable_flurry_ad", "flurry_ad_space_name"))
		.add(AmazonAdAdapter.create("amazon_app_key")
		.startActivityAfterAdsFinished(SplashScreen2Activity.class)
		.build();
```

__More Configurations__
- showAuto(): It displays ads automatically when they loaded.
- showOnClick(R.id.button_id): Displays ads when button (with id 'button_id') click. It binds listener automatically
- withStepByStep(true): Show one ad per calling 'showAds()' function of AdChain object. After all completed, it reload all ads.

[CHANGELOG](CHANGELOG.md)
------------------------------


