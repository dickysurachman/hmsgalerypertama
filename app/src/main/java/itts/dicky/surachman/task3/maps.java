package itts.dicky.surachman.task3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.SupportMapFragment;

public class maps extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mMapView;
    private SupportMapFragment mSupportMapFragment;
    //@RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE})
    @Override
    public void onMapReady(HuaweiMap map){
        HuaweiMap hMap = map;
        // Enable the my-location layer.
        hMap.setMyLocationEnabled(true);
        // Enable the my-location icon.
        hMap.getUiSettings().setMyLocationButtonEnabled(true);
        //hMap.moveCamera();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment_mapfragmentdemo);
        mSupportMapFragment.getMapAsync(this);
        Toast.makeText(getApplicationContext(), "Click navigation icon  on the bottom for show your location in Maps...",Toast.LENGTH_SHORT).show();

    }
}