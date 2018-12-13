package sussex.android.ase_android;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.internal.deps.guava.collect.Maps;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsActivity;


@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    

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

        Thread.sleep(120000);

        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.moveCamera(new LatLng(50.837635, -0.124675),
                        18);
            }
        });


    }

    @Rule
    public ActivityTestRule<MapsActivity> mapsActivityActivityTestRule = new ActivityTestRule<>(MapsActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    //perform task
    @LargeTest
    @Test
    public void ActivityTest() throws UiObjectNotFoundException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = mDevice.findObject(new UiSelector().descriptionContains("BN2 3QA"));
        UiObject bottomsheet = mDevice.findObject(new UiSelector()
                .resourceId("sussex.android.ase_android:id/pullUp_bottom_sheet")
                .packageName("sussex.android.ase_android"));
        UiObject toggle = mDevice.findObject(new UiSelector()
                .resourceId("sussex.android.ase_android:id/switch1")
                .packageName("sussex.android.ase_android"));

        toggle.click();
        Thread.sleep(3000);
        toggle.click();
        marker.click();
        marker.clickTopLeft();
        Thread.sleep(1000);
        bottomsheet.swipeUp(10);
        Thread.sleep(5000);
        bottomsheet.swipeDown(10);
        Thread.sleep(5000);
    }
}
