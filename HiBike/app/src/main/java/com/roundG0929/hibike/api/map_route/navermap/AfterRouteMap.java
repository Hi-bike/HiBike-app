package com.roundG0929.hibike.api.map_route.navermap;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto.GraphhopperResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AfterRouteMap implements OnMapReadyCallback {
    NaverMap routeMap;
    FusedLocationSource fusedLocationSource;
    Context mContext;
    GraphhopperResponse graphhopperResponse;
    List<LatLng> routePoints = new ArrayList<>();

    public AfterRouteMap(Context context,
                         FusedLocationSource fusedLocationSource,
                         GraphhopperResponse graphhopperResponse,
                         List<LatLng> coordsForDrawLine) {

        this.fusedLocationSource = fusedLocationSource;
        this.mContext = context;
        routePoints.addAll(coordsForDrawLine);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        routeMap = naverMap;

        //오버레이 설정
            //자전거길 오버레이
        routeMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, true);

        //내위치 마커 오버레이 설정
        LocationOverlay locationOverlay = routeMap.getLocationOverlay();
        locationOverlay.setVisible(true);

        //내위치소스
        routeMap.setLocationSource(fusedLocationSource);
        routeMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);

        //ui 설정
        UiSettings uiSettings = routeMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        //길찾기 위치로 이동
        //Toast.makeText(mContext,routePoints.get(0).latitude+","+routePoints.get(0).longitude,Toast.LENGTH_SHORT).show();
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(routePoints.get(0));
        naverMap.moveCamera(cameraUpdate);

        //경로그리기
        PathOverlay pathOverlay = new PathOverlay();
            //경로배열지정
        pathOverlay.setCoords(routePoints);
            //스타일
        pathOverlay.setColor(Color.BLUE);
            //그리기
        pathOverlay.setMap(routeMap);

    }
}
