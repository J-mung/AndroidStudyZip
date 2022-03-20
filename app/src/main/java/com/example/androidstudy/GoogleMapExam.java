package com.example.androidstudy;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class GoogleMapExam extends AppCompatActivity implements OnMapReadyCallback {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemapexam_main);

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // marker
        LatLng location = new LatLng(36.628837, 127.460645); // 위도와 경도
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("충북대");
        markerOptions.snippet("대학교");
        markerOptions.position(location);
        googleMap.addMarker(markerOptions);

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
    }
}
