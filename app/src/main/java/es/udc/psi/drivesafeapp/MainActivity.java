package es.udc.psi.drivesafeapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.udc.psi.drivesafeapp.model.Alert;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //Tutoriales basicos
    //https://www.youtube.com/watch?v=2duc77R4Hqw
    //https://www.youtube.com/watch?v=foS6l8Wb1DM
    //https://www.youtube.com/watch?v=18VcnYN5_LM

    //cuenta de firebase:
    //fic.psi.1920@gmail.com
    //psi.fic.20


    private static final String TAG = "ViewDatabase";
    private static final int MY_PERMISSIONS_LOCATION = 666;
    LocationManager locateManager;
    private DatabaseReference alertDatabase;
    private DatabaseReference geoDatabase;
    private GeoFire geoFire;
    private RecyclerView recyclerView;
    AlertAdapter alertAdapter;
    ProgressDialog gpsLoadDialog;
    List<Alert> alertsArray = new ArrayList<>();
    GeoQuery geoQuery;
    boolean signalGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        signalGPS = false;

        recyclerView = findViewById(R.id.recyclerView);

        alertDatabase = FirebaseDatabase.getInstance().getReference().child("Alert"); //el nombre de la clase java es "Alert"
        geoDatabase = FirebaseDatabase.getInstance().getReference("Geo");
        geoFire = new GeoFire(geoDatabase);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputAlertActivity.class);
                startActivity(intent);
            }
        });


        //PERMISO GPS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //location manager
            locateManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locateManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        }

        //Set Up recyclerView
        alertAdapter = new AlertAdapter(this, alertsArray);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(alertAdapter);

        //la carga de alertas es asíncrona, mientras cargan se pone un dialogo de carga
        startGpsLoadingDialog();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locateManager != null) {
            locateManager.removeUpdates(this);
        }
    }

    

    void startGpsLoadingDialog() {
        gpsLoadDialog = new ProgressDialog(MainActivity.this);
        gpsLoadDialog.setTitle("Cargando..."); // Setting Message
        gpsLoadDialog.setMessage("Obteniendo ubicación del GPS"); // Setting Title
        gpsLoadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        gpsLoadDialog.setCancelable(false);
        gpsLoadDialog.show(); // Display Progress Dialog
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, String perm[], int[] grantRes) {
        switch (reqCode) {
            case MY_PERMISSIONS_LOCATION:
                if (grantRes.length > 0 && grantRes[0] == PackageManager.PERMISSION_GRANTED) {
                    //tenemos permisos pero es arriba donde ponemos a andar al location manager
                }else {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("SE NECESITAN PERMISOS DE GPS");
                    alertDialog.setMessage("La aplicación va a cerrarse");
                    alertDialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Runtime.getRuntime().exit(0);
                        }
                    }, 3000);
                }
                return;
        }
    }

    void makeLocationRadiusQuery(double lat, double lon, int rad){
        geoQuery = geoFire.queryAtLocation(new GeoLocation(lat, lon), rad);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {
                Log.d(TAG, String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                saveAlertFromKey(key);
            }
            @Override
            public void onKeyExited(String key) {
                Log.d("KEYOUT", "Key out:"+key);
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
            @Override
            public void onKeyMoved(String key, GeoLocation location) {}
            @Override
            public void onGeoQueryReady() {       }
            @Override
            public void onGeoQueryError(DatabaseError error) {}
        });

    }



    void saveAlertFromKey (final String key){
        alertDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (ds.getValue() != null) {
                    String id = ds.child("alertid").getValue(String.class);
                    int cat = ds.child("category").getValue(Integer.class);
                    String time = ds.child("time").getValue(String.class);
                    String desc = ds.child("description").getValue(String.class);
                    double lon = ds.child("longitude").getValue(Double.class);
                    double lat = ds.child("latitude").getValue(Double.class);
                    Alert newAlert = new Alert(id, cat, desc, time, lon, lat);
                    alertsArray.add(newAlert);
                    alertAdapter.notifyDataSetChanged();
                    Log.d("NEW ALERT FOUND", "Alert added to array");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    //LOCATOR
    @Override
    public void onLocationChanged(Location location) {
        if (!signalGPS){
            signalGPS = true;
            gpsLoadDialog.dismiss();
            makeLocationRadiusQuery(location.getLatitude(), location.getLongitude(), 50);
        }else{
                geoQuery.setCenter(new GeoLocation(location.getLatitude(), location.getLongitude()));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }



/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
