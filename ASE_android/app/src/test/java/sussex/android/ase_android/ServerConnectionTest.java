package sussex.android.ase_android;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;
import sussex.android.ase_android.MapsScreen.model.CallbackInfoInterface;
import sussex.android.ase_android.MapsScreen.model.CallbackMarkerInterface;
import sussex.android.ase_android.MapsScreen.model.PostCodeMarker;
import sussex.android.ase_android.MapsScreen.model.ServerConnection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
public class ServerConnectionTest {
    private Context context = ApplicationProvider.getApplicationContext();


    @Before
    public void setup() {

    }

    @Test
    public void callbackIsCalled()
    {
        final CallbackMarkerInterface callBack = mock(CallbackMarkerInterface.class);
        ServerConnection scon = new ServerConnection(context);
        scon.markerJsonParse(callBack, 50.822823, -0.131921,0.1);

        verify(callBack, timeout(5000).times(1)).displayMarkers((List<PostCodeMarker>)any());
    }

    @Test(timeout = 5000)
    public void markerJsonParse() {
        ServerConnection scon = new ServerConnection(context);
        scon.markerJsonParse(new CallbackMarkerInterface() {
            @Override
            public void displayMarkers(List<PostCodeMarker> markerList) {
                assertEquals(markerList.size(), 14);
                assertEquals(markerList.get(0).getPostcode(), "BN2 0JH");
            }
        }, 50.822823, -0.131921,0.1);
    }

    @Test(timeout = 5000)
    public void postcodeJsonParse() {
        ServerConnection scon = new ServerConnection(context);
        scon.postcodeJsonParse(new CallbackInfoInterface() {
            @Override
            public void displayInfo(String json, String price) {
                //assertEquals(markerList.size(), 14);
                //assertEquals(markerList.get(0).getPostcode(), "BN2 0JH");
            }
        }, "BN2 0JH");
    }
}