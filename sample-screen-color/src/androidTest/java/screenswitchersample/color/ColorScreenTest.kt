package screenswitchersample.color

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jaynewstrom.screenswitcher.Screen
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screenswitchersample.espressotestingbootstrap.matchers.withBackgroundColor
import screenswitchersample.espressotestingbootstrap.matchers.withTextView
import screenswitchersample.espressotestingbootstrap.test.ScreenTestRule
import screenswitchersample.espressotestingbootstrap.test.TestScreenFactory
import screenswitchersample.espressotestingbootstrap.test.assertAvailableForTransition
import screenswitchersample.espressotestingbootstrap.test.notInActivityWindow
import screenswitchersample.espressotestingbootstrap.test.recreateActivity
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class ColorScreenTest {
    @get:Rule val screenTestRule = ScreenTestRule(ColorTestScreenFactory::class.java)

    @Before fun resetNavigator() {
        TestColorScreenNavigator.callback.set { _, _ ->
            throw AssertionError("colorSubmitted not expected.")
        }
    }

    @Test fun ensureItStartsWithTheColorDisplayed() {
        screenTestRule.showScreen()
        onView(withTextView(id = R.id.color_text_view, text = "#FF0000")).check(matches(isDisplayed()))
        onView(withId(R.id.color_view)).check(matches(withBackgroundColor("#FF0000")))
    }

    @Test fun ensureEditTextPreservesTextWhenActivityIsRecreated() {
        screenTestRule.showScreen()
        onView(withId(R.id.edit_text)).perform(replaceText("#00FF00"))
        screenTestRule.recreateActivity()
        onView(withId(R.id.edit_text)).check(matches(withText("#00FF00")))
    }

    @Test fun ensureSubmitCallsNavigator() {
        val countDownLatch = CountDownLatch(1)
        TestColorScreenNavigator.callback.set { colorHex, fromView ->
            assertThat(colorHex).isEqualTo("#00FF00")
            fromView.assertAvailableForTransition()
            countDownLatch.countDown()
        }
        screenTestRule.showScreen()
        onView(withId(R.id.edit_text)).perform(replaceText("#00FF00"))
        onView(withId(R.id.submit_button)).perform(click())
        assertThat(countDownLatch.await(5, TimeUnit.SECONDS)).isTrue
    }

    @Test fun ensureInvalidColorShowsToastAndDoesNotCallNavigator() {
        screenTestRule.showScreen()
        onView(withId(R.id.edit_text)).perform(replaceText("F00"))
        onView(withId(R.id.submit_button)).perform(click())
        onView(withText("Invalid Color")).notInActivityWindow(screenTestRule).check(matches(isDisplayed()))
    }
}

object TestColorScreenNavigator : ColorScreenNavigator {
    val callback = AtomicReference<(colorHex: String, fromView: View) -> Unit>()

    override fun colorSubmitted(colorHex: String, fromView: View) {
        callback.get().invoke(colorHex, fromView)
    }
}

class ColorTestScreenFactory : TestScreenFactory {
    override fun createScreen(): Screen {
        return ColorScreenFactory.create(TestColorScreenNavigator, "#FF0000")
    }
}
