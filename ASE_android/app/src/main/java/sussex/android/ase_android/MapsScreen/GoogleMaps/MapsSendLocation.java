package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import sussex.android.ase_android.CustomInfoWindowAdapter;
import sussex.android.ase_android.R;

import static android.content.Context.LOCATION_SERVICE;

public class MapsSendLocation {


    //send location feature
    private final DatabaseReference mDatabase;
    private final FirebaseUser user;
    final int delay = 10000;



    MapsContract.View view;

    public MapsSendLocation(MapsContract.View view) {
        this.view = view;


        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
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


    private void startSendLocationRoutine(final GoogleMap mMap){
        final LocationManager service = (LocationManager)
                view.getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        assert service != null;
        final String provider = service.getBestProvider(criteria, false);
        final HashMap loc = new HashMap<String, String>();


        final Handler handler=new Handler();
        handler.post(new Runnable(){
            Marker marker;
            public void run(){
                if (ActivityCompat.checkSelfPermission(view.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(view.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location location = service.getLastKnownLocation(provider);
                    if(location!=null){
                        LatLng myLocation = new LatLng(location.getLatitude(),
                                location.getLongitude());
                        loc.put("latitude", location.getLatitude());
                        loc.put("longitude", location.getLongitude());

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                                14));
                        if (marker!=null)
                            marker.remove();

                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(view.getActivity()));

                        marker=mMap.addMarker(new MarkerOptions()
                                .position(myLocation)
                                .icon(bitmapDescriptorFromVector(view.getActivity(), R.drawable.ic_marker))
                                .title("Last known position")
                                .snippet("Lat/Lng: " +String.format(Locale.UK,"%.6f", myLocation.latitude) + " / " +  String.format(Locale.UK,"%.6f", myLocation.longitude)));;
                        sendCoords(loc, user.getUid());
                        handler.postDelayed(this, delay);
                    }
                }else{
                    Toast.makeText(view.getActivity(), "no location permission", Toast.LENGTH_SHORT).show();
                }}});
    }

    private void sendCoords(HashMap coords, String id) {
        mDatabase.child("gpsdata").child(id).child(getUnixTime()).setValue(coords);
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }
    private String getUnixTime(){
        return Long.toString(System.currentTimeMillis());
    }

}
