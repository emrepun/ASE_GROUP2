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

import java.util.ArrayList;
import java.util.Locale;

import sussex.android.ase_android.MapsScreen.BottomSheet.BottomSheetContract;

public class JSONparser {
    private Context context;
    private RequestQueue mQueue;

    public  JSONparser(Context context){
        this.context=context;
        mQueue=Volley.newRequestQueue(context);
    }

    public void markerJsonParse(final CallbackMarkerInterface callback, double lat, double lon) {
        mQueue.cancelAll("marker");
        /*String url = "https://fa5eff00.ngrok.io/api/pcprices/"+lat+"/"+lon+"/0.1";*/
        String url = "https://api.myjson.com/bins/siz6a"; //temp json
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<ZipCodeMarker> markerArrayList= new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postcodeData = response.getJSONObject(i);

                                double price = postcodeData.getDouble("price");
                                String postcode = postcodeData.getString("postcode");
                                double lat = postcodeData.getDouble("latitude");
                                double lon = postcodeData.getDouble("longitude");
                                markerArrayList.add(new ZipCodeMarker(lat,lon, price, postcode));
                            }
                            callback.displayMarkers(markerArrayList);
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

    public void postcodeJsonParse(final CallbackInfoInterface callback, String postcode) {
        mQueue.cancelAll("address");
        /*String url = "https://fa5eff00.ngrok.io/api/addresses/"+postcode;*/
        String url = "https://api.myjson.com/bins/b9emq"; //temp json in case the link is down
        RequestQueue mQueue = Volley.newRequestQueue(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String json="";
                            String price = "";
                            String houseAddress="";
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postcodeData = response.getJSONObject(i);
                                double pricePaid = postcodeData.getDouble("pricePaid");

                                JSONObject propAddrObject = postcodeData.getJSONObject("propertyAddress");
                                String town = propAddrObject.getString("town");
                                String address = propAddrObject.getString("paon")+" "
                                        + propAddrObject.getString("street");

                                json=json+address+"\n";
                                price=price+"£" + String.format(Locale.UK,"%,.2f", pricePaid)+"\n";

                                /*json=json+address+"\n"+pricePaid+"\n";*/


                            }
                            callback.displayInfo(json, price);
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
