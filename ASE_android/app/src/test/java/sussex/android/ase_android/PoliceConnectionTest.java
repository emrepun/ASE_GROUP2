package sussex.android.ase_android;

import android.content.Context;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.google.android.gms.maps.GoogleMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.test.core.app.ApplicationProvider;
import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsContract;
import sussex.android.ase_android.MapsScreen.Model.CallbackMarkerInterface;
import sussex.android.ase_android.MapsScreen.Model.PoliceDataConnection;
import sussex.android.ase_android.MapsScreen.Model.PostCodeMarker;
import sussex.android.ase_android.MapsScreen.Model.ServerConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
public class PoliceConnectionTest {
    private Context context = ApplicationProvider.getApplicationContext();
    private MapsContract.Model scon;

    private CountDownLatch lock = new CountDownLatch(1);

    //Volley needs specific queue for testing
    private RequestQueue newVolleyRequestQueueForTest(final Context context) {
        File cacheDir = new File(context.getCacheDir(), "cache/volley");
        Network network = new BasicNetwork(new HurlStack());
        ResponseDelivery responseDelivery = new ExecutorDelivery(Executors.newSingleThreadExecutor());
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network, 4, responseDelivery);
        queue.start();
        return queue;
    }

    @Before
    public void setup() {
        scon = new PoliceDataConnection(context);
        RequestQueue queue = newVolleyRequestQueueForTest(context);
        Field privateQueue = null;
        try {
            privateQueue = PoliceDataConnection.class.getDeclaredField("mQueue");
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        privateQueue.setAccessible(true);
        try {
            privateQueue.set(scon,queue);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void callbackIsCalled() throws InterruptedException {
        final CallbackMarkerInterface callBack = spy(new CallbackMarkerInterface() {
            @Override
            public void displayMarkers(List<PostCodeMarker> markerList) {
                lock.countDown();
            }

            @Override
            public void onResponseError(String errorMessage) {
                fail(errorMessage);
            }
        });
        scon.markerJsonParse(callBack, 50.822823, -0.131921,0.1);
        //lock.await(10000, TimeUnit.MILLISECONDS); not needed verify has timeout
        verify(callBack, timeout(5000).times(1)).displayMarkers((List<PostCodeMarker>)any());
    }



    @Test
    public void markerJsonParse() throws InterruptedException {
        final List<PostCodeMarker> markerList_result = new ArrayList<>();
        scon.markerJsonParse(new CallbackMarkerInterface() {
            @Override
            public void displayMarkers(List<PostCodeMarker> markerList) {
                markerList_result.addAll(markerList);
                lock.countDown();
            }

            @Override
            public void onResponseError(String errorMessage) {
                fail(errorMessage);
            }
        }, 50.822823, -0.131921,0.1);
        lock.await(5000, TimeUnit.MILLISECONDS);
        assertTrue(markerList_result.size()> 0);
    }

}