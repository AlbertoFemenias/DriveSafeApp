package es.udc.psi.drivesafeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ExpandedAlertActivity extends AppCompatActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;

    ImageView mainImageView;
    TextView titleTV, descriptionTV;

    String titleText, descText;
    int icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expanded_alert);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

        mainImageView = findViewById(R.id.mainImageView);
        titleTV = findViewById(R.id.title);
        descriptionTV = findViewById(R.id.description);

        getData();
        loadData();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng coruña = new LatLng(43.3623, -8.4115);

        mMap.addMarker(new MarkerOptions().position(coruña).title("Alerta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coruña, 12));
    }

    private void getData(){
        if (getIntent().hasExtra("title") && getIntent().hasExtra("description") && getIntent().hasExtra("catIcon")){

            titleText = getIntent().getStringExtra("title");
            descText = getIntent().getStringExtra("description");
            icon = getIntent().getIntExtra("catIcon", 1);
        } else{

        }
    }

    private void loadData(){
        titleTV.setText(titleText);
        descriptionTV.setText(descText);
        mainImageView.setImageResource(icon);
    }
}
