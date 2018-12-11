package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import sussex.android.ase_android.CustomInfoWindowAdapter;
import sussex.android.ase_android.MapsScreen.BottomSheet.BottomSheetContract;
import sussex.android.ase_android.MapsScreen.BottomSheet.BottomSheetView;
import sussex.android.ase_android.MapsScreen.model.PostCodeMarker;
import sussex.android.ase_android.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,MapsContract.View, CompoundButton.OnCheckedChangeListener {


    private GoogleMap mMap;
    final int LOCATION_PERMISSION_REQUEST_CODE = 1532;
    View bottomSheet;

    private MapsContract.Presenter mapsPresenter;
    private BottomSheetContract.View bottomSheetView;
    Switch switch1;
    Button button;

    boolean enableInfoWindow =true;

    private ClusterManager<PostCodeMarker> mClusterManager;

    private boolean crimeMapEnabled;
    private boolean heatMapEnabled;

    final CameraPosition[] mPreviousCameraPosition = {null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        crimeMapEnabled=false;
        heatMapEnabled=false;

        //create MapsPresenter
        mapsPresenter=new MapsPresenter(this);

        bottomSheet = findViewById(R.id.pullUp_bottom_sheet);
        bottomSheetView = new BottomSheetView(bottomSheet, mapsPresenter, this);
        switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(this);
        button = findViewById(R.id.button);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Sets up the map options and callbacks once the map is ready
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        enableMyLocation();

        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));

        mClusterManager = new ClusterManager<>(this, mMap);
        mClusterManager.setRenderer(new MarkerRenderer(this, mMap, mClusterManager));

        //set Listener for hiding and displaying the BottomSheet
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mClusterManager.onMarkerClick(marker);
                markerClicked(marker);
                return true;
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
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                updateCameraRegion();
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

    private void updateCameraRegion() {
        mClusterManager.onCameraIdle();
        CameraPosition position = mMap.getCameraPosition();
        if(mPreviousCameraPosition[0] == null || !mPreviousCameraPosition[0].equals(position)) {
            //position changed
            mPreviousCameraPosition[0] = mMap.getCameraPosition();

            float radius_meter = calcVisibleRadius();
            enableInfoWindow = radius_meter < 1000;

            mapsPresenter.cameraPosChanged(mPreviousCameraPosition[0].target,radius_meter);
        }
    }

    public void onClick(View view){
        if(crimeMapEnabled){
            crimeMapEnabled=false;
            ((Button)view).setText("price map");
        }else{
            crimeMapEnabled=true;
            ((Button)view).setText("crime map");
        }
        clearMap();
        mapsPresenter.switchDataSource(crimeMapEnabled);
        mPreviousCameraPosition[0] = null; //invalidate current camera position
        updateCameraRegion();
    }

    private void markerClicked(Marker marker) {
        if(marker!=null && marker.getTitle()!=null) {
            if (enableInfoWindow && marker.getSnippet() != null && !marker.getSnippet().isEmpty()) {
                //only display info window for markers where that information is available
                bottomSheetView.displayBottomSheet(marker.getTitle(), marker.getSnippet());
            }
            marker.showInfoWindow();
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
                (farLeft.latitude+nearLeft.latitude)/2,
                (farLeft.longitude+nearLeft.longitude)/2,
                (farRight.latitude+nearRight.latitude)/2,
                (farRight.longitude+nearRight.longitude)/2,
                radiusWidth
        );


        float[] radiusHeight = new float[2];
        Location.distanceBetween(
                (farLeft.latitude+farRight.latitude)/2,
                (farLeft.longitude+farRight.longitude)/2,
                (nearLeft.latitude+nearRight.latitude)/2,
                (nearLeft.longitude+nearRight.longitude)/2,
                radiusHeight
        );

        float radius_meter;

        if (radiusWidth[0]>radiusHeight[0]){
            radius_meter = radiusWidth[0];
        } else {
            radius_meter = radiusHeight[0];
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

    public void addMarkersClustered(List<PostCodeMarker> markerList){
                mClusterManager.addItems(markerList);
                mClusterManager.cluster();
    }
    @Override
    public Marker addMarker(MarkerOptions markerOptions) {
        Marker marker = mMap.addMarker(markerOptions);
        if(postcodeSearch.equals(marker.getTitle())){
            postcodeSearch="";
            markerClicked(marker);
        }
        return marker;
    }

    public void clearMap(){
        mMap.clear();
        mClusterManager.clearItems();
        mClusterManager.cluster();
    }

    /**
     * Adds the heatmap to the map
     * @param options the TileOverlay options of the heatmap
     * @return the added TileOverlay
     */
    public TileOverlay addTileOverlay(TileOverlayOptions options){
        return  mMap.addTileOverlay(options);
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(switch1.isChecked()){
            heatMapEnabled=true;
        }else{
            heatMapEnabled=false;
        }
        clearMap();
        mapsPresenter.switchHeatmap(heatMapEnabled);
    }


    private int PLACE_PICKER_REQUEST = 1;
    private String postcodeSearch="";
    public void search_onclick(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),
                        17));
                postcodeSearch=place.getName().toString();
            }
        }
    }
}