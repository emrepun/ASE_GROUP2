package sussex.android.ase_android.MapsScreen.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class AdressInfo {
    private String pricePaid;
    private String address;
    private String date;

    /**
     * @param pricePaid price of the house
     * @param address address of the house
     * @param date date of house purchase
     */
    public AdressInfo(String pricePaid,String address, String date) {
        this.pricePaid = pricePaid;
        this.address = address;
        this.date = date;
    }

    public String getPricePaid() {
        return pricePaid;
    }

    public String getAddress(){return address;}

    public String getDate(){return date;}

}
