package com.jaynewstrom.screenswitcher

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screenswitchersample.core.screen.DefaultScreenTransition
import screenswitchersample.espressotestingbootstrap.matchers.withBackgroundColor
import screenswitchersample.espressotestingbootstrap.test.ScreenTestRule
import screenswitchersample.espressotestingbootstrap.test.TestScreenFactory
import screenswitchersample.espressotestingbootstrap.test.currentScreen
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
internal class GenericScreenTest {
    @get:Rule val screenTestRule = ScreenTestRule(GenericTestScreenFactory::class.java)

    // Having the view not be attached to the window makes things significantly easier when dealing with lifecycle stuff.
    // This ensures view.addOnAttachStateChangeListener calls will be symmetric.
    @Test fun testBindView_ViewNotAttachedToWindow() {
        screenTestRule.showScreen()
        onView(withBackgroundColor("#123456")).check(matches(isDisplayed()))
        val screenEvents = screenTestRule.currentScreen<GenericScreen>().screenEvents
        pressBackUnconditionally() // Finish the activity.
        assertThat(screenEvents.await(5, TimeUnit.SECONDS)).isTrue
        assertThat(screenEvents.count).isZero
    }
}

class GenericTestScreenFactory : TestScreenFactory {
    override fun createScreen(): Screen {
        return GenericScreen()
    }
}

class GenericScreen : Screen {
    val screenEvents = CountDownLatch(5)

    override fun createView(hostView: ViewGroup, screenSwitcherState: ScreenSwitcherState): View {
        screenEvents.countDown()
        val view = View(hostView.context)
        view.setBackgroundColor(Color.parseColor("#123456"))
        return view
    }

    override fun bindView(view: View) {
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View) {
                screenEvents.countDown()
            }

            override fun onViewAttachedToWindow(v: View) {
                screenEvents.countDown()
            }
        })

        assertThat(view.isAttachedToWindow).isFalse
        screenEvents.countDown()
    }

    override fun destroyScreen(associatedView: View) {
        assertThat(associatedView.isAttachedToWindow).isTrue
        screenEvents.countDown()
    }

    override fun transition(): Screen.Transition = DefaultScreenTransition
}
