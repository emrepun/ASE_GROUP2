package sussex.android.ase_android.MapsScreen.BottomSheet;

import android.app.Activity;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsContract;
import sussex.android.ase_android.R;

public class BottomSheetView implements BottomSheetContract.View{
    private BottomSheetContract.Presenter sheetPresenter;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int peekHeight = 250;


    private Activity activity;
    public BottomSheetView(View bottomSheet, MapsContract.Presenter mapsPresenter, Activity activity){


        this.activity=activity;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setPeekHeight(peekHeight);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        sheetPresenter=new BottomSheetPresenter(this, mapsPresenter);

    }

    public int getPeekHeightPx() {
     return peekHeight;
    }

    @Override
    public void displayBottomSheet(String postcode, String average) {
        ((TextView)activity.findViewById(R.id.postcode_big)).setText(postcode);
        ((TextView)activity.findViewById(R.id.avgprice_big)).setText(average);
        sheetPresenter.displayAddresses(postcode);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void hideBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void displayAddresses(String json){
        ((TextView)activity.findViewById(R.id.houseList)).setText(json);
    }

    /**
     * Hides the on-screen soft keyboard
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
