package sussex.android.ase_android.MapsScreen.BottomSheet;


import sussex.android.ase_android.BasePresenter;

public interface BottomSheetContract {
        interface View {
                int getPeekHeightPx();
                void displayBottomSheet(String postcode, String average);
                void hideBottomSheet();

                void displayAddresses(String json, String price);

                void populateListView(String json, String price);
                void onResponseError(String errorMessage);
        }

        interface Presenter extends BasePresenter {

                void displayAddresses(String postcode);
        }
}
