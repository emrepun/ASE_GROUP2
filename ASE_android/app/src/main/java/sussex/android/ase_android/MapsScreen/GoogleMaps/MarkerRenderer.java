package sussex.android.ase_android.MapsScreen.GoogleMaps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.Locale;

import sussex.android.ase_android.MapsScreen.model.PostCodeMarker;
import sussex.android.ase_android.R;

public class MarkerRenderer extends DefaultClusterRenderer<PostCodeMarker> {
    private Context context;

    private BitmapDescriptor policeIcon;
    private BitmapDescriptor houseIcon;

    public MarkerRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        this.context=context;
        houseIcon=bitmapDescriptorFromVector(R.drawable.ic_marker);
        policeIcon=bitmapDescriptorFromVector(R.drawable.ic_marker_police);
    }

    @Override
    protected void onBeforeClusterItemRendered(PostCodeMarker postCodeMarker, MarkerOptions markerOptions) {
        if(postCodeMarker.getType()==PostCodeMarker.HOUSE_MARKER){
            markerOptions.icon(houseIcon);
        }else{
            markerOptions.icon(policeIcon);
        }
    }

    /**
     * Generates a bitmap from the provided vector image
     * @param vectorResId id of the vector image
     * @return Bitmap representation of the vector image at the right resolution in respect to the device
     */
    private BitmapDescriptor bitmapDescriptorFromVector(int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }
}
