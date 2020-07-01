package screenswitchersample.espressotestingbootstrap.test

import android.view.View
import com.jaynewstrom.screenswitcher.screenSwitcherData
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import org.fest.assertions.api.Assertions.assertThat

fun View.assertAvailableForTransition() {
    assertThat(screenTransitioner()).isNotNull
    val screenSwitcherData = screenSwitcherData()
    assertThat(screenSwitcherData.screenSwitcher.isTransitioning).isFalse
}
