package com.example.gyu.geofence_test_2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//참조 사이트
//https://tsicilian.wordpress.com/2013/06/25/android-geofencing-with-google-maps/
public class MainActivity extends FragmentActivity {


    GoogleMap map;
    SimpleGeofence geofence;

    private static final long SECONDS_PER_HOUR = 60;
    private static final long MILLISECONDS_PER_SECOND = 1000;
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_TIME =
            GEOFENCE_EXPIRATION_IN_HOURS *
                    SECONDS_PER_HOUR *
                    MILLISECONDS_PER_SECOND;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        setContentView(R.layout.activity_main);
        map = ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map)).getMap(); // <- onCreate안에, setContentView 다음에 넣어야 nullpointer오류 안남
        CameraPosition INIT =
                new CameraPosition.Builder()
                        .target(new LatLng(37.5545, 126.971))
                        .zoom( 17.5F )
                        .bearing( 300F) // orientation
                        //.tilt( 50F) // viewing angle z축 회전
                        .build();
        // use GooggleMap mMap to move camera into position
        map.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
        //toggleView(map); <- 위성사진으로 바꿔줌
        geofence = new SimpleGeofence(
                "1",  // 앱 내에서의 지오펜스 식별자
                37.5545, //위도
                126.971, //경도
                20,//반지름
                GEOFENCE_EXPIRATION_TIME,
                Geofence.GEOFENCE_TRANSITION_ENTER);// This geofence records only entry transitions
        addMarkerForFence(geofence);


    }
    public void addMarkerForFence(SimpleGeofence fence){
        if(fence == null){
            // display en error message and return
            return;
        }
        map.addMarker( new MarkerOptions()
                .position( new LatLng(fence.getLatitude(), fence.getLongitude()) )
                .title("Fence " + fence.getId()) // 지오펜스 중심점에 표시할 핀셋의 이름
                .snippet("Radius: " + fence.getRadius()) ).showInfoWindow(); // 핀셋의 부가설명

//Instantiates a new CircleOptions object +  center/radius
        CircleOptions circleOptions = new CircleOptions()
                .center( new LatLng(fence.getLatitude(), fence.getLongitude()) )
                .radius(fence.getRadius())
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);

// Get back the mutable Circle
        Circle circle = map.addCircle(circleOptions); // map.addCircle(circleOptions); 만 써도 될듯.
// more operations on the circle...

    }

    public void scroll() {
        // we don't want to scroll too fast since
        // loading new areas in map takes time
        map.animateCamera( CameraUpdateFactory.scrollBy(10, -10),
                callback ); // 10 pix
    }

    private GoogleMap.CancelableCallback callback = new GoogleMap.CancelableCallback() {
        @Override
        public void onFinish() {
            scroll();
        }
        @Override
        public void onCancel() {}
    };

    public static void toggleView(GoogleMap map){
        map.setMapType(map.getMapType() ==
                GoogleMap.MAP_TYPE_NORMAL ?
                GoogleMap.MAP_TYPE_SATELLITE :
                GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
