package sussex.android.ase_android.MapsScreen.BottomSheet;

import java.util.List;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsContract;
import sussex.android.ase_android.MapsScreen.Model.AdressInfo;
import sussex.android.ase_android.MapsScreen.Model.CallbackInfoInterface;

public class BottomSheetPresenter implements  BottomSheetContract.Presenter, CallbackInfoInterface {
    BottomSheetContract.View view;
    MapsContract.Presenter mapsPresenter;


    public BottomSheetPresenter(BottomSheetContract.View view, MapsContract.Presenter mapsPresenter){
    this.view=view;
        this.mapsPresenter=mapsPresenter;
    }

    public void displayAddresses(String postcode){
        mapsPresenter.getServerConnectionHandler().postcodeJsonParse(this,postcode);
    }

    public void displayInfo(List<AdressInfo> houseAddressInfo){
        view.displayAddresses(houseAddressInfo);
    }

    @Override
    public void onResponseError(String errorMessage) {
        view.onResponseError(errorMessage);
    }
}
