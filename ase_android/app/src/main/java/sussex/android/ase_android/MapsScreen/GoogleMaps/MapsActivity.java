package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



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
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        enableMyLocation();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                bottomSheetView.collapseBottomSheet();
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
        mapsPresenter.initialize();
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



    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Marker addMarker(MarkerOptions marker) {
        return mMap.addMarker(marker);
    }
}