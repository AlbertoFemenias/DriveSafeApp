package es.udc.psi.drivesafeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandedAlertActivity extends AppCompatActivity {

    ImageView mainImageView;
    TextView titleTV, descriptionTV;

    String titleText, descText;
    int icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expanded_alert);

        mainImageView = findViewById(R.id.mainImageView);
        titleTV = findViewById(R.id.title);
        descriptionTV = findViewById(R.id.description);

        getData();
        loadData();
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
