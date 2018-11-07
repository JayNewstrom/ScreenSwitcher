package com.jaynewstrom.screenswitchersample.activity

import android.animation.ValueAnimator
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.jaynewstrom.screenswitchersample.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule val activityTestRule = ActivityTestRule(MainActivity::class.java)

    // Animations need to be turned off in order for these to pass.
    @Before fun setup() {
        val method = ValueAnimator::class.java.getDeclaredMethod("setDurationScale", Float::class.javaPrimitiveType)
        method.invoke(null, 0.toFloat())
    }

    @Test fun testAllTheThings() {
        onView(withId(R.id.btn_second)).perform(click())
        pressBack()
        onView(withId(R.id.btn_confirm_pop)).perform(click())

        onView(withId(R.id.btn_second)).perform(click())
        onView(withId(R.id.btn_third)).perform(click())
        pressBack()

        onView(withId(R.id.btn_third)).perform(click())
        onView(withId(R.id.btn_pop_two)).perform(click())
        onView(withId(R.id.btn_confirm_pop)).check(matches(isDisplayed()))
    }
}