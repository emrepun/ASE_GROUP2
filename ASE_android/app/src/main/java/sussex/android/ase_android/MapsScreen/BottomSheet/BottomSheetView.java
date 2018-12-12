package sussex.android.ase_android.MapsScreen.BottomSheet;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsContract;
import sussex.android.ase_android.MapsScreen.model.AdressInfo;
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

    public void displayAddresses(List<AdressInfo> houseAddressInfo){
        ArrayAdapter<AdressInfo> adapter = new CustomAdapter(activity, R.layout.layout_item_ist, houseAddressInfo);
        ListView listView = activity.findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    private class CustomAdapter extends ArrayAdapter<AdressInfo> {
        private Context mcontext;
        int mResource;
        public CustomAdapter(@NonNull Context context, int resource, @NonNull List<AdressInfo> houseAddressInfo) {
            super(context, resource, houseAddressInfo);
            mcontext = context;
            mResource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String address = getItem(position).getAddress();
            String price = getItem(position).getPrice();
            String date = getItem(position).getDate();

            AdressInfo houseAddress = new AdressInfo(address, price, date);

            LayoutInflater inflater = LayoutInflater.from(mcontext);
            convertView = inflater.inflate(mResource, parent, false);

            TextView tvAddress = (TextView) convertView.findViewById(R.id.addressList);
            TextView tvPrice = (TextView) convertView.findViewById(R.id.priceList);
            TextView tvDate = (TextView) convertView.findViewById(R.id.dateList);

            tvAddress.setText(address);
            tvPrice.setText(price);
            tvDate.setText(date);
            return convertView;
        }
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
