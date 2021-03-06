package sussex.android.ase_android.MapsScreen.Model;

import java.util.List;

/**
 * This is the Callback for requesting the markers for each postcode in the ServerConnection class
 */
public interface CallbackMarkerInterface {
    void displayMarkers(List<PostCodeMarker> markerList);

    void onResponseError(String errorMessage);
}