package sussex.android.ase_android.MapsScreen.model;

import android.content.Context;
import android.widget.Toast;

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

public class ServerConnection implements MapsContract.Model {
    private Context context;
    private RequestQueue mQueue;


    /**
     * server URL of our backend database server
     */
    private String serverURL = "https://ab6a5144.ngrok.io/api/";

    /**
     * @param context Context for creating the Volley request queue
     */
    public ServerConnection(Context context){
        this.context=context;
        mQueue=Volley.newRequestQueue(context);
    }

    /**
     * This method returns all information about postcodes in the area around the provided lat/lon
     * that are within the radius
     * @param callback Callback for passing the postCodeMarker object
     * @param lat Latitude for the current request
     * @param lon Longitude for the current request
     * @param radius Radius in KM for the current request
     */
    public void markerJsonParse(final CallbackMarkerInterface callback, double lat, double lon, double radius) {
        //cancel all other requests to the backend as they are now outdated
        mQueue.cancelAll("marker");
        String url = serverURL+"pcprices/"+lat+"/"+lon+"/"+radius;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<PostCodeMarker> markerArrayList= new ArrayList<>();
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
                                markerArrayList.add(new PostCodeMarker(lat,lon, price, postcode));

                            }
                            //pass markers back to calling object
                            callback.displayMarkers(markerArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "The backend server was not reachable or produced an error", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        //30 seconds timeout, because the backend can take multiple seconds to query the database
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request.setTag("marker");
        mQueue.add(request);

    }

    /**
     * This method returns all information about the properties within the provided postcode
     * @param callback Callback for passing the properties to the caller
     * @param postcode Postcode
     */
    public void postcodeJsonParse(final CallbackInfoInterface callback, String postcode) {
        mQueue.cancelAll("address");
        String url = serverURL+"addresses/"+postcode;
        //String url = "https://api.myjson.com/bins/b9emq"; //temp json in case the link is down
        RequestQueue mQueue = Volley.newRequestQueue(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            String json="";
                            String price = "";
                            String houseAddress="";
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postcodeData = response.getJSONObject(i);
                                double pricePaid;
                                try {
                                    pricePaid = postcodeData.getDouble("pricePaid");
                                } catch (JSONException e) {
                                    pricePaid=0;
                                }
                                String town;
                                String address;
                                try {
                                    JSONObject propAddrObject = postcodeData.getJSONObject("propertyAddress");
                                    town = propAddrObject.getString("town");
                                    address = propAddrObject.getString("paon") + " "
                                            + propAddrObject.getString("street");
                                }catch (JSONException e) {
                                    town="Unknown";
                                    address="Unknown";
                                }
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
                Toast.makeText(context, "The backend server was not reachable or produced an error", Toast.LENGTH_LONG).show();
            }
        });
        //30 seconds timeout, because the backend can take multiple seconds to query the database
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request.setTag("address");
        mQueue.add(request);

    }
}
