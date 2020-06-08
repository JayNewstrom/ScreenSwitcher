package com.jaynewstrom.screenswitchersample.core

import android.view.View

class DebouncingClickListener(private val clickListener: (View) -> Unit) : View.OnClickListener {
    override fun onClick(v: View) {
        if (denyClick) {
            return
        }
        // This order matters. The post needs to happen before running the click listener to ensure resetting deny click
        // happens (due to the view being attached).
        denyClick = true
        v.post { denyClick = false }
        clickListener(v)
    }

    companion object {
        private var denyClick = false
    }
}
