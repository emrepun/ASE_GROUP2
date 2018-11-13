package sussex.android.ase_android.MapsScreen.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsContract;

public class JSONparser {
    private Context context;
    MapsContract.Presenter presenter;
    public  JSONparser(Context context, MapsContract.Presenter presenter){
        this.context=context;
        this.presenter=presenter;
    }

    public void jsonParse() {

        //String url = "https://a5a03ae7.ngrok.io/api/pcprices/50.827930/-0.168749/0.1";
        String url = "https://www.dropbox.com/s/5us526t69r99irl/test.json?dl=1";
        RequestQueue mQueue = Volley.newRequestQueue(context);

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

        mQueue.add(request);

    }
}
