package com.example.api3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey( "l7xx3b3f893778d04343a7a2fbd0e5bb1599" );

        tMapView.setCenterPoint( 127.346958, 36.369533);

        TMapData tMapData = new TMapData();
// 검색창
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("목적지 검색");
        final EditText name = new EditText(this);
        alert.setView(name);
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String strData = name.getText().toString();

                tMapData.findAllPOI(strData,
                        new TMapData.FindAllPOIListenerCallback() {
                            @Override
                            public void onFindAllPOI(ArrayList poiItem) {
                                for (int i = 0; i < poiItem.size(); i++) {
                                    TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                                    Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                            "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                                            "Point: " + item.getPOIPoint().toString());
                                }
                                TMapPOIItem item = (TMapPOIItem) poiItem.get(0);

                                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                                markerItem1.setTMapPoint(item.getPOIPoint()); // 마커의 좌표 지정
                                markerItem1.setName("검색위치"); // 마커의 타이틀 지정
                                tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가
                                tMapView.setCenterPoint(item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude());
                            }
                        });
            }
        });

        alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();

        linearLayoutTmap.addView( tMapView );
    }
}