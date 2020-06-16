package com.jaynewstrom.recyclerview

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes

abstract class RecyclerItemViewFactory<T> {

    /**
     * Because we cache the result of the layoutResource function call, every instance of the adapter will have a fixed
     * resource which will not change unless a new adapter is created.
     */
    @LayoutRes abstract fun layoutResource(context: Context): Int

    abstract fun initialize(itemView: View): T

    abstract fun bind(presenter: T)

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    /**
     * This is needed because the presenter needs to be bound from the RecyclerViewAdapter, which lacks any explicit
     * knowledge of the class of T
     */
    fun bindAny(presenter: Any) {
        @Suppress("UNCHECKED_CAST")
        bind(presenter as T)
    }

    /**
     * Return true if obj is the same item as this (One example would be if they have the same id).
     * The default is overly aggressive at rejecting, override for better performance.
     */
    open fun isSameItemAs(obj: Any?): Boolean = equals(obj)

    /**
     * Return true if obj is the same exactly the same contents as this. Meaning they'll be displayed in the view
     * exactly the same. The default is overly aggressive at rejecting, override for better performance.
     */
    open fun hasSameContentsAs(obj: Any?): Boolean = false
}
