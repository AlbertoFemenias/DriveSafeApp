package es.udc.psi.drivesafeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.udc.psi.drivesafeapp.model.Alert;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.MyViewHolder> {

    ArrayList<Alert> alertsArray;
    int icons[] = {R.drawable.radar_icon, R.drawable.control_icon, R.drawable.obstacle_icon, R.drawable.helicopter_icon, R.drawable.warning_icon};
    Context context;


    public AlertAdapter(Context ctx, ArrayList<Alert> alerts){
        context = ctx;
        alertsArray = alerts;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alert_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String time = alertsArray.get(position).getTime();
        String description = alertsArray.get(position).getDescription();
        int catIcon = icons[alertsArray.get(position).getCategory()];

        holder.time.setText(time);
        holder.description.setText(description);
        holder.icon.setImageResource(catIcon);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExpandedAlertActivity.class);
                intent.putExtra("alert", alertsArray.get(position));
                context.startActivity(intent);
            }

        });

    }

    @Override
    public int getItemCount() {

        return alertsArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time, description;
        ImageView icon;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            time = itemView.findViewById(R.id.rowTime);
            description = itemView.findViewById(R.id.rowDescription);
            icon = itemView.findViewById(R.id.rowIconView);
            mainLayout = itemView.findViewById(R.id.alertRowLayout);

        }
    }
}
