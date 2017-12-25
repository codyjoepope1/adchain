package com.adchain.toneshub.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Gust on 20.12.2017.
 */
public class BlinkButton extends Button {

    private Integer TONESHUB_PERIOD;
    private Handler handler = new Handler();
    private int counter = 0;
    private int color1;
    private int color2;

    Runnable r = new Runnable() {
        public void run() {
            try {
                setBackgroundColor(counter % 2 != 0 ? color1 : color2);
                setTextColor(counter % 2 == 0 ? color1 : color2);
                counter++;
                handler.postDelayed(r, TONESHUB_PERIOD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public BlinkButton(Context context) {
        super(context);
        this.TONESHUB_PERIOD = 1000;
    }

    public BlinkButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TONESHUB_PERIOD = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "period", 1000);
    }

    private BlinkButton(Context c, int period) {
        super(c);
        this.TONESHUB_PERIOD = period;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // View is now attached
        ColorDrawable buttonColor = (ColorDrawable) getBackground();
        this.color1 = buttonColor.getColor();

        this.color2 = getTextColors().getDefaultColor();

        handler.postDelayed(r, TONESHUB_PERIOD);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // View is now detached, and about to be destroyed
        if (handler != null && r != null) {
            handler.removeCallbacks(r);
        }
    }

}
