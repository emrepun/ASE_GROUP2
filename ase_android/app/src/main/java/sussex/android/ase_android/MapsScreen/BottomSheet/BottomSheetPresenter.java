package sussex.android.ase_android.MapsScreen.BottomSheet;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsContract;
import sussex.android.ase_android.MapsScreen.model.CallbackInfoInterface;
import sussex.android.ase_android.MapsScreen.model.JSONparser;

public class BottomSheetPresenter implements  BottomSheetContract.Presenter, CallbackInfoInterface {
    BottomSheetContract.View view;
    MapsContract.Presenter mapsPresenter;
    JSONparser jsonParser;


    public BottomSheetPresenter(BottomSheetContract.View view, MapsContract.Presenter mapsPresenter){
    this.view=view;
        this.mapsPresenter=mapsPresenter;
        this.jsonParser=mapsPresenter.getJsonParser();
    }

    public void displayAddresses(String postcode){
        jsonParser.postcodeJsonParse(postcode, this);
    }

    public void displayInfo(String json, String price){
        view.displayAddresses(json, price);
    }

    public void populateListView(String json, String price){ view.populateListView(json, price);}
}
