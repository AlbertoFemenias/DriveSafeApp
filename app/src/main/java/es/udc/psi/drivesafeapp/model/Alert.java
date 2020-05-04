package es.udc.psi.drivesafeapp.model;

public class Alert {

    private int category;
    private String alertid, title, description;
    private Double longitude, latitude;

    public Alert(String alertid, int category, String title, String description, Double longitude, Double latitude) {
        this.alertid = alertid;
        this.category = category;
        this.title = title;
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

    public String getTitle() {
        return title;
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
