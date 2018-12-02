package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import sussex.android.ase_android.CustomInfoWindowAdapter;
import sussex.android.ase_android.MapsScreen.BottomSheet.BottomSheetContract;
import sussex.android.ase_android.MapsScreen.BottomSheet.BottomSheetView;
import sussex.android.ase_android.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,MapsContract.View {


    private GoogleMap mMap;
    final int LOCATION_PERMISSION_REQUEST_CODE = 1532;
    View bottomSheet;

    private MapsPresenter mapsPresenter;
    private BottomSheetContract.View bottomSheetView;

    boolean enableInfoWindow =true;


    private boolean heatMapEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        heatMapEnabled=false;

        //create MapsPresenter
        mapsPresenter=new MapsPresenter(this);

        bottomSheet = findViewById(R.id.pullUp_bottom_sheet);
        bottomSheetView = new BottomSheetView(bottomSheet, mapsPresenter, this);


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
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        enableMyLocation();

        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));

        //set Listener for hiding and displaying the BottomSheet
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(enableInfoWindow) {
                    bottomSheetView.displayBottomSheet(marker.getTitle(), marker.getSnippet());
                }
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                bottomSheetView.hideBottomSheet();
            }
        });
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));

        //Set listener for notifying the presenter when the camera position changes
        final CameraPosition[] mPreviousCameraPosition = {null};
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition position = mMap.getCameraPosition();
                if(mPreviousCameraPosition[0] == null || !mPreviousCameraPosition[0].equals(position)) {
                    //position changed
                    mPreviousCameraPosition[0] = mMap.getCameraPosition();

                    float radius_meter = calcVisibleRadius();
                    enableInfoWindow = radius_meter < 1000;

                    mapsPresenter.cameraPosChanged(mPreviousCameraPosition[0].target,radius_meter);
                    }
            }
        });

        //zoom to the current location
        final LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        assert service != null;
        final String provider = service.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = service.getLastKnownLocation(provider);
            if (location != null) {
                LatLng myLocation = new LatLng(location.getLatitude(),
                        location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                        14));
            }
        }
        }

    private float calcVisibleRadius() {
        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();

        LatLng farRight = visibleRegion.farRight; //top right
        LatLng farLeft = visibleRegion.farLeft; //top left
        LatLng nearRight = visibleRegion.nearRight; //bottom right
        LatLng nearLeft = visibleRegion.nearLeft; //bottom left

        float[] radiusWidth = new float[2];
        Location.distanceBetween(
                (farRight.latitude+nearRight.latitude)/2,
                (farRight.longitude+nearRight.longitude)/2,
                (farLeft.latitude+nearLeft.latitude)/2,
                (farLeft.longitude+nearLeft.longitude)/2,
                radiusWidth
        );


        float[] distanceHeight = new float[2];
        Location.distanceBetween(
                (farRight.latitude+nearRight.latitude)/2,
                (farRight.longitude+nearRight.longitude)/2,
                (farLeft.latitude+nearLeft.latitude)/2,
                (farLeft.longitude+nearLeft.longitude)/2,
                distanceHeight
        );

        float radius_meter;

        if (radiusWidth[0]>distanceHeight[0]){
            radius_meter = radiusWidth[0];
        } else {
            radius_meter = distanceHeight[0];
        }

        return radius_meter;
    }

    /**
     * Asks for permission of location services
     */
    public void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     * Callback for accepting/denying the location permission request
     */
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



    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Marker addMarker(MarkerOptions marker) {
        return mMap.addMarker(marker);
    }

    public void clearMap(){
        mMap.clear();
    }

    /**
     * Adds the heatmap to the map
     * @param options the TileOverlay options of the heatmap
     * @return the added TileOverlayy
     */
    public TileOverlay addTileOverlay(TileOverlayOptions options){
       return  mMap.addTileOverlay(options);
    }

    public void onClick(View view) {
        if(heatMapEnabled){
            heatMapEnabled=false;
            ((Button)view).setText("Heatmap");
        }else{
            heatMapEnabled=true;
            ((Button)view).setText("Markers");
        }
        clearMap();
        mapsPresenter.switchHeatmap(heatMapEnabled);
    }
}