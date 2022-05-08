package com.roundG0929.hibike.activities.map_route;

import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;


//위험경로 지정 클래스
public class PathAreaCheckTest {

    public PathAreaCheckTest() {
    }

    //모든 경로 point 에 대한 범위 4 point 배열
    /**
     *
     * @param pathPoints 경로 배열
     * @return
     */
    public ArrayList<ArrayList<LatLng>> getAreaPointsList (ArrayList<LatLng> pathPoints){
        ArrayList<ArrayList<LatLng>> areaPointsList = new ArrayList<>();



        return areaPointsList;
    }


    //범위 구성 4 point 반환
    /**
     *
     * @param startPoint 경로중 한 직선 경로 부분의 시작 지점
     * @param endPoint 경로중 한 직선 경로 부분의 끝 지점
     * @return
     */
    public ArrayList<LatLng> getAreaPoints(LatLng startPoint, LatLng endPoint){
        ArrayList<LatLng> areaPoints = new ArrayList<>();

        double dy = 0;
        double dx = 0;
        double constant_endpoint = 0;
        double constant_startpoint = 0;


        dy = endPoint.latitude - startPoint.latitude;
        dx = endPoint.longitude - startPoint.longitude;
        double gradient_Area = -(dx/dy);// 경로 직선에 수직인 직선의 기울기

        constant_endpoint = getConstant(gradient_Area,endPoint);
        constant_startpoint = getConstant(gradient_Area,startPoint);


        ArrayList<LatLng> twoPoints = getTargetPoints(gradient_Area,startPoint,0.00008);
        areaPoints.add(twoPoints.get(0));
        areaPoints.add(twoPoints.get(1));
        twoPoints = getTargetPoints(gradient_Area,endPoint,0.00008);
        areaPoints.add(twoPoints.get(1));
        areaPoints.add(twoPoints.get(0));

        //순환을 위한 첫 point 추가
//        twoPoints = getTargetPoints(gradient_Area,startPoint,0.00008);
//        areaPoints.add(twoPoints.get(0));

        return  areaPoints;
    }

    //기울기와 한점을 알때 거리가 d 만큼 떨어진 점 2개 구하기
    private ArrayList<LatLng> getTargetPoints(double gradient, LatLng point, double distance){
        ArrayList<LatLng> twoPoints = new ArrayList<>();

        double dx = distance / Math.sqrt(1+(gradient*gradient));
        double dy = (distance * gradient) / Math.sqrt(1+(gradient*gradient));

        twoPoints.add(new LatLng(point.latitude+dy,point.longitude+dx));
        twoPoints.add(new LatLng(point.latitude-dy,point.longitude-dx));

        return twoPoints;
    }




    //y = ax + m 일떄 m (상수)구하기
    /**
     *
     * @param gradient 기울기
     * @param point 기울기와 겹치는 한점
     * @return
     */
    private double getConstant(double gradient,LatLng point){
        double constant_Point = 0;

        constant_Point = -(gradient*point.longitude) + point.latitude;

        return constant_Point;
    }
}
