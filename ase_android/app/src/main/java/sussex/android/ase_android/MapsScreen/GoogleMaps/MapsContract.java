package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.app.Activity;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import sussex.android.ase_android.BasePresenter;

public interface MapsContract {
    interface View {
        Activity getActivity();
        Marker addMarker(MarkerOptions marker);
    }

    interface Presenter extends BasePresenter {
        void initialize();

        void addMarkerToList(double lat, double lon, double price, String postcode);

        void displayMarkers();
    }

    interface Model{

    }
}
