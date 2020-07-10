package screenswitchersample.second

import android.view.View
import com.jaynewstrom.screenswitcher.ScreenPopListener
import screenswitchersample.core.screen.ScreenScope
import javax.inject.Inject

@ScreenScope
internal class SecondPopListener @Inject constructor() : ScreenPopListener {
    private var hasConfirmedPop: Boolean = false
    var showingConfirm: Boolean = false

    fun popConfirmed() {
        hasConfirmedPop = true
    }

    override fun onScreenPop(view: View, popContext: Any?): Boolean {
        if (!hasConfirmedPop) {
            (view.getTag(R.id.presenter) as SecondPresenter).showConfirmPop()
            return true
        }
        return false
    }
}
