package es.udc.psi.drivesafeapp;

public class Alert {

    String alertid, category, description;
    Double longitude, latitude;

    public Alert(String alertid, String category, String description, Double longitude, Double latitude) {
        this.alertid = alertid;
        this.category = category;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAlertid() {
        return alertid;
    }

    public String getCategory() {
        return category;
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
