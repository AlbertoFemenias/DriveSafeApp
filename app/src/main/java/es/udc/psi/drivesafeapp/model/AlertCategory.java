package es.udc.psi.drivesafeapp.model;

public class AlertCategory {
    public static final int RADAR = 0;
    public static final int CONTROL = 1;
    public static final int OBSTACLE = 2;
    public static final int HELICOPTER = 3;
    public static final int WARNING = 0;

    private AlertCategory() {
        //Constructor privado para no poder instanciar
    }

}
