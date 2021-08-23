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
        TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx3b3f893778d04343a7a2fbd0e5bb1599");

        TMapPoint tMapPointStart = new TMapPoint(37.570841, 126.985302); // SKT타워(출발지)
        TMapPoint tMapPointEnd = new TMapPoint(36.366717, 127.644623); // 충남대학교 공과대학 5호관(목적지)

        try {
            // TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd); 이 문장을 사용하기 위해서는 Thread로 사용한다.
            TMapData tmapdata = new TMapData();
            // tmpdata 객체 생성 O
            tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH,tMapPointStart,tMapPointEnd, new TMapData.FindPathDataListenerCallback(){
                // CAR_PATH : 차 경로
                @Override
                public void onFindPathData(TMapPolyLine tMapPolyLine){
                    TMapView mMapView = new TMapView();
                    // mMapView 객체 생성 오류 발생
                    // TMapView라는게 없는데 객체를 만들려고 해서 그럼
                    // alt + enter하면 TMapView 객체로 만들어지는데 대신 써야할 것은 무엇인가
                    mMapView.addTMapPath(tMapPolyLine);
                }
            });
            // 당연히 tMapPolyLine은 안되겠지 그렇게 선언한게 없으니깐
            // 아니면 내가 라인을 정의를 안해줘서 안되는걸까?
            // 그러면 라인 정의를 해주고 하면 되는건가?
            
            // 2개 해결하면 이제 출발/도착지 경로 표시 가능
            tpolyLine.setLineColor(Color.RED);
            tMapPolyLine.setLineWidth(2);
            tMapView.addTMapPolyLine("Line1", tMapPolyLine);

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