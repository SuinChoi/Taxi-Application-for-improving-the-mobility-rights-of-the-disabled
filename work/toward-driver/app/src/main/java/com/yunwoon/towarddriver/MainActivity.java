package com.yunwoon.towarddriver;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainFragment mainFragment = new MainFragment();
    private NoticeFragment noticeFragment = new NoticeFragment();
    private RuleFragment ruleFragment = new RuleFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags( //상태바 투명하게
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.relativeLayout, mainFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{ //BottomItemSelected
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
}
