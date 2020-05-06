package es.udc.psi.drivesafeapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

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
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import es.udc.psi.drivesafeapp.model.Alert;

public class MainActivity extends AppCompatActivity {

    //Tutoriales basicos
    //https://www.youtube.com/watch?v=2duc77R4Hqw
    //https://www.youtube.com/watch?v=foS6l8Wb1DM
    //https://www.youtube.com/watch?v=18VcnYN5_LM

    //cuenta de firebase:
    //fic.psi.1920@gmail.com
    //psi.fic.20


    private static final String TAG = "ViewDatabase";
    private static final int MY_PERMISSIONS_LOCATION = 666;
    private DatabaseReference alertDatabase;
    private RecyclerView recyclerView;
    AlertAdapter alertAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        alertDatabase = FirebaseDatabase.getInstance().getReference().child("Alert"); //el nombre de la clase java es "Alert"

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

        //BD
        startLoadingDialog();
        alertDatabase.addValueEventListener(new ValueEventListener() {
            @Override //onDataChange se llama siempre que entras en la app o cuando algo cambia en la BD
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }


    void startLoadingDialog(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Cargando..."); // Setting Message
        progressDialog.setMessage("Se están cargando las alertas cercanas"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.show(); // Display Progress Dialog
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, String perm[], int[] grantRes) {
        switch (reqCode) {
            case MY_PERMISSIONS_LOCATION:
                if (grantRes.length > 0 && grantRes[0] == PackageManager.PERMISSION_GRANTED) {
                    //TENEMOS PERMISOS!!!
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
        } }



    private void showData(DataSnapshot dataSnapshot) {
        ArrayList<Alert> alertsArray = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            String id = ds.child("alertid").getValue(String.class);
            int cat = ds.child("category").getValue(Integer.class);
            String time = ds.child("time").getValue(String.class);
            String desc = ds.child("description").getValue(String.class);
            double lon = ds.child("longitude").getValue(Double.class);
            double lat = ds.child("latitude").getValue(Double.class);
            Alert newAlert = new Alert(id, cat, desc, time, lon, lat);
            alertsArray.add(newAlert);
        }
        alertAdapter =  new AlertAdapter(this, alertsArray);
        recyclerView.setAdapter(alertAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog.dismiss();
    }
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
