package screenswitchersample.color

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jaynewstrom.screenswitcher.Screen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import screenswitchersample.espressotestingbootstrap.test.ScreenTestRule
import screenswitchersample.espressotestingbootstrap.test.TestScreenFactory

@RunWith(AndroidJUnit4::class)
class ColorScreenTest {
    @get:Rule val screenTestRule = ScreenTestRule(ColorTestScreenFactory::class.java)

    @Test fun ensureItStartsWithTheColorDisplayed() {
        screenTestRule.showScreen()
        onView(withText("#FF0000")).check(matches(isDisplayed()))
    }
}

class ColorTestScreenFactory : TestScreenFactory {
    override fun createScreen(): Screen {
        return ColorScreenFactory.create("#FF0000")
    }
}
