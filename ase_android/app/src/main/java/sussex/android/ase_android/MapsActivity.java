package sussex.android.ase_android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private GoogleMap mMap;
    final int LOCATION_PERMISSION_REQUEST_CODE = 1532;
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final HashMap loc = new HashMap<String, String>();
        final Handler handler = new Handler();
        final int delay = 10000;
        mMap = googleMap;

        enableMyLocation();

        final LocationManager service = (LocationManager)

                getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        assert service != null;
        final String provider = service.getBestProvider(criteria, false);



        handler.post(new Runnable(){
            Marker marker;
            public void run(){
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

                marker=mMap.addMarker(new MarkerOptions()
                        .position(myLocation)
                        .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_marker))
                        .title("Last known position")
                        .snippet("Lat/Lng: " +String.format(Locale.UK,"%.6f", myLocation.latitude) + " / " +  String.format(Locale.UK,"%.6f", myLocation.longitude)));;
                sendCoords(loc, user.getUid());
                handler.postDelayed(this, delay);
            }
        }else{
            Toast.makeText(MapsActivity.this, "no location permission", Toast.LENGTH_SHORT).show();
        }}});
    }

    public void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                } else {
                    Toast.makeText(this, "Location permisssion was denied.", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void sendCoords(HashMap coords, String id) {
        mDatabase.child("gpsdata").child(id).child(getUnixTime()).setValue(coords);
    }

    private String getUnixTime(){
        return Long.toString(System.currentTimeMillis());
    }
}