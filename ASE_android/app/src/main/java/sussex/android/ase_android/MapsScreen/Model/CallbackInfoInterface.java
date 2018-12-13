package sussex.android.ase_android.MapsScreen.Model;

import java.util.List;

import sussex.android.ase_android.MapsScreen.Model.AdressInfo;

/**
 * This is the Callback for requesting the addresses and properties for one postcode in the ServerConnection class
 */
public interface CallbackInfoInterface {
    void displayInfo(List<AdressInfo> houseAddressInfo);

    void onResponseError(String errorMessage);

}