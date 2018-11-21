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
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sussex.android.ase_android.MapsScreen.model.CallbackMarkerInterface;
import sussex.android.ase_android.MapsScreen.model.JSONparser;
import sussex.android.ase_android.MapsScreen.model.ZipCodeMarker;
import sussex.android.ase_android.R;

public class MapsPresenter implements MapsContract.Presenter, CallbackMarkerInterface {

    private MapsContract.View view;
    private MapsContract.Model jsonparser;
    private List<ZipCodeMarker> markerList = new ArrayList<>();

    private boolean heatMapEnabled;

    public MapsPresenter(MapsContract.View view) {
        this.view = view;
        jsonparser=new JSONparser(view.getActivity());
    }




    @Override
    public void displayMarkers(List<ZipCodeMarker> markerList) {
        this.markerList=markerList;
        displayMarkers();
    }

    private void displayMarkers(){
        view.clearMap();
        if(heatMapEnabled){
            enableHeatMap();
        }else {
            for (ZipCodeMarker zipCodeMarker : markerList) {
                view.addMarker(new MarkerOptions()
                        .position(new LatLng(zipCodeMarker.getLat(), zipCodeMarker.getLon()))
                        .icon(bitmapDescriptorFromVector(view.getActivity(), R.drawable.ic_marker))
                        .title(zipCodeMarker.getPostcode())
                        .snippet("Average price: £" + String.format(Locale.UK, "%,.2f", zipCodeMarker.getPrice())));
            }
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
        //TODO: calculate radius according to Radius Of Visible Map in Android
            jsonparser.markerJsonParse(this, cameraPosition.target.latitude, cameraPosition.target.longitude,0.5);
    }

    public MapsContract.Model getJsonParser(){
        return jsonparser;
    }

    public void switchHeatmap(boolean showHeatmap){
        this.heatMapEnabled=showHeatmap;
        if(showHeatmap){
            enableHeatMap();
        }else{
            displayMarkers();
        }
    }

    private void enableHeatMap() {
        List <WeightedLatLng> list = new ArrayList<>();
        for (ZipCodeMarker marker: markerList) {
            list.add(new WeightedLatLng(new LatLng(marker.getLat(), marker.getLon()), marker.getPrice()));
        }
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .weightedData(list)
                .build();
        TileOverlay OvermOverlay = view.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }
}
