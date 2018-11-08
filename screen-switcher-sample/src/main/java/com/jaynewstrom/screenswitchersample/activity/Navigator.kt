package com.jaynewstrom.screenswitchersample.activity

import android.view.View
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import com.jaynewstrom.screenswitchersample.first.FirstNavigator
import com.jaynewstrom.screenswitchersample.second.SecondScreenFactory
import javax.inject.Inject

internal class Navigator @Inject constructor() : FirstNavigator {
    override fun pushToSecondScreen(fromView: View) {
        fromView.screenTransitioner()?.push(SecondScreenFactory.create())
    }

    override fun replaceWithSecondScreen(fromView: View) {
        fromView.screenTransitioner()?.replaceScreenWith(SecondScreenFactory.create())
    }
}
