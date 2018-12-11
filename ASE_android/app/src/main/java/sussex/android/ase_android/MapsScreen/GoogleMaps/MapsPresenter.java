package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.List;

import sussex.android.ase_android.MapsScreen.model.CallbackMarkerInterface;
import sussex.android.ase_android.MapsScreen.model.PoliceDataConnection;
import sussex.android.ase_android.MapsScreen.model.PostCodeMarker;
import sussex.android.ase_android.MapsScreen.model.ServerConnection;

public class MapsPresenter implements MapsContract.Presenter, CallbackMarkerInterface {

    private MapsContract.View view;
    private MapsContract.Model ServerConnectionHandler;
    private List<PostCodeMarker> markerList = new ArrayList<>();

    private boolean heatMapEnabled;



    public MapsPresenter(MapsContract.View view) {
        this.view = view;
        ServerConnectionHandler =new ServerConnection(view.getActivity());

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
     * displays an error message when the backend produces an error
     * @param errorMessage message to display
     */
    @Override
    public void onResponseError(String errorMessage) {
        new AlertDialog.Builder(view.getActivity())
                .setTitle("Error")
                .setMessage(errorMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Displays the saved markers in markerList if the heatmap is disabled
     */
    private void displayMarkers(){
        view.clearMap();
        if(heatMapEnabled){
            enableHeatMap();
        }else {
            view.addMarkersClustered(markerList);
        }
    }


    public MapsContract.Model getServerConnectionHandler(){
        return ServerConnectionHandler;
    }

    public void setServerConnectionHandler(MapsContract.Model serverConnectionHandler){
        this.ServerConnectionHandler=serverConnectionHandler;
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
        ServerConnectionHandler.markerJsonParse(this,target.latitude, target.longitude,radius_meter/1000);
    }
}
