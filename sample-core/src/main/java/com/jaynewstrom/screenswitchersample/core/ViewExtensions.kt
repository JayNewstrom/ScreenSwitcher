package com.jaynewstrom.screenswitchersample.core

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

fun ViewGroup.inflate(layoutResId: Int, attach: Boolean = false, context: Context = this.context): View {
    return LayoutInflater.from(context).inflate(layoutResId, this, attach)
}

inline fun View.setClickListener(crossinline clickListener: () -> Unit) {
    setOnClickListener(
        DebouncingClickListener {
            clickListener()
        }
    )
}

fun <V : View> View.bindView(@IdRes viewId: Int): V {
    return findViewById(viewId)
}

fun View.dpToPx(dpSize: Int): Int {
    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize.toFloat(), displayMetrics).toInt()
}

fun <T> View.registerObservable(observable: Observable<T>, subscriber: (t: T) -> Unit): () -> Disposable? {
    return registerObservableWithView(observable) { functionT, _ ->
        subscriber(functionT)
    }
}

fun <T> View.registerObservableWithView(
    observable: Observable<T>,
    subscriber: (t: T, view: View) -> Unit
): () -> Disposable? {
    val listener = RxViewAttachStateChangeListener(observable, subscriber)
    if (isAttachedToWindow) {
        listener.onViewAttachedToWindow(this)
    }
    this.addOnAttachStateChangeListener(listener)
    return listener::disposable
}

private class RxViewAttachStateChangeListener<T>(
    private val observable: Observable<T>,
    private val subscriber: (t: T, view: View) -> Unit
) : View.OnAttachStateChangeListener {
    var disposable: Disposable? = null

    override fun onViewAttachedToWindow(view: View) {
        if (disposable != null) {
            throw IllegalStateException("Disposable already created.")
        }
        disposable = observable.subscribe { t -> subscriber.invoke(t, view) }
    }

    override fun onViewDetachedFromWindow(view: View) {
        disposable?.dispose()
        disposable = null
    }
}
