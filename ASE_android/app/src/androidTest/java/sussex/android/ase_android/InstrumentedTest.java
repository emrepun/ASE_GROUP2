package sussex.android.ase_android;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Rule
    public ActivityTestRule<MapsActivity> mapsActivityActivityTestRule = new ActivityTestRule<>(MapsActivity.class);

    @Test
    public void clickMarker(){
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject marker = mDevice.findObject(new UiSelector().descriptionContains("BN1 9PP"));
        UiObject bottomsheet = mDevice.findObject(new UiSelector()
                .resourceId("sussex.android.ase_android:id/pullUp_bottom_sheet")
                .index(1)
                .packageName("sussex.android.ase_android"));
        try {
            marker.click();
            marker.clickTopLeft();
            Thread.sleep(1000);
            bottomsheet.swipeUp(10);
            Thread.sleep(5000);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
