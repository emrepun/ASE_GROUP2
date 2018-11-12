package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class MapsPresenter implements MapsContract.Presenter {



    MapsContract.View view;

    public MapsPresenter(MapsContract.View view) {
        this.view = view;


    }





    /**
     * @param strAddress Address as String
     * @return Latitude and Longitude of first result of Directions API
     */
    private LatLng getLatLngFromAddress(String strAddress, Context context) {

        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> address = geocoder.getFromLocationName(strAddress, 1);
            if (address == null || address.size() == 0) {
                return null;
            }
            Address location = address.get(0);

            LatLng p1 = new LatLng((double) (location.getLatitude()),
                    (double) (location.getLongitude()));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
