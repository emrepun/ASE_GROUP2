package sussex.android.ase_android.MapsScreen.model;

/**
 * This is the Callback for requesting the addresses and properties for one postcode in the ServerConnection class
 */
public interface CallbackInfoInterface {
    void displayInfo(String json, String price);
}