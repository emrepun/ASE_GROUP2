package sussex.android.ase_android.MapsScreen.BottomSheet;


import java.util.List;

import sussex.android.ase_android.BasePresenter;
import sussex.android.ase_android.MapsScreen.model.AdressInfo;

public interface BottomSheetContract {
        interface View {
                int getPeekHeightPx();
                void displayBottomSheet(String postcode, String average);
                void hideBottomSheet();

                void displayAddresses(List<AdressInfo> houseAddressInfo);

                void populateListView(String json, String price, String date);
        }

        interface Presenter extends BasePresenter {

                void displayAddresses(String postcode);
        }
}
