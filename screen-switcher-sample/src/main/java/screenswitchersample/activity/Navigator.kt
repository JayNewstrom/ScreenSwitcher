package screenswitchersample.activity

import android.view.View
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import screenswitchersample.first.FirstNavigator
import screenswitchersample.second.SecondNavigator
import screenswitchersample.second.SecondScreenFactory
import screenswitchersample.third.ThirdNavigator
import screenswitchersample.third.ThirdScreenFactory
import javax.inject.Inject

internal class Navigator @Inject constructor() : FirstNavigator, SecondNavigator, ThirdNavigator {
    override fun pushToSecondScreen(fromView: View) {
        fromView.screenTransitioner()?.push(SecondScreenFactory.create(this))
    }

    override fun replaceWithSecondScreen(fromView: View) {
        fromView.screenTransitioner()?.replaceScreenWith(SecondScreenFactory.create(this))
    }

    override fun goToThirdScreen(fromView: View) {
        fromView.screenTransitioner()?.push(ThirdScreenFactory.create(this))
    }

    override fun popToSecondScreen(fromView: View) {
        fromView.screenTransitioner()?.popTo(SecondScreenFactory.create(this))
    }
}
