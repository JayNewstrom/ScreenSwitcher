package com.jaynewstrom.screenswitchersample.activity

import android.view.View
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import com.jaynewstrom.screenswitchersample.first.FirstNavigator
import com.jaynewstrom.screenswitchersample.second.SecondNavigator
import com.jaynewstrom.screenswitchersample.second.SecondScreenFactory
import com.jaynewstrom.screenswitchersample.third.ThirdNavigator
import com.jaynewstrom.screenswitchersample.third.ThirdScreenFactory
import com.jaynewstrom.screenswitchersample.viewpdf.ViewPdfScreenFactory
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

    override fun viewPdf(fromView: View) {
        fromView.screenTransitioner()?.push(ViewPdfScreenFactory.create())
    }
}
