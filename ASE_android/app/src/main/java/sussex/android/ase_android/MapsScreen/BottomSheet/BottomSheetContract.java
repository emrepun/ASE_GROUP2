package sussex.android.ase_android.MapsScreen.BottomSheet;


import java.util.List;

import sussex.android.ase_android.BasePresenter;
import sussex.android.ase_android.MapsScreen.Model.AdressInfo;

public interface BottomSheetContract {
        interface View {
                void displayBottomSheet(String postcode, String average);
                void hideBottomSheet();


                void displayAddresses(List<AdressInfo> houseAddressInfo);
                void onResponseError(String errorMessage);
        }

        interface Presenter extends BasePresenter {

                void displayAddresses(String postcode);
        }
}
