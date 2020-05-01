package com.yunwoon.toward_driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity {

                private FragmentManager fragmentManager = getSupportFragmentManager();
                private MainFragment mainFragment = new MainFragment();
                private NoticeFragment noticeFragment = new NoticeFragment();
                private RuleFragment ruleFragment = new RuleFragment();

                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_main);

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); //상태바 공간 사용
                    getWindow().setStatusBarColor(Color.parseColor("#00ff0000")); // 상태바 투명 지정
                    getWindow().getDecorView().setSystemUiVisibility( //상태바 글자색 검정 변경
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    );

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.relativeLayout, mainFragment).commitAllowingStateLoss();

                    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
                    bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

                    String keyhash = getKeyHash(this);
                    Log.e("KEYHASH",keyhash);
                }

                class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{ //BottomItemSelected
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        switch (menuItem.getItemId()){
                            case R.id.main:
                                transaction.replace(R.id.relativeLayout,mainFragment).commitAllowingStateLoss();
                                break;
                            case R.id.notice:
                                transaction.replace(R.id.relativeLayout,noticeFragment).commitAllowingStateLoss();
                                break;
                            case R.id.rule:
                                transaction.replace(R.id.relativeLayout,ruleFragment).commitAllowingStateLoss();
                                break;
                        }
                        return true;
                    }
                }

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
}
