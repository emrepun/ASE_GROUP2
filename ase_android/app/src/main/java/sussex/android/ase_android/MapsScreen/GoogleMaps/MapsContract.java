package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.app.Activity;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import sussex.android.ase_android.BasePresenter;
import sussex.android.ase_android.MapsScreen.model.JSONparser;

public interface MapsContract {
    interface View {
        Activity getActivity();
        Marker addMarker(MarkerOptions marker);
        LatLng getViewLoc();
        void clearMap();
    }

    interface Presenter extends BasePresenter {
        void initialize();

        void addMarkerToList(double lat, double lon, double price, String postcode);

        void displayMarkers();
        void cameraPosChanged(CameraPosition cameraPosition);

        JSONparser getJsonParser();
    }

    interface Model{

    }
}
