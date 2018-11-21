package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.app.Activity;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import sussex.android.ase_android.BasePresenter;
import sussex.android.ase_android.MapsScreen.model.CallbackInfoInterface;
import sussex.android.ase_android.MapsScreen.model.CallbackMarkerInterface;
import sussex.android.ase_android.MapsScreen.model.JSONparser;

public interface MapsContract {
    interface View {
        Activity getActivity();
        Marker addMarker(MarkerOptions marker);
        TileOverlay addTileOverlay(TileOverlayOptions options);
        void clearMap();
    }

    interface Presenter extends BasePresenter {
        void cameraPosChanged(CameraPosition cameraPosition);
        void switchHeatmap(boolean showHeatmap);
        Model getJsonParser();
    }

    interface Model{
        void markerJsonParse(final CallbackMarkerInterface callback, double lat, double lon, double radius);
        void postcodeJsonParse(final CallbackInfoInterface callback, String postcode);
    }
}
