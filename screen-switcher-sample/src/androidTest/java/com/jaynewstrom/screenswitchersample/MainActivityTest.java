package com.jaynewstrom.screenswitchersample;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public final class MainActivityTest {

    @Rule public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    // Animations need to be turned of in order for these to pass.
    @Test public void testAllTheThings() {
        onView(withId(R.id.btn_second)).perform(click());
        pressBack();
        onView(withId(R.id.btn_confirm_pop)).perform(click());

        onView(withId(R.id.btn_second)).perform(click());
        onView(withId(R.id.btn_third)).perform(click());
        pressBack();

        onView(withId(R.id.btn_third)).perform(click());
        onView(withId(R.id.btn_pop_two)).perform(click());
        onView(withId(R.id.btn_confirm_pop)).check(matches(isDisplayed()));
    }
}
