package com.jaynewstrom.screenswitcher.dialogmanager

import android.app.Dialog
import android.content.Context

import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall

abstract class ScopedDialogFactory<in C>(scopedContext: Context) : DialogFactory {
    private val concreteWall: ConcreteWall<C> = Concrete.findWall(scopedContext)

    override fun createDialog(context: Context): Dialog {
        val scopedContext = concreteWall.createContext(context)
        return createDialog(scopedContext, concreteWall.component)
    }

    protected abstract fun createDialog(context: Context, component: C): Dialog
}
