package com.jaynewstrom.screenswitchersample.core

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.IdRes
import com.jaynewstrom.concrete.Concrete
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

abstract class ViewPresenter(protected val view: View) {
    val context: Context = view.context

    constructor(dialog: Dialog) : this(dialog.findViewById<View>(android.R.id.content))

    fun <V : View> bindView(@IdRes viewId: Int): V {
        return view.findViewById(viewId)
    }

    fun <V : View> bindOptionalView(@IdRes viewId: Int): V? {
        return view.findViewById(viewId)
    }

    fun Int.toPx(): Int {
        return view.dpToPx(this)
    }

    inline fun <reified T> component(): T = Concrete.getComponent(context)

    fun bindClick(@IdRes viewId: Int, clickListener: () -> Unit) {
        view.findViewById<View>(viewId).setClickListener(clickListener)
    }

    fun bindCheckChanged(@IdRes viewId: Int, checkChangedListener: (checked: Boolean) -> Unit) {
        val compoundButton = view.findViewById<CompoundButton>(viewId)
        compoundButton.setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
            checkChangedListener(checked)
        }
    }

    fun <T> registerObservable(observable: Observable<T>, subscriber: (t: T) -> Unit): () -> Disposable? {
        return view.registerObservable(observable, subscriber)
    }
}
