package screenswitchersample.espressotestingbootstrap.activity

import android.content.Intent
import com.jaynewstrom.screenswitcher.Screen
import screenswitchersample.core.activity.InitialScreenFactory
import screenswitchersample.espressotestingbootstrap.test.TestScreenFactory
import javax.inject.Inject

internal class TestInitialScreenFactory @Inject constructor() : InitialScreenFactory {
    override fun create(intent: Intent?): List<Screen> {
        try {
            val screenClassName = intent!!.getStringExtra(TEST_SCREEN_FACTORY)!!
            val screenFactory = Class.forName(screenClassName).newInstance() as TestScreenFactory
            return listOf(screenFactory.createScreen())
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
