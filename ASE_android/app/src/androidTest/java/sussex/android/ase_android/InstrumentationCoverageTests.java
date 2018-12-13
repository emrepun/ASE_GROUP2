package sussex.android.ase_android;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;
import android.view.WindowManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsActivity;
import sussex.android.ase_android.MapsScreen.Model.PostCodeMarker;
import sussex.android.ase_android.MapsScreen.Model.ServerConnection;


@RunWith(AndroidJUnit4.class)
public class InstrumentationCoverageTests {
    @Before
    public void unlockScreenAndInitMap() throws InterruptedException {
        final MapsActivity activity = mapsActivityActivityTestRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);

        Thread.sleep(5000);

        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.moveCamera(new LatLng(50.837635, -0.124675),
                        18);
            }
        });


    }

    @Rule
    public ActivityTestRule<MapsActivity> mapsActivityActivityTestRule = new ActivityTestRule<>(MapsActivity.class);


    @Test
    public void testActivity(){
        MapsActivity activity = mapsActivityActivityTestRule.getActivity();

        Field mClusterManager = null;
        try {
            mClusterManager = MapsActivity.class.getDeclaredField("mClusterManager");
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        mClusterManager.setAccessible(true);
        try {
            ClusterManager<PostCodeMarker> cManager = (ClusterManager<PostCodeMarker>)mClusterManager.get(activity);
            assert(cManager.getMarkerCollection().getMarkers().size()>0);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }



    }
}
