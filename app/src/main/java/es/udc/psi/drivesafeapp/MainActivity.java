package es.udc.psi.drivesafeapp;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

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
    private DatabaseReference alertDatabase;
    private RecyclerView recyclerView;
    AlertAdapter alertAdapter;

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

        alertDatabase.addValueEventListener(new ValueEventListener() {
            @Override //onDataChange se llama siempre que entras en la app o cuando algo cambia en la BD
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void showData(DataSnapshot dataSnapshot) {
        ArrayList<Alert> alertsArray = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            String id = ds.child("alertid").getValue(String.class);
            int cat = ds.child("category").getValue(Integer.class);
            String title = ds.child("title").getValue(String.class);
            String desc = ds.child("description").getValue(String.class);
            double lon = ds.child("longitude").getValue(Double.class);
            double lat = ds.child("latitude").getValue(Double.class);
            Alert newAlert = new Alert(id, cat, title, desc, lon, lat);
            alertsArray.add(newAlert);
        }
        alertAdapter =  new AlertAdapter(this, alertsArray);
        recyclerView.setAdapter(alertAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
