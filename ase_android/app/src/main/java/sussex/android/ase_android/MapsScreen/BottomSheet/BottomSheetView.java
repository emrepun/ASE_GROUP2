package sussex.android.ase_android.MapsScreen.BottomSheet;

import android.app.Activity;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsContract;

public class BottomSheetView implements BottomSheetContract.View{
    private BottomSheetBehavior mBottomSheetBehavior;
    private int peekHeight = 250;


    Activity activity;
    public BottomSheetView(View bottomSheet, MapsContract.Presenter mapsPresenter, Activity activity){


        this.activity=activity;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setPeekHeight(peekHeight);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);



    }

    public int getPeekHeightPx() {
     return peekHeight;
    }

    @Override
    public void collapseBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void hideBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
