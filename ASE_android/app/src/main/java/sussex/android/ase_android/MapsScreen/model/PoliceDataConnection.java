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



public class PoliceDataConnection implements MapsContract.Model {
    private Context context;
    private RequestQueue mQueue;


    /**
     * server URL of our backend database server
     */
    private String serverURL = "https://data.police.uk/api/crimes-street/all-crime";

    /**
     * @param context Context for creating the Volley request queue
     */
    public PoliceDataConnection(Context context){
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
        if(radius>1.7){
            Toast.makeText(context, "Crime data can only be shown up to a radius of 1 mile.", Toast.LENGTH_SHORT).show();
        }
        String url = serverURL+"?lat="+lat+"&lng="+lon;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<PostCodeMarker> markerArrayList= new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postcodeData = response.getJSONObject(i);
                                double lat;double lon;
                                try {
                                    JSONObject locObject = postcodeData.getJSONObject("location");
                                    lat = locObject.getDouble("latitude");
                                    lon = locObject.getDouble("longitude");
                                }catch (JSONException e){
                                    lat=lon=0;
                                }

                                String category;
                                try {
                                    category = postcodeData.getString("category");
                                } catch (JSONException e) {
                                    category = "Unknown Crime";
                                }


                                markerArrayList.add(new PostCodeMarker(lat,lon, 1.0, category, PostCodeMarker.POLICE_MARKER));

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
     * The bottom sheet is not needed for the police map
     */
    public void postcodeJsonParse(final CallbackInfoInterface callback, String postcode) {
        //do nothing
    }
}
