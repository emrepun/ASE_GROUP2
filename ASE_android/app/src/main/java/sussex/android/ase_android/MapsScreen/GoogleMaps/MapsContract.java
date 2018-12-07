package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.List;

import sussex.android.ase_android.BasePresenter;
import sussex.android.ase_android.MapsScreen.model.CallbackInfoInterface;
import sussex.android.ase_android.MapsScreen.model.CallbackMarkerInterface;
import sussex.android.ase_android.MapsScreen.model.PostCodeMarker;

public interface MapsContract {
    interface View {
        Activity getActivity();
        void addMarkersClustered(List<PostCodeMarker> marker);
        Marker addMarker(MarkerOptions marker);
        TileOverlay addTileOverlay(TileOverlayOptions options);
        void clearMap();

    }

    interface Presenter extends BasePresenter {
        void cameraPosChanged(LatLng target, float radius_meter);
        void switchHeatmap(boolean showHeatmap);
        Model getServerConnectionHandler();
    }

    interface Model{
        void markerJsonParse(final CallbackMarkerInterface callback, double lat, double lon, double radius);
        void postcodeJsonParse(final CallbackInfoInterface callback, String postcode);
    }
}
