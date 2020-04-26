package com.yunwoon.gmap_example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Google APIs - Maps SDK for Android - 사용 어플 API 키 등록
 * cmd 창을 통해 SHA-1 인증서 지문 얻기
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //지정한 ID 같는 프래그먼트 핸들 가져옴
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //getMapAsync() 메서드로 GoogleMap 객체 준비시 실행될 콜백 등록
        mapFragment.getMapAsync(this);
    }

    /**
     * onMapReadyCallback 인터페이스의 onMapReady 메서드
     * 맵이 사용할 준비가 되었을 때 (NULL이 아닌 GoogleMap 객체를 파라미터로 제공해 줄 수 있을 때),
     * 호출되어지는 메서드
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng SEOUL = new LatLng(37.56,126.97);
        //MarkerOptions로 마커나 표시될 위치/타이틀/클릭시설명 설정
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        //addMarker 메서드로 GoogleMap 객체 추가 시 지도에 표시됨
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL)); //카메라를 지정한 경도,위도로 이동
        map.animateCamera(CameraUpdateFactory.zoomTo(10)); //지정한 단계로 카메라 줌 조정
    }
}
