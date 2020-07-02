package screenswitchersample.espressotestingbootstrap.test

import android.os.SystemClock
import android.view.ViewGroup
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.rule.ActivityTestRule
import com.jaynewstrom.screenswitcher.Screen
import org.hamcrest.CoreMatchers
import screenswitchersample.espressotestingbootstrap.R

fun ActivityTestRule<*>.recreateActivity() {
    val previousHashCode = activity.hashCode()
    runOnUiThread {
        activity.recreate()
    }
    // Wait until we actually get a new activity!
    while (previousHashCode == activity.hashCode()) {
        SystemClock.sleep(100)
    }
}

fun ViewInteraction.notInActivityWindow(rule: ActivityTestRule<*>): ViewInteraction {
    return inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(rule.activity.window.decorView))))
}

fun <T : Screen> ActivityTestRule<*>.currentScreen(): T {
    @Suppress("UNCHECKED_CAST")
    return activity.findViewById<ViewGroup>(R.id.screen_host).getChildAt(0).getTag(R.id.screen_switcher_screen) as T
}
