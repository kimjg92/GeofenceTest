package com.example.gyu.geofence_test_2;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by GYU on 2015-11-21.
 */
public class GeofenceManager {
    /*
    private static final long SECONDS_PER_HOUR = 60;
    private static final long MILLISECONDS_PER_SECOND = 1000;
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_TIME =
            GEOFENCE_EXPIRATION_IN_HOURS *
                    SECONDS_PER_HOUR *
                    MILLISECONDS_PER_SECOND;
                    */
    private float radius;

    ArrayList<SimpleGeofence> GeofenceList = new ArrayList<>(); // 나중에 해쉬맵을 이용해서 검색시 for문이 아닌 key로 검색할 수 있게
    LatLng latLng;
    GoogleMap map;

    public GeofenceManager (GoogleMap Map){
        map = Map; // Geofence를 띄울 map Fragment를 받아옴
        radius = 20.0f; // 기본 값을 20으로 설정
    }

    public SimpleGeofence CreateGeofence (String geofenceID, double latitude, double longitude, long expiration, int transition){
        // 반지름은 manager에서 일괄 관리. 기본값은 생성자에서 할당.
        SimpleGeofence newGeofence = new SimpleGeofence(geofenceID, latitude, longitude, radius, expiration, transition);
        return  newGeofence;
    }
    public void setRadius(float rad){
        radius = rad;
    }
    public void addGeofenceList(SimpleGeofence addGeofence){
        //지오펜스 리스트에 지오펜스를 넣음
        GeofenceList.add(addGeofence);
    }
    public SimpleGeofence getGeofenceById(String GeofenceID){
        //지오펜스 고유의 ID로 지오펜스 객체를 찾음
        SimpleGeofence ret = null;
        for (int i = 0 ; i < GeofenceList.size() ; i ++){
            if(GeofenceList.get(i).getId().toString().equals(GeofenceID.toString())){
                ret = GeofenceList.get(i);
            }
        }
        return ret;
    }

    public void addMarkerForFence(){
        for(int i = 0 ; i < GeofenceList.size() ; i ++){
            if(GeofenceList.get(i) == null){
                // display en error message and return
                return;
            }
            map.addMarker( new MarkerOptions()
                    .position( new LatLng(GeofenceList.get(i).getLatitude(), GeofenceList.get(i).getLongitude()) )
                    .title("Fence " + GeofenceList.get(i).getId()) // 지오펜스 중심점에 표시할 핀셋의 이름
                    .snippet("Radius: " + radius) ).showInfoWindow(); // 핀셋의 부가설명

//Instantiates a new CircleOptions object +  center/radius
            CircleOptions circleOptions = new CircleOptions()
                    .center( new LatLng(GeofenceList.get(i).getLatitude(), GeofenceList.get(i).getLongitude()) )
                    .radius(radius) // 기존엔 .radius(GeofenceList.get(i).getRadius()) 였지만 GeofenceManager에서 일괄적인 반지름을 부여하고 관리하기위해 변경
                    .fillColor(0x40ff0000)
                    .strokeColor(Color.TRANSPARENT)
                    .strokeWidth(2);

// Get back the mutable Circle
            Circle circle = map.addCircle(circleOptions); // map.addCircle(circleOptions); 만 써도 될듯.
// more operations on the circle...
        }


    }

}
