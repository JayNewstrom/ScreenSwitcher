package com.jaynewstrom.screenswitchersample.badge

import android.view.View
import androidx.annotation.LayoutRes
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jaynewstrom.screenswitchersample.core.ViewPresenter
import javax.inject.Inject

internal class BadgePresenter private constructor(view: View, component: BadgeComponent) : ViewPresenter(view) {
    companion object {
        @LayoutRes fun layoutId() = R.layout.badge_view
        fun bindView(view: View, component: BadgeComponent) = BadgePresenter(view, component)
    }

    @Inject @BadgeCount lateinit var badgeCountRelay: BehaviorRelay<Int>

    init {
        component.inject(this)

        bindClick(R.id.increment_button) {
            badgeCountRelay.accept(badgeCountRelay.value!! + 1)
        }

        bindClick(R.id.decrement_button) {
            badgeCountRelay.accept(badgeCountRelay.value!! - 1)
        }
    }
}
