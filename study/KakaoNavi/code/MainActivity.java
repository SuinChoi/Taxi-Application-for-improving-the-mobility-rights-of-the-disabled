package com.yunwoon.kakaonavisampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.KakaoNaviWebViewActivity;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;
import com.kakao.kakaonavi.options.RpOption;
import com.kakao.kakaonavi.options.VehicleType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity{
    Button button;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String keyhash = getKeyHash(this);
        Log.e("KEYHASH",keyhash);

        button = findViewById(R.id.button);
        webView = findViewById(R.id.web_view);
    }

    //KeyHash 값 구하기
    //카카오 개발자 사이트 내 등록
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("SIGN", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    //웹뷰 동작
    public void openWebView(View view){
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://m.naver.com");
    }

    /**
     * 카카오 내비 동작 - 목적지 공유
     * https://developers.kakao.com/docs/android/kakaonavi-api
     * "Uncaught TypeError: Cannot read property 'Valid' of null"  <- 이 오류 계속 발생 ,, 폰으로 실험해보자★
     */
    public void openNavigation(View view){
        if(view.getId() == R.id.button2){
            Location destination = Location.newBuilder("카카오 판교 오피스", 127.10821222694533, 37.40205604363057).build();
            KakaoNaviParams.Builder builder = KakaoNaviParams.newBuilder(destination).setNaviOptions(NaviOptions.newBuilder().setCoordType(CoordType.WGS84).build());
            KakaoNaviService.getInstance().shareDestination(MainActivity.this, builder.build());
        }
    }
}