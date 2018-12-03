package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import sussex.android.ase_android.MapsScreen.model.PostCodeMarker;
import sussex.android.ase_android.MapsScreen.model.ServerConnection;
import sussex.android.ase_android.R;

public class MapsPresenter implements MapsContract.Presenter, CallbackMarkerInterface {

    private MapsContract.View view;
    private MapsContract.Model jsonparser;
    private List<PostCodeMarker> markerList = new ArrayList<>();

    private boolean heatMapEnabled;

    public MapsPresenter(MapsContract.View view) {
        this.view = view;
        jsonparser=new ServerConnection(view.getActivity());
    }


    /**
     * @param markerList a list of PostCodeMarkers that should be saved and displayed
     */
    @Override
    public void displayMarkers(List<PostCodeMarker> markerList) {
        this.markerList=markerList;
        displayMarkers();
    }

    /**
     * Displays the saved markers in markerList if the heatmap is disabled
     */
    private void displayMarkers(){
        view.clearMap();
        if(heatMapEnabled){
            enableHeatMap();
        }else {
            for (PostCodeMarker postCodeMarker : markerList) {
                view.addMarker(new MarkerOptions()
                        .position(new LatLng(postCodeMarker.getLat(), postCodeMarker.getLon()))
                        .icon(bitmapDescriptorFromVector(view.getActivity(), R.drawable.ic_marker))
                        .title(postCodeMarker.getPostcode())
                        .snippet("Average price: £" + String.format(Locale.UK, "%,.2f", postCodeMarker.getPrice())));
            }
        }
    }


    /**
     * Generates a bitmap from the provided vector image
     * @param context Context
     * @param vectorResId id of the vector image
     * @return Bitmap representation of the vector image at the right resolution in respect to the device
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }


    public MapsContract.Model getServerConnectionHandler(){
        return jsonparser;
    }

    /**
     * Switches between the heatmap and marker display
     * @param showHeatmap true if heatmap should be displayed
     */
    public void switchHeatmap(boolean showHeatmap){
        this.heatMapEnabled=showHeatmap;
        if(showHeatmap){
            enableHeatMap();
        }else{
            displayMarkers();
        }
    }

    /**
     * Enables the heatmap with the saved data in markerList
     */
    private void enableHeatMap() {
        List <WeightedLatLng> list = new ArrayList<>();
        for (PostCodeMarker marker: markerList) {
            list.add(new WeightedLatLng(new LatLng(marker.getLat(), marker.getLon()), marker.getPrice()));
        }
        if(list.isEmpty()){
            Log.d("heamap","heatmap list is empty");
        }else {
            HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                    .weightedData(list)
                    .radius(50)
                    .build();
            TileOverlay OvermOverlay = view.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    /**
     * Gets called when the camera of the map changes (user/programmatically)
     * @param target the current/new camera position
     * @param radius_meter radius of the current visible region in meters
     */
    public void cameraPosChanged(LatLng target, float radius_meter) {
        jsonparser.markerJsonParse(this,target.latitude, target.longitude,radius_meter/1000);
    }
}
