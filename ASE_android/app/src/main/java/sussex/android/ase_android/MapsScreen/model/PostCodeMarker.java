package sussex.android.ase_android.MapsScreen.model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.Locale;

public class PostCodeMarker implements ClusterItem {
    private double lat;
    private double lon;
    private double price;
    private String postcode;
    private int type;


    private String snippet;
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

        if(type==HOUSE_MARKER){
            if(price>0){
                snippet=("Average price: £" + String.format(Locale.UK, "%,.2f", price));
            }else{
                snippet=("Unknown average price.");
            }
        }else{
            snippet="Occurred within the last month.";
        }
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

    @Override
    public LatLng getPosition() {
        return new LatLng(lat,lon);
    }

    @Override
    public String getTitle() {
        return postcode;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

}
