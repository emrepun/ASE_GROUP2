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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssertionTest {
    @Before
    public void unlockScreen() {
        final CategorySelectionActivity activity = mActivityRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }

    @Rule
    public ActivityTestRule<MainActivity> mapsActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    //Check if object is displayed
    @SmallTest
    @Test
    public void assertSwitch() {
        ViewInteraction switch_ = onView(
                Matchers.allOf(withId(R.id.switch1),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        switch_.check(matches(isDisplayed()));
    }
    @SmallTest
    @Test
    public void assertMarker() throws InterruptedException {
            Thread.sleep(7000);
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = mDevice.findObject(new UiSelector().descriptionContains("GoogleMap")
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
        UiObject marker = mDevice.findObject(new UiSelector().descriptionContains("BN2 3QA. Average price: £114,292.09."));
        marker.click();

        UiObject bottomsheet = mDevice.findObject(new UiSelector()
                .resourceId("sussex.android.ase_android:id/pullUp_bottom_sheet")
                .packageName("sussex.android.ase_android"));
        bottomsheet.exists();


        ViewInteraction textView = onView(
                Matchers.allOf(withId(R.id.postcode_big),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.pullUp_bottom_sheet),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(ViewGroup.class),
                                                2)),
                                0),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                Matchers.allOf(withId(R.id.avgprice_big),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.pullUp_bottom_sheet),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(ViewGroup.class),
                                                2)),
                                1),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                Matchers.allOf(withId(R.id.addressList),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                Matchers.allOf(withId(R.id.priceList), withText("£210,000.00\n£210,000.00\n"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                Matchers.allOf(withId(R.id.priceList),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.TableLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
