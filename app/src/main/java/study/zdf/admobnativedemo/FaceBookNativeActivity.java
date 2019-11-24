package study.zdf.admobnativedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;

import java.util.ArrayList;
import java.util.List;

public class FaceBookNativeActivity extends AppCompatActivity {
    private NativeAdLayout nativeAdLayout;
    private View adView;
    private NativeAd nativeAd;
    private FrameLayout mFlFaceBookContain;
    private TextView mTvCountDown;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_book_native);

        mFlFaceBookContain = findViewById(R.id.fl_facebook_contain);
        adView = View.inflate(this, R.layout.facebook_native_splash, null);
        mTvCountDown = adView.findViewById(R.id.tv_countdown);
        //初始化
        AudienceNetworkAds.initialize(this);
        loadNativeAd();
    }

    private void loadNativeAd() {
        nativeAd = new NativeAd(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e("zdf", "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e("zdf", "Native ad failed to load: " + adError.getErrorMessage());

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d("zdf", "Native ad is loaded and ready to be displayed!");
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }

                // Inflate Native Ad into Container
                inflateAd(nativeAd);
                nativeAd.getAdCoverImage();
                Log.i("11111", nativeAd.getAdChoicesImageUrl());

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d("zdf", "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d("zdf", "Native ad impression logged!");
//                // 广告曝光1s后,显示倒计时跳过方法
//                handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 倒计时效果
//                        beginCountDownClose();
//                    }
//                }, 1000);//1秒后执行Runnable中的run方法
                beginCountDownClose();
            }
        });

        // Request an ad
        nativeAd.loadAd();
    }

    private void beginCountDownClose() {
        CountDownTimer timer = new CountDownTimer(5 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTvCountDown.setText("Skip: " + millisUntilFinished / 1000);

            }

            @Override
            public void onFinish() {
                mTvCountDown.setText("Skip");
            }

        }.start();
    }

    private void inflateAd(NativeAd nativeAd) {
        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
//        nativeAdLayout = findViewById(R.id.native_ad_container);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);

        mFlFaceBookContain.addView(adView);


//        // Add the AdOptionsView
//        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
//        AdOptionsView adOptionsView = new AdOptionsView(FaceBookNativeActivity.this, nativeAd, nativeAdLayout);
//        adChoicesContainer.removeAllViews();
//        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
//        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.tv_native_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.mv_native_meaView);
//        MediaView nativeAdMedia = new MediaView(this);
//        adView.addView(nativeAdMedia, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.tv_native_desc);
//        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.btn_native_call_to_action);


        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
//        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(adView, nativeAdMedia, clickableViews);
    }
}
