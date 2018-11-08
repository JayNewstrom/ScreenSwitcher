package com.jaynewstrom.screenswitchersample.first

import android.view.View

interface FirstNavigator {
    fun pushToSecondScreen(fromView: View)
    fun replaceWithSecondScreen(fromView: View)
    fun viewPdf(fromView: View)
}
