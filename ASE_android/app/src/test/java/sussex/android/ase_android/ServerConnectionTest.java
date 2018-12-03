package sussex.android.ase_android;

import android.content.Context;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.test.core.app.ApplicationProvider;
import sussex.android.ase_android.MapsScreen.model.CallbackInfoInterface;
import sussex.android.ase_android.MapsScreen.model.CallbackMarkerInterface;
import sussex.android.ase_android.MapsScreen.model.PostCodeMarker;
import sussex.android.ase_android.MapsScreen.model.ServerConnection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
public class ServerConnectionTest {
    private Context context = ApplicationProvider.getApplicationContext();
    private ServerConnection scon;

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
        scon = new ServerConnection(context);
        RequestQueue queue = newVolleyRequestQueueForTest(context);
        Field privateQueue = null;
        try {
            privateQueue = ServerConnection.class.getDeclaredField("mQueue");
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
        });
        scon.markerJsonParse(callBack, 50.822823, -0.131921,0.1);
        lock.await(10000, TimeUnit.MILLISECONDS);
        verify(callBack, timeout(5000).times(1)).displayMarkers((List<PostCodeMarker>)any());
    }



    @Test
    public void markerJsonParse() throws InterruptedException {
        scon.markerJsonParse(new CallbackMarkerInterface() {
            @Override
            public void displayMarkers(List<PostCodeMarker> markerList) {
                assertEquals(markerList.size(), 15);
                assertEquals(markerList.get(0).getPostcode(), "BN2 0JH");
                lock.countDown();
            }
        }, 50.822823, -0.131921,0.1);
        lock.await(10000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void postcodeJsonParse() throws InterruptedException {
        scon.postcodeJsonParse(new CallbackInfoInterface() {
            @Override
            public void displayInfo(String json, String price) {
                assertEquals(price, 14);
                assertEquals(json.substring(1,10), "BN2 0JH");
                lock.countDown();
            }
        }, "BN2 0JH");
        lock.await(10000, TimeUnit.MILLISECONDS);
    }
}