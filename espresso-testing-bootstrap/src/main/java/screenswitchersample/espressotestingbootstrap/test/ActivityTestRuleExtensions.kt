package screenswitchersample.espressotestingbootstrap.test

import android.os.SystemClock
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers

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
