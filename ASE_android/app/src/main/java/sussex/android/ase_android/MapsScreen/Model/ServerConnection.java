package sussex.android.ase_android.MapsScreen.Model;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

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
    private String serverURL = "https://ase-group2.herokuapp.com/api/";

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
        final String url = serverURL+"pcprices/"+lat+"/"+lon+"/"+radius;
        //final String url = "https://api.myjson.com/bins/siz6a";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<sussex.android.ase_android.MapsScreen.Model.PostCodeMarker> markerArrayList= new ArrayList<>();
                        try {
                            parseMarkerResponse(response, markerArrayList);
                            //pass markers back to calling object
                            callback.displayMarkers(markerArrayList);
                            try{
                                Crashlytics.log("Successful marker request for: " + url);
                            }catch (IllegalStateException e){
                                //fails only for unit tests
                            }
                        } catch (JSONException e) {
                            try{
                                Crashlytics.log("Failed (JSONException) marker request for: " + url);
                                Crashlytics.logException(e);
                            }catch (IllegalStateException ex){
                                //fails only for unit tests
                            }
                            callback.onResponseError("The backend server produced an error.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    Crashlytics.log("Failed (not reachable) marker request for: " + url);
                    Crashlytics.logException(error);
                }catch (IllegalStateException e){
                    //fails only for unit tests
                }
                callback.onResponseError("The backend server was not reachable.");
            }
        });
        //20 seconds timeout, because the backend can take multiple seconds to query the database
        int socketTimeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request.setTag("marker");
        mQueue.add(request);

    }

    private void parseMarkerResponse(JSONArray response, ArrayList<sussex.android.ase_android.MapsScreen.Model.PostCodeMarker> markerArrayList) throws JSONException {
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
            markerArrayList.add(new sussex.android.ase_android.MapsScreen.Model.PostCodeMarker(lat,lon, price, postcode, sussex.android.ase_android.MapsScreen.Model.PostCodeMarker.HOUSE_MARKER));

        }
    }

    /**
     * This method returns all information about the properties within the provided postcode
     * @param callback Callback for passing the properties to the caller
     * @param postcode Postcode
     */
    public void postcodeJsonParse(final CallbackInfoInterface callback, final String postcode) {
        mQueue.cancelAll("address");
        final String url = serverURL+"addresses/"+postcode.replaceAll(" ", "%20");
        //final String url = "https://api.myjson.com/bins/b9emq";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<AdressInfo> houseAddressInfo = new ArrayList<>();
                        try{
                            parsePostcodeResponse(response, houseAddressInfo);
                            callback.displayInfo(houseAddressInfo);
                            try{
                            Crashlytics.log("Successful postcode request for: " + url);
                            }catch (IllegalStateException e){
                                //fails only for unit tests
                            }
                        } catch (JSONException e) {
                            try{
                                Crashlytics.log("Failed (JSONException) postcode request for :" + url);
                                Crashlytics.logException(e);
                            }catch (IllegalStateException ex){
                                //fails only for unit tests
                            }
                            callback.onResponseError("The backend server produced an error.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    Crashlytics.log("Failed (not reachable) postcode request for: " + url);
                    Crashlytics.logException(error);
                }catch (IllegalStateException e){
                    //fails only for unit tests
                }
                callback.onResponseError("The backend server was not reachable.");
            }
        });
        //20 seconds timeout, because the backend can take multiple seconds to query the database
        int socketTimeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request.setTag("address");
        mQueue.add(request);

    }

    private void parsePostcodeResponse(JSONArray response, ArrayList<AdressInfo> houseAddressInfo) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            JSONObject postcodeData = response.getJSONObject(i);
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
            double pricePaid;
            String price;
            try {
                pricePaid = postcodeData.getDouble("pricePaid");
                price = "£"+String.format(Locale.UK, "%,.2f", pricePaid);

            } catch (JSONException e) {
                price= "unknown";
            }
            String transactionDate;
            try {
                transactionDate = postcodeData.getString("transactionDate");
            }catch (JSONException e) {
                transactionDate="Unknown";
            }
            houseAddressInfo.add(new AdressInfo(address, price, transactionDate));

        }
    }
}
