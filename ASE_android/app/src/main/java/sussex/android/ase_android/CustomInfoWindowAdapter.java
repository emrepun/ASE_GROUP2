package sussex.android.ase_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        this.mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.layout_info_window, null);
    }

    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTitle = ((TextView) view.findViewById(R.id.infoWindow_title));
        if (title!=null && !title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = ((TextView) view.findViewById(R.id.infoWindow_snippet));
        if (snippet!=null && !snippet.isEmpty()){
            tvSnippet.setText(snippet);
        }
    }
    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
