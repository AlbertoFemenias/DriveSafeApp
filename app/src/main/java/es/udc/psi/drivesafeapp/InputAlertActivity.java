package es.udc.psi.drivesafeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.udc.psi.drivesafeapp.model.Alert;

public class InputAlertActivity extends AppCompatActivity {


    EditText editDesc, editTitle;
    Spinner spinnerCat;
    Button btnSubmit;


    private DatabaseReference alertDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_alert);

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
                registerAlert();
            }
        });
    }

    public void registerAlert (){
        int cat =  spinnerCat.getSelectedItemPosition();
        String title = editTitle.getText().toString();
        String desc = editDesc.getText().toString();
        double lat = 314;
        double lon = 159;

        if(!TextUtils.isEmpty(desc)) {
            String id = alertDatabase.push().getKey();
            Alert newAlert = new Alert(id, cat, title,  desc, lon, lat);
            alertDatabase.child(id).setValue(newAlert);
            //alertDatabase.setValue("Hello world");
            Toast.makeText(this, "Alerta registrada", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this, "DEBES ESCRIBIR UNA DESCRIPCION", Toast.LENGTH_LONG).show();
        }
    }
}
