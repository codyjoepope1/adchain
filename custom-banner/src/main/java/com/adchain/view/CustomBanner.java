package com.adchain.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.adchain.config.RemoteConfigHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * Created by a on 20.12.2017.
 */
public class CustomBanner extends ImageView {

    public static final String TAG = "CustomBanner";
    public static final String TAG_PREFIX = "http://schemas.android.com/apk/res-auto";

    public AdListener adListener;
    private String remoteConfigTargetUrlKey;
    private String remoteConfigImageUrlKey;
    private String defaultTargetUrl;
    private String defaultImageUrl;

    public CustomBanner(Context context) {
        super(context);
    }

    public CustomBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.remoteConfigTargetUrlKey = attrs.getAttributeValue(TAG_PREFIX, "remoteConfigTargetUrlKey");
        this.remoteConfigImageUrlKey = attrs.getAttributeValue(TAG_PREFIX, "remoteConfigImageUrlKey");
        this.defaultTargetUrl = attrs.getAttributeValue(TAG_PREFIX, "defaultTargetUrl");
        this.defaultImageUrl = attrs.getAttributeValue(TAG_PREFIX, "defaultImageUrl");
    }


    public AdListener getAdListener() {
        return adListener;
    }

    public void setAdListener(AdListener adListener) {
        this.adListener = adListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // View is now attached

        if (!TextUtils.isEmpty(this.remoteConfigTargetUrlKey) && !TextUtils.isEmpty(this.remoteConfigImageUrlKey)) {
            String imageUrl = RemoteConfigHelper.getConfigs().getString(remoteConfigImageUrlKey);
            String targetUrl = RemoteConfigHelper.getConfigs().getString(remoteConfigTargetUrlKey);

            if (!TextUtils.isEmpty(imageUrl) && !TextUtils.isEmpty(targetUrl)) {
                loadBanner(imageUrl, targetUrl);
                return;
            }
        }


        // no config fetched. check default values
        if (!TextUtils.isEmpty(defaultImageUrl) && !TextUtils.isEmpty(defaultTargetUrl)) {
            loadBanner(defaultImageUrl, defaultTargetUrl);
        }
    }

    private void loadBanner(final String imageUrl, final String targetUrl) {
        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d(TAG, "load failed.", e);
                        setVisibility(View.GONE);

                        if (!TextUtils.isEmpty(defaultImageUrl) && !TextUtils.isEmpty(defaultTargetUrl)) {
                            if (!imageUrl.equals(defaultImageUrl)) {
                                loadBanner(defaultImageUrl, defaultTargetUrl);
                            }
                        }
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openUrl(getContext(), targetUrl);
                            }
                        });
                        return false;
                    }
                })
                .apply(new RequestOptions().override(getLayoutParams().width, Target.SIZE_ORIGINAL))
                .into(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // View is now detached, and about to be destroyed

    }


    private void openUrl(Context c, String url) {
        try {
            if (adListener != null) {
                adListener.onClick(url);
            }
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            c.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "couldn't open url");
        }
    }

    public interface AdListener {
        void onClick(String url);
    }
}
