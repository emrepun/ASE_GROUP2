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

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsContract;

public class JSONparser implements MapsContract.Model {
    private Context context;
    private RequestQueue mQueue;
    private String serverURL = "https://ab6a5144.ngrok.io/api/";

    public  JSONparser(Context context){
        this.context=context;
        mQueue=Volley.newRequestQueue(context);
    }

    public void markerJsonParse(final CallbackMarkerInterface callback, double lat, double lon, double radius) {
        mQueue.cancelAll("marker");
        String url = serverURL+"pcprices/"+lat+"/"+lon+"/"+radius;
        //String url = "https://www.dropbox.com/s/5us526t69r99irl/test.json?dl=1"; //temp json
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<ZipCodeMarker> markerArrayList= new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postcodeData = response.getJSONObject(i);
                                double price;
                                try {
                                    price = postcodeData.getDouble("price");
                                }catch (JSONException e){
                                    price=0;
                                }
                                String postcode;
                                try{
                                    postcode = postcodeData.getString("postcode");
                                }catch (JSONException e){
                                    postcode="Unknown";
                                }
                                double lat;double lon;
                                try {
                                    lat = postcodeData.getDouble("latitude");
                                    lon = postcodeData.getDouble("longitude");
                                }catch (JSONException e){
                                    lat=lon=0;
                                }
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
        String url = serverURL+"addresses/"+postcode;
        //String url = "https://api.myjson.com/bins/b9emq"; //temp json in case the link is down
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
