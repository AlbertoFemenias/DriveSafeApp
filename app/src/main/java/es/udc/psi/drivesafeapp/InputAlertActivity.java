package es.udc.psi.drivesafeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import es.udc.psi.drivesafeapp.model.Alert;

public class InputAlertActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    int LAUNCH_MAP_ACTIVITY = 111;
    boolean selectedCoordinates = false;
    EditText editDesc, editTitle;
    Spinner spinnerCat;
    Button btnSubmit;

    private GoogleMap mMap;
    private DatabaseReference alertDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_alert);

        //MAPA
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //ARRANCAR MAPA
        mapFragment.getMapAsync(this);

        alertDatabase = FirebaseDatabase.getInstance().getReference("Alert");

        editTitle = findViewById(R.id.editText_title);
        editDesc = findViewById(R.id.editText_desc);
        btnSubmit = findViewById(R.id.btn_submit);
        spinnerCat = findViewById(R.id.spinner_cat);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerCat.setAdapter(adapter);




        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCoordinates && editTitle.getText().toString().length()>5){
                    registerAlert();
                }else{
                    if(!selectedCoordinates)
                        showDialog("Debes seleccionar un punto en el mapa");
                    if(editTitle.getText().toString().length()<5)
                        showDialog("Debes introducir un título válido");
                }

            }
        });
    }

    public void showDialog(String data) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("FALTAN DATOS");
        alertDialog.setMessage(data);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void registerAlert (){
        int cat =  spinnerCat.getSelectedItemPosition();
        String title = editTitle.getText().toString();
        String desc = editDesc.getText().toString();
        double lat = 314;
        double lon = 159;


        String id = alertDatabase.push().getKey();
        Alert newAlert = new Alert(id, cat, title,  desc, lon, lat);
        alertDatabase.child(id).setValue(newAlert);
        //alertDatabase.setValue("Hello world");
        Toast.makeText(this, "Alerta registrada", Toast.LENGTH_LONG).show();
        finish();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
    }

    public void onMapClick (LatLng point) {
        Intent i = new Intent(this, MapsActivity.class);
        startActivityForResult(i, LAUNCH_MAP_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_MAP_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                selectedCoordinates = true;
                 Double lat =data.getDoubleExtra("lat", 0);
                 Double lon =data.getDoubleExtra("lon", 0);
                LatLng pos = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 8));

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

}
