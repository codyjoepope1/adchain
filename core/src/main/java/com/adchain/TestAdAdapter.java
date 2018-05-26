package com.adchain;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;

import com.adchain.sample.TestAdActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gust on 19.12.2017.
 */
public class TestAdAdapter extends AdChainAdapter {
    private final int delay;
    public String name;
    private boolean loaded;
    private boolean errorTest;

    public static TestAdAdapter error(String name, int delay) {
        return new TestAdAdapter(null, name, delay, true);
    }
    public static TestAdAdapter create(String name, int delay) {
        return new TestAdAdapter(null, name, delay, false);
    }

    private TestAdAdapter(AdConfiguration adConfiguration, String name, int delay, boolean errorTest) {
        super(adConfiguration);
        this.name = name;
        this.delay = delay;
        this.errorTest = errorTest;
    }

    @Override
    public void init() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (errorTest) {
                    error("Test Error Occurred");
                } else {
                    loaded = true;
                    loaded();
                }
            }
        }, delay);
    }


    @Override
    public boolean isAdLoaded() {
        return loaded;
    }

    public static Map<String, DialogInterface.OnClickListener> hm = new HashMap<>();

    @Override
    public void showAd() {
        loaded = false;
        Intent intent = new Intent(getActivity(), TestAdActivity.class);
        intent.putExtra("name", name);
        hm.put(name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closed();
            }
        });
        getActivity().startActivity(intent);


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                closed();
//            }
//        }, 250);
    }

    @Override
    public void destroy() {
    }

}
