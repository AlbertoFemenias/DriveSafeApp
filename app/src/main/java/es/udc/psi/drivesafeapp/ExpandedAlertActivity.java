package es.udc.psi.drivesafeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.udc.psi.drivesafeapp.model.Alert;

public class ExpandedAlertActivity extends AppCompatActivity implements OnMapReadyCallback  {

    private DatabaseReference alertDatabase;

    private GoogleMap mMap;
    Alert alert;

    ImageView mainImageView;
    TextView hourTV, descriptionTV;
    int icons[] = {R.drawable.radar_icon, R.drawable.control_icon, R.drawable.obstacle_icon, R.drawable.helicopter_icon, R.drawable.warning_icon};
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expanded_alert);

        alertDatabase = FirebaseDatabase.getInstance().getReference("Alert");


        mainImageView = findViewById(R.id.mainImageView);
        hourTV = findViewById(R.id.hour);
        descriptionTV = findViewById(R.id.description);

        getData();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng pos = new LatLng(alert.getLatitude(), alert.getLongitude());

        mMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(alert.getTime())
                .snippet(alert.getDescription())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14));
    }

    private void getData(){
        if (getIntent().hasExtra("alert")){
            alert = (Alert) getIntent().getSerializableExtra("alert");

            hourTV.setText(alert.getTime());
            descriptionTV.setText(alert.getDescription());
            mainImageView.setImageResource(icons[alert.getCategory()]);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync( this);


        } else{
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expanded_alert, menu);

        //mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.shareButton)
        //        .getActionProvider();
        //mShareActionProvider.setShareIntent(doShare());

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_report) {
            //Toast.makeText(this, "REPORTAR ALERTA AHORA", Toast.LENGTH_LONG).show();
            alertDatabase.child(alert.getAlertid()).removeValue();
            onBackPressed();
            return true;
        }

        if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String str1 = "DriveSafeApp - Alert: "+ alert.getDescription() + "  " +alert.getTime();
            String str2 = "      https://maps.google.com/?q="+alert.getLatitude()+","+alert.getLongitude();
            sendIntent.putExtra(Intent.EXTRA_TEXT, str1+str2);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
