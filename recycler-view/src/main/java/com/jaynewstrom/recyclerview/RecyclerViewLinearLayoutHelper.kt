package com.jaynewstrom.recyclerview

import android.widget.LinearLayout
import com.jaynewstrom.screenswitchersample.core.inflate

fun LinearLayout.addViewFactories(
    viewFactories: List<RecyclerItemViewFactory<*>>
) {
    for (viewFactory in viewFactories) {
        val itemView = inflate(viewFactory.layoutResource(context))

        val presenter = viewFactory.initialize(itemView)
        viewFactory.bindAny(presenter as Any)

        addView(itemView)
    }
}
