package screenswitchersample.activity

import android.animation.ValueAnimator
import android.os.SystemClock
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screenswitchersample.R
import screenswitchersample.core.activity.ScreenSwitcherActivity

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule val activityTestRule = ActivityTestRule(ScreenSwitcherActivity::class.java)

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

    @Test fun testReplaceRootScreenInTabBar() {
        onView(withId(R.id.btn_replace_with_second)).perform(click())
        onView(withTextView(id = R.id.title_text_view, text = "Second Screen")).check(matches(isDisplayed()))
        onView(withText("Color")).perform(click())
        onView(withTextView(id = R.id.color_text_view, text = "#FF0000")).check(matches(isDisplayed()))
        onView(withText("Demo")).perform(click())
        onView(withTextView(id = R.id.title_text_view, text = "Second Screen")).check(matches(isDisplayed()))
    }

    @Test fun testClickingTabItemPopsToRootOfTab() {
        onView(withId(R.id.btn_second)).perform(click())
        onView(withText("Color")).perform(click())
        onView(withTextView(id = R.id.edit_text, text = "")).perform(replaceText("#00FF00"))
        onView(withTextView(id = R.id.submit_button, text = "Submit")).perform(click())
        onView(withTextView(id = R.id.color_text_view, text = "#00FF00")).check(matches(isDisplayed()))
        onView(withTextView(id = R.id.edit_text, text = "")).perform(replaceText("#0000FF"))
        onView(withTextView(id = R.id.submit_button, text = "Submit")).perform(click())
        onView(withTextView(id = R.id.color_text_view, text = "#0000FF")).check(matches(isDisplayed()))
        onView(withText("Color")).perform(click())
        onView(withTextView(id = R.id.color_text_view, text = "#FF0000")).check(matches(isDisplayed()))
    }

    @Test fun testExitConfirmation() {
        onView(withId(R.id.btn_second)).perform(click())
        onView(withText("Color")).perform(click())
        onView(withTextView(id = R.id.edit_text, text = "")).perform(replaceText("#00FF00"))
        onView(withText("Submit")).perform(click())
        onView(withTextView(id = R.id.color_text_view, text = "#00FF00")).check(matches(isDisplayed()))
        pressBack()
        onView(withTextView(id = R.id.color_text_view, text = "#FF0000")).check(matches(isDisplayed()))
        pressBack()
        onView(withTextView(id = R.id.title_text_view, text = "Second Screen")).check(matches(isDisplayed()))
        pressBack()
        onView(withId(R.id.btn_confirm_pop)).perform(click())
        onView(withText("First Screen")).check(matches(isDisplayed()))
        pressBack()
        onView(withText("Are you sure you want to exit?")).check(matches(isDisplayed()))
        onView(withText("Exit")).perform(click())
        assertThat(activityTestRule.activity.isFinishing).isTrue
    }

    @Test fun testDialogSavedStateRestoration() {
        onView(withId(R.id.btn_show_first_dialog)).perform(click())
        onView(withId(R.id.first_edit_text)).perform(replaceText("This is some text!"))
        activityTestRule.recreateActivity()
        onView(withId(R.id.first_edit_text)).check(matches(withText("This is some text!")))
    }

    @Test fun testContentSavedStateRestoration() {
        onView(withText("Color")).perform(click())
        onView(withTextView(id = R.id.color_text_view, text = "#FF0000")).check(matches(isDisplayed()))
        onView(withTextView(id = R.id.edit_text, text = "")).perform(replaceText("#00FF00"))
        onView(withId(R.id.submit_button)).perform(click())
        onView(withTextView(id = R.id.color_text_view, text = "#00FF00")).check(matches(isDisplayed()))
        onView(withTextView(id = R.id.edit_text, text = "")).perform(replaceText("preserve me!"))
        activityTestRule.recreateActivity()
        onView(withTextView(id = R.id.color_text_view, text = "#00FF00")).check(matches(isDisplayed()))
        onView(withTextView(id = R.id.edit_text, text = "preserve me!")).check(matches(isDisplayed()))
        pressBack()
        onView(withTextView(id = R.id.edit_text, text = "#00FF00")).check(matches(isDisplayed()))
    }

    @Test fun testBadgeIconUpdate() {
        onView(withText("Badge")).perform(click())
        onView(withTextView(id = R.id.bottom_bar_badge_text_view, text = "5")).check(matches(isDisplayed()))
        onView(withText("Increment 1")).perform(click())
        onView(withTextView(id = R.id.bottom_bar_badge_text_view, text = "6")).check(matches(isDisplayed()))
        onView(withText("Increment 1")).perform(click(), click())
        onView(withTextView(id = R.id.bottom_bar_badge_text_view, text = "8")).check(matches(isDisplayed()))
        onView(withText("Decrement 1")).perform(click(), click(), click(), click())
        onView(withTextView(id = R.id.bottom_bar_badge_text_view, text = "4")).check(matches(isDisplayed()))
    }

    @Test fun testDialogWithNavigationWorks() {
        onView(withId(R.id.btn_show_navigate_dialog)).perform(click())
        onView(withId(R.id.navigate_to_second_screen_button)).perform(click())
        pressBack()
        onView(withId(R.id.btn_confirm_pop)).perform(click())
        onView(withId(R.id.btn_show_navigate_dialog)).perform(click())
        activityTestRule.recreateActivity()
        onView(withId(R.id.navigate_to_second_screen_button)).perform(click())
        pressBack()
        onView(withId(R.id.btn_confirm_pop)).check(matches(isDisplayed())).perform(click())

        // Ensure removing the screen doesn't leak anything!
        onView(withId(R.id.btn_replace_with_second)).perform(click())
    }
}

private fun ActivityTestRule<*>.recreateActivity() {
    val previousHashCode = activity.hashCode()
    runOnUiThread {
        activity.recreate()
    }
    // Wait until we actually get a new activity!
    while (previousHashCode == activity.hashCode()) {
        SystemClock.sleep(100)
    }
}
