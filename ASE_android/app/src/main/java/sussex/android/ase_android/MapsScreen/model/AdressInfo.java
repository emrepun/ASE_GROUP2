package sussex.android.ase_android.MapsScreen.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class AdressInfo {
    private String address;
    private String price;
    private String date;

    /**
     * @param address address of the house
     * @param price price of the house
     * @param date date of house purchase
     */
    public AdressInfo(String address, String price, String date) {
        this.address = address;
        this.price= price;
        this.date = date;
    }

    public String getAddress(){return address;}

    public String getPrice() { return price; }

    public String getDate(){return date;}

}
