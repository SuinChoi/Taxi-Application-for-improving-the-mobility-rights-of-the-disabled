package com.yunwoon.toward_driver;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;

public class ProcessActivity extends AppCompatActivity {

    Button start_navi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); //상태바 공간 사용
        getWindow().setStatusBarColor(Color.parseColor("#00ff0000")); // 상태바 투명 지정

        start_navi = findViewById(R.id.start_navi);
        start_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location destination = Location.newBuilder("카카오 판교 오피스", 127.10821222694533, 37.40205604363057).build();
                //선택 파라미터 정의 클래스
                //NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST).setRpOption(RpOption.SHORTEST).build();
                //KakaoNaviParams.Builder builder = KakaoNaviParams.newBuilder(destination).setNaviOptions(options);

                KakaoNaviParams.Builder builder = KakaoNaviParams.newBuilder(destination)
                        .setNaviOptions(NaviOptions.newBuilder().setCoordType(CoordType.WGS84).build());
                KakaoNaviService.getInstance().shareDestination(ProcessActivity.this, builder.build());
            }
        });
    }
/*
    public void startNavigation(View view){
        //카카오 내비 오픈 API 호출
        if(view.getId() == R.id.start_navi){
            //필수 파라미터, 목적지 정의 클래스
            Location destination = Location.newBuilder("카카오 판교 오피스", 127.10821222694533, 37.40205604363057).build();
            //선택 파라미터 정의 클래스
            NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST).setRpOption(RpOption.SHORTEST).build();

            KakaoNaviParams.Builder builder = KakaoNaviParams.newBuilder(destination).setNaviOptions(options);
            KakaoNaviService.getInstance().shareDestination(ProcessActivity.this,builder.build());

            //KakaoNaviService.getInstance().navigate(ProcessActivity.this,builder.build());
        }
    }
 */
}