package study.zdf.admobnativedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Locale;

import static com.google.android.gms.ads.formats.NativeAdOptions.ADCHOICES_BOTTOM_RIGHT;

public class MainActivity extends AppCompatActivity {

    private UnifiedNativeAd nativeAd;
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/1044960115";
    private FrameLayout frameLayout;
    private TextView mTvCountDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        loadAd();
    }

    /**
     * 加载广告
     */
    private void loadAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, ADMOB_AD_UNIT_ID);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {

            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                frameLayout = findViewById(R.id.fl_admob_contain);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.admob_native_splash, null);
                mTvCountDown = adView.findViewById(R.id.tv_countdown);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });
        VideoOptions videoOptions = new VideoOptions.Builder()
                //设置静音播放
                .setStartMuted(true).build();

        //用于进一步定义广告设置，比如静音播放、设置广告位置等
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_BOTTOM_RIGHT).build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.i("11111", "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(MainActivity.this, "Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show();
                Log.i("11111", "onAdFailedToLoad");
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.i("11111", "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.i("11111", "onAdOpened");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i("11111", "onAdLoaded");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.i("11111", "onAdClicked");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.i("11111", "onAdImpression");
                // 倒计时效果
                beginCountDownClose();
            }

        }).build();

        //发起请求
        adLoader.loadAd(new AdRequest.Builder().build());

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


    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.mv_native_meaView));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.tv_native_title));
        adView.setBodyView(adView.findViewById(R.id.tv_native_desc));
        adView.setCallToActionView(adView.findViewById(R.id.btn_native_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        //设置父布局背景图
        Drawable drawable = nativeAd.getImages().get(0).getDrawable();
        frameLayout.setBackground(drawable);
        adView.setNativeAd(nativeAd);
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();
        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            Toast.makeText(this, "这是视频广告", Toast.LENGTH_LONG).show();
            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        } else {
            Toast.makeText(this, "这是图文广告", Toast.LENGTH_LONG).show();
        }
    }

}
