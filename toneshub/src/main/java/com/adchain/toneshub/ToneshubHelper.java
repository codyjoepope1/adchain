package com.adchain.toneshub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.adchain.config.RemoteConfigHelper;

/**
 * Created by Gust on 20.12.2017.
 */

public class ToneshubHelper {

    public static String getUrl(final Context context, final String toneshubId) {
        return context.getString(R.string.adchain_toneshub_link, toneshubId);
    }


    public static void checkAndBindToneshubButton(final Activity activity, final String toneshubId, Button button, String remoteConfigEnableKey) {
        boolean enable = RemoteConfigHelper.getConfigs().getBoolean(remoteConfigEnableKey);
        bindToneshubButton(activity, toneshubId, button, enable);

    }

    public static void bindToneshubButton(final Activity activity, final String toneshubId, Button button) {
        bindToneshubButton(activity, toneshubId, button, true);
    }

    private static void bindToneshubButton(final Activity activity, final String toneshubId, Button button, boolean enabled) {
        if (!enabled) {
            button.setVisibility(View.GONE);
            return;
        }
        button.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(button.getText())) {
            button.setText(activity.getString(R.string.adchain_toneshub_text));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(activity, toneshubId);
            }
        });
    }

    public static void openLink(Context context, String toneshubId) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(context, toneshubId)));
        context.startActivity(browserIntent);
    }

}