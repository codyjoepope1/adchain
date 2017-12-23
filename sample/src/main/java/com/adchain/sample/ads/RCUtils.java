package com.adchain.sample.ads;


import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Created by a on 5.05.2017.
 */
public class RCUtils {
    private static final String TAG = "RCUtils";
    // TODO ADD REMOTE CONFIG KEYS HERE
    public static final String SHOW_ADMOB_1 = "show_admob_1";
    public static final String SHOW_FACEB_1 = "show_faceb_1";
    public static final String SHOW_FLURRY_1 = "show_flurry_1";
    public static final String SHOW_AMAZON_1 = "show_amazon_1";

    public static final String SHOW_ADMOB_2 = "show_admob_2";
    public static final String SHOW_FACEB_2 = "show_faceb_2";
    public static final String SHOW_FLURRY_2 = "show_flurry_2";
    public static final String SHOW_AMAZON_2 = "show_amazon_2";

    public static final String SHOW_MAIN = "show_main";
    public static final String MAIN_PROBABILITY = "main_probability";
    public static final String SHOW_BANNER = "show_banner";
    public static final String SHOW_BANNER_AMAZON = "show_banner_amazon";
    public static final String SHOW_TONESHUB = "show_toneshub";


    public static final String ID_FACEB_1 = "id_faceb_1";
    public static final String ID_FACEB_2 = "id_faceb_2";
    public static final String ID_FACEB_3 = "id_faceb_3";
    public static final String ID_ADMOB_1 = "id_admob_1";
    public static final String ID_ADMOB_2 = "id_admob_2";
    public static final String ID_ADMOB_3 = "id_admob_3";
    public static final String ID_ADMOB_BANNER = "id_admob_banner";


    public RCUtils() {
    }

    // TODO ADD REMOTE CONFIG DEFAULTS HERE

    @NonNull
    public static HashMap<String, Object> getDefaults() {
        HashMap<String, Object> defaults = new HashMap<>();
        //AD .CONFIGS
        defaults.put(SHOW_ADMOB_1, true);
        defaults.put(SHOW_FACEB_1, true);
        defaults.put(SHOW_FLURRY_1, true);
        defaults.put(SHOW_AMAZON_1, true);

        defaults.put(SHOW_ADMOB_2, true);
        defaults.put(SHOW_FACEB_2, true);
        defaults.put(SHOW_FLURRY_2, true);
        defaults.put(SHOW_AMAZON_2, true);

        defaults.put(SHOW_MAIN, true);
        defaults.put(MAIN_PROBABILITY, 100);

        defaults.put(SHOW_BANNER, true);
        defaults.put(SHOW_BANNER_AMAZON, false);
        defaults.put(SHOW_TONESHUB, true);

        // IDs
        defaults.put(ID_FACEB_1, "facebook_ids_here");
        defaults.put(ID_FACEB_2, "facebook_ids_here");
        defaults.put(ID_FACEB_3, "facebook_ids_here");

        defaults.put(ID_ADMOB_1, "ca-app-pub-admob/ids_here");
        defaults.put(ID_ADMOB_2, "ca-app-pub-admob/ids_here");
        defaults.put(ID_ADMOB_3, "ca-app-pub-admob/ids_here");
        defaults.put(ID_ADMOB_BANNER, "ca-app-pub-admob/ids_here");

        return defaults;
    }

}
