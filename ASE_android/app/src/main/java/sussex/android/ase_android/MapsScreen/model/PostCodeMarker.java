package sussex.android.ase_android.MapsScreen.model;

public class PostCodeMarker {
    private double lat;
    private double lon;
    private double price;
    private String postcode;

    /**
     * @param lat Latitude
     * @param lon Longitude
     * @param price Average price of the houses in the postcode
     * @param postcode Postcode
     */
    public PostCodeMarker(double lat, double lon, double price, String postcode) {
        this.lat = lat;
        this.lon = lon;
        this.price = price;
        this.postcode = postcode;
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
}
