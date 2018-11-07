package com.jaynewstrom.screenswitchersample.second

import android.view.View
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenPopListener
import com.jaynewstrom.screenswitchersample.R
import com.jaynewstrom.screenswitchersample.core.ScreenScope
import javax.inject.Inject

@ScreenScope
internal class SecondPopListener @Inject constructor() : ScreenPopListener {
    private var hasConfirmedPop: Boolean = false
    var showingConfirm: Boolean = false

    fun popConfirmed() {
        hasConfirmedPop = true
    }

    override fun onScreenPop(view: View, screen: Screen): Boolean {
        if (!hasConfirmedPop) {
            (view.getTag(R.id.presenter) as SecondPresenter).showConfirmPop()
            return true
        }
        return false
    }
}
