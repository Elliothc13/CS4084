package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng Limerick = new LatLng(52.663157, -8.619842);
        map.addMarker(new MarkerOptions().position(Limerick).title("Limerick"));

        LatLng GoGymLimerick = new LatLng(52.67461838920839, -8.67740927457319);
        map.addMarker(new MarkerOptions().position(GoGymLimerick).title("GO GYM Limerick"));

        LatLng CrossFitTreaty = new LatLng(52.674530819622305, -8.688913909418236);
        map.addMarker(new MarkerOptions().position(CrossFitTreaty).title("CrossFit Treaty"));

        LatLng TUSSportshub = new LatLng(52.67586268807097, -8.649654378521578);
        map.addMarker(new MarkerOptions().position(TUSSportshub).title("TUS Sportshub"));

        LatLng EnergizeHealthClub = new LatLng(52.665932960086586, -8.63165026187101);
        map.addMarker(new MarkerOptions().position(EnergizeHealthClub).title("Energize Health Club"));

        LatLng APGPersonalTraining = new LatLng(52.665932960086586, -8.63165026187101);
        map.addMarker(new MarkerOptions().position(APGPersonalTraining).title("APG Personal Training"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Limerick));
    }

}