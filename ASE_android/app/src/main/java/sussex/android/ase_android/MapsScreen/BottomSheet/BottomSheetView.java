package sussex.android.ase_android.MapsScreen.BottomSheet;

import android.app.Activity;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsContract;
import sussex.android.ase_android.R;

public class BottomSheetView implements BottomSheetContract.View{
    private BottomSheetContract.Presenter sheetPresenter;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int peekHeight = 400;


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

    public void displayAddresses(String json, String price, String date){
        ((TextView)activity.findViewById(R.id.addressList)).setText(json);
        ((TextView)activity.findViewById(R.id.priceList)).setText(price);
        ((TextView)activity.findViewById(R.id.dateList)).setText(date);

        /*ListView listView = activity.findViewById(R.id.listView);

        HashMap<String, String[]> addressInfo = new HashMap<>();
        String[] purchaseData = new String[]{price, date};
        addressInfo.put(json, purchaseData);

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(activity, listItems, R.layout.layout_item_ist,
                new String[]{"address", purchaseData[0], purchaseData[1]}, new int[]{R.id.addressList, R.id.priceList, R.id.dateList});

        Iterator iterator = addressInfo.entrySet().iterator();
        while (iterator.hasNext()){
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)iterator.next();
            resultsMap.put("address", pair.getKey().toString());
            resultsMap.put(purchaseData[0], pair.getValue().toString());
            resultsMap.put(purchaseData[1], pair.getValue().toString());
            listItems.add(resultsMap);
        }

        listView.setAdapter(adapter);*/
    }

    public void populateListView(String json, String price, String date){

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
