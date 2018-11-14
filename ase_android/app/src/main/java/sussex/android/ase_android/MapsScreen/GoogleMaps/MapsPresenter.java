package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sussex.android.ase_android.MapsScreen.model.JSONparser;
import sussex.android.ase_android.R;

public class MapsPresenter implements MapsContract.Presenter {



    private MapsContract.View view;
    private JSONparser jsonparser;
    private ArrayList<MarkerOptions> markers=new ArrayList<>();

    public MapsPresenter(MapsContract.View view) {
        this.view = view;
        jsonparser=new JSONparser(view.getActivity());
    }



    public void initialize() {
    }

    @Override
    public void addMarkerToList(double lat, double lon, double price, String postcode) {
        markers.add(new MarkerOptions()
                .position(new LatLng(lat,lon))
                .icon(bitmapDescriptorFromVector(view.getActivity(), R.drawable.ic_marker))
                .title(postcode)
                .snippet("Average price: £" + String.format(Locale.UK,"%,.2f", price)));

    }

    @Override
    public void displayMarkers() {
        view.clearMap();
        for (MarkerOptions options: markers) {
            view.addMarker(options);
        }
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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    public void cameraPosChanged(CameraPosition cameraPosition) {
        jsonparser.markerJsonParse(this, cameraPosition.target.latitude, cameraPosition.target.longitude);
    }

    public JSONparser getJsonParser(){
        return jsonparser;
    }
}
