package com.jaynewstrom.screenswitcher.tabbar

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.widget.FrameLayout

internal class StateAwareFrameLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    var saveStateDelegate: SaveStateDelegate? = null

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        // Don't call super, we will do the saving of the state.
        saveStateDelegate?.dispatchSaveInstanceState(container)
    }

    internal interface SaveStateDelegate {
        fun dispatchSaveInstanceState(container: SparseArray<Parcelable>)
    }
}
