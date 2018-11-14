package sussex.android.ase_android.MapsScreen.model;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sussex.android.ase_android.MapsScreen.BottomSheet.BottomSheetContract;
import sussex.android.ase_android.MapsScreen.BottomSheet.BottomSheetPresenter;
import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsContract;

public class JSONparser {
    private Context context;
    private RequestQueue mQueue;

    public  JSONparser(Context context){
        this.context=context;
        mQueue=Volley.newRequestQueue(context);
    }

    public void markerJsonParse(final MapsContract.Presenter presenter, double lat, double lon) {
        mQueue.cancelAll("marker");
        String url = "https://d42fd882.ngrok.io/api/pcprices/"+lat+"/"+lon+"/0.1";
        //String url = "https://www.dropbox.com/s/5us526t69r99irl/test.json?dl=1";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postcodeData = response.getJSONObject(i);

                                double price = postcodeData.getDouble("price");
                                String postcode = postcodeData.getString("postcode");
                                double lat = postcodeData.getDouble("latitude");
                                double lon = postcodeData.getDouble("longitude");
                                presenter.addMarkerToList(lat,lon, price, postcode);
                            }
                            presenter.displayMarkers();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request.setTag("marker");
        mQueue.add(request);

    }

    public void postcodeJsonParse(String postcode, final BottomSheetContract.Presenter sheetPresenter) {
        mQueue.cancelAll("address");
        String url = "https://d42fd882.ngrok.io/api/addresses/"+postcode;
        RequestQueue mQueue = Volley.newRequestQueue(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String json="";
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postcodeData = response.getJSONObject(i);
                                double pricePaid = postcodeData.getDouble("pricePaid");

                                JSONObject propAddrObject = postcodeData.getJSONObject("propertyAddress");
                                String town = propAddrObject.getString("town");
                                String address = propAddrObject.getString("paon")+" "
                                        + propAddrObject.getString("street");

                                json=json+address+"\n"+pricePaid+"\n";


                            }
                            sheetPresenter.displayInfo(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request.setTag("address");
        mQueue.add(request);

    }
}
