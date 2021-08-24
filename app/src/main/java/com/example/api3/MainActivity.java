package com.example.api3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한 ID를 가져온다
        int permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        // 권한이 열려있는지 확인
        if (permission1 == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
            // 마쉬멜로우 이상버전부터 권한을 물어본다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 권한 체크(LOCATION_STATE의 requestCode를 1000으로 세팅
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            }
            return;
        }

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        final TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx3b3f893778d04343a7a2fbd0e5bb1599");

        TMapPoint tMapPointStart = new TMapPoint(37.566389, 126.985303); // SKT타워(출발지)
        TMapPoint tMapPointEnd = new TMapPoint(36.366548, 127.344537); // 충남대학교 공과대학 5호관(목적지)
        // 제대로 좌표 설정이 안되어있으니깐 다시 설정 필요

        try {
            // TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd); 이 문장을 사용하기 위해서는 Thread로 사용한다.
            TMapData tmapdata = new TMapData();
            // tmpdata 객체 생성
            tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH,tMapPointStart,tMapPointEnd, new TMapData.FindPathDataListenerCallback(){
                // CAR_PATH : 차 경로
                @Override
                public void onFindPathData(TMapPolyLine tMapPolyLine){
                    // TMapView 객체는 위에 final로 선언되어 있음
                    // final을 써야 되는 이유 : Variable 'tMapView' is accessed from within inner class, needs to be declared final
                    tMapView.addTMapPath(tMapPolyLine); // add TMapPath()는 출발 마크, 도착 마크, 경로 이런걸 종합적으로 추가해주는 메소드

                }
            });
            // 만약 아래처럼 하고 싶다면 길 색깔 정하고 width 정하고 addTMapPolyLine() 사용하면 경로만 띄워지게 할 수 있다.
            // 근데 addTMapPath()처럼 출발, 도착 마크 이런거 설정을 따로 해줘야 된다는 번거로움이 있다.
            // tpolyLine.setLineColor(Color.RED);
            // tMapPolyLine.setLineWidth(2);
            // tMapView.addTMapPolyLine("Line1", tMapPolyLine);

        } catch (Exception e) {
            e.printStackTrace();
        }

        linearLayoutTmap.addView(tMapView);
    }
    // 권한 체크 이후 로직
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        if (requestCode == 1000) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result == true) {
            } else {
                finish();
            }
        }
    }
}