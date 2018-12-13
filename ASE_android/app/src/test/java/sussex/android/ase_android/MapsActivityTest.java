package sussex.android.ase_android;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsActivity;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class MapsActivityTest {
    MapsActivity activity;
    @Test
    public void setUp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        activity = Robolectric.buildActivity(MapsActivity.class, intent).create().get();
    }

    // further testing is not possible as Roboelectric does not support testing play services
    // as google maps
    // Integration tests (UI tests) have to be used for further tests

}