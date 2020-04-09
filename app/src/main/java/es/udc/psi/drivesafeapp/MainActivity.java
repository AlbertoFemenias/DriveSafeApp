package es.udc.psi.drivesafeapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Tutoriales basicos
    //https://www.youtube.com/watch?v=2duc77R4Hqw
    //https://www.youtube.com/watch?v=foS6l8Wb1DM

    //cuenta de firebase:
    //fic.psi.1920@gmail.com
    //psi.fic.20


    private static final String TAG = "ViewDatabase";
    private DatabaseReference alertDatabase;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = findViewById(R.id.listView_alerts);

        alertDatabase = FirebaseDatabase.getInstance().getReference().child("Alert"); //el nombre de la clase java es "Alert"

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputAlert.class);
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
        ArrayList<String> array = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            String cat = ds.child("category").getValue(String.class);
            String desc = ds.child("description").getValue(String.class);
            double lat = ds.child("latitude").getValue(Double.class);
            double lon = ds.child("longitude").getValue(Double.class);

            array.add(cat+": "+desc);

            Log.d("TAG",   "categoria: " + cat + " /description: " + desc + " /lat: " + lat + " /long: " + lon);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        mListView.setAdapter(adapter);



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
