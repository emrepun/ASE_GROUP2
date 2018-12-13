package sussex.android.ase_android;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import sussex.android.ase_android.MapsScreen.GoogleMaps.MapsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AssertionTest {

    @Rule
    public ActivityTestRule<MapsActivity> mapsActivityActivityTestRule = new ActivityTestRule<>(MapsActivity.class);



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

        Thread.sleep(8000);
    }


    //Check if object is displayed
    @SmallTest
    @Test
    public void assertSwitch() {
        ViewInteraction switch_ = onView(
                Matchers.allOf(withId(R.id.switch1)));
        switch_.check(matches(isDisplayed()));
    }
    @SmallTest
    @Test
    public void assertMarker() {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = mDevice.findObject(new UiSelector().descriptionContains("BN2 3QA")
                .className(android.widget.FrameLayout.class));
        marker.exists();
    }

    @SmallTest
    @Test
    public void assertBottomView() throws UiObjectNotFoundException, InterruptedException {

        // Added a sleep statement to match the app's execution delay.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = mDevice.findObject(new UiSelector().descriptionContains("BN2 3QA"));
        marker.click();

        UiObject bottomsheet = mDevice.findObject(new UiSelector()
                .resourceId("sussex.android.ase_android:id/pullUp_bottom_sheet")
                .packageName("sussex.android.ase_android"));
        bottomsheet.exists();


        ViewInteraction textView = onView(
                Matchers.allOf(withId(R.id.postcode_big),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                Matchers.allOf(withId(R.id.avgprice_big),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        marker.click();
        marker.clickTopLeft();
        Thread.sleep(1000);
        bottomsheet.swipeUp(10);
        Thread.sleep(5000);

        ViewInteraction textView3 = onView(
                Matchers.allOf(withIndex(withId(R.id.addressList),0),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                Matchers.allOf(withIndex(withId(R.id.priceList),0),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                Matchers.allOf(withIndex(withId(R.id.dateList),0),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));
    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;
            @Override
            public void describeTo(Description description) {
                //matches depending on index
            }
            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

}
