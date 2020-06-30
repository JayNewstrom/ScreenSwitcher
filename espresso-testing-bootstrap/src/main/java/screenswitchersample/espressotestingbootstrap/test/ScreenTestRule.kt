package screenswitchersample.espressotestingbootstrap.test

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.jaynewstrom.screenswitcher.Screen
import screenswitchersample.core.activity.ScreenSwitcherActivity
import screenswitchersample.core.utilities.AbstractActivityLifecycleCallbacks
import screenswitchersample.espressotestingbootstrap.activity.TEST_SCREEN_FACTORY

class ScreenTestRule(
    private val screenFactoryClass: Class<out TestScreenFactory>
) : IntentsTestRule<ScreenSwitcherActivity>(ScreenSwitcherActivity::class.java, false, false) {
    fun showScreen() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val intent = Intent(application, ScreenSwitcherActivity::class.java)
        intent.putExtra(TEST_SCREEN_FACTORY, screenFactoryClass.name)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) // Remove enter animation.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        launchActivity(intent)
        application.registerActivityLifecycleCallbacks(object : AbstractActivityLifecycleCallbacks() {
            override fun onActivityPaused(activity: Activity) {
                activity.overridePendingTransition(0, 0) // Remove exit animation.
            }
        })
    }
}

interface TestScreenFactory {
    fun createScreen(): Screen
}
