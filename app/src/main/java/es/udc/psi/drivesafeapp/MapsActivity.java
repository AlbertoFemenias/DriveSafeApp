package es.udc.psi.drivesafeapp;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import es.udc.psi.drivesafeapp.R;


/**
 * A demo class that stores and retrieves data objects with each marker.
 */
public class MapsActivity extends FragmentActivity implements GoogleMap.OnCameraMoveListener,   OnMapReadyCallback {

    private static LatLng MADRID = new LatLng(40.4168, -3.7038);

    private Marker myMarker;
    private GoogleMap mMap;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSubmit = findViewById(R.id.map_button);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("lat", mMap.getCameraPosition().target.latitude);
                returnIntent.putExtra("lon", mMap.getCameraPosition().target.longitude);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

    }

    /** Called when the map is ready. */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true); // true to enable
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);

        mMap.setOnCameraMoveListener(this);

        myMarker = mMap.addMarker(new MarkerOptions()
                .position(MADRID)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        myMarker.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MADRID, 8));

    }


    @Override
    public void onCameraMove() {
        Log.d("CAMERA", "new position "+mMap.getCameraPosition().target);
        myMarker.setPosition(mMap.getCameraPosition().target);
    }
}

