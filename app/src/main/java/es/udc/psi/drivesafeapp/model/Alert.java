package es.udc.psi.drivesafeapp.model;

import java.io.Serializable;

public class Alert implements Serializable {

    private int category;
    private String alertid, time, description;
    private Double longitude, latitude;

    public Alert(String alertid, int category, String description, String time, Double longitude, Double latitude) {
        this.alertid = alertid;
        this.category = category;
        this.time = time;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAlertid() {
        return alertid;
    }

    public int getCategory() {
        return category;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }


}
