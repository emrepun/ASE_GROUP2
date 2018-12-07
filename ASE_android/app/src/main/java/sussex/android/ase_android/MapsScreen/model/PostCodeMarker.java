package sussex.android.ase_android.MapsScreen.model;

public class PostCodeMarker {
    private double lat;
    private double lon;
    private double price;
    private String postcode;
    private int type;

    public static final int POLICE_MARKER=1;
    public static final int HOUSE_MARKER=2;

    /**
     * @param lat Latitude
     * @param lon Longitude
     * @param price Average price of the houses in the postcode
     * @param info Postcode or crime category
     * @param type type of the marker
     */
    public PostCodeMarker(double lat, double lon, double price, String info, int type) {
        this.lat = lat;
        this.lon = lon;
        this.price = price;
        this.postcode = info;
        this.type=type;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getPrice() {
        return price;
    }

    public String getPostcode() {
        return postcode;
    }

    public int getType() {
        return type;
    }
}
