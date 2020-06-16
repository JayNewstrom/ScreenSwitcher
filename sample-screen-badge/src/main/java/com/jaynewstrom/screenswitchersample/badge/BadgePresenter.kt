package com.jaynewstrom.screenswitchersample.badge

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.jaynewstrom.recyclerview.RecyclerStateHolder
import com.jaynewstrom.recyclerview.bootstrapRecyclerView
import com.jaynewstrom.screenswitchersample.core.ViewPresenter
import javax.inject.Inject

internal class BadgePresenter private constructor(view: View, component: BadgeComponent) : ViewPresenter(view) {
    companion object {
        @LayoutRes fun layoutId() = R.layout.badge_view
        fun bindView(view: View, component: BadgeComponent) = BadgePresenter(view, component)
    }

    @Inject lateinit var recyclerStateHolder: RecyclerStateHolder

    private val recyclerView = bindView<RecyclerView>(R.id.recycler_view)

    init {
        component.inject(this)
        bootstrapRecyclerView(recyclerView, recyclerStateHolder)
    }
}
