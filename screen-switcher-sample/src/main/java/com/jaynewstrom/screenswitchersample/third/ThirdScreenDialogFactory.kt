package com.jaynewstrom.screenswitchersample.third

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context

import com.jnewstrom.screenswitcher.dialoghub.ScopedDialogFactory
import javax.inject.Inject
import javax.inject.Named

internal class ThirdScreenDialogFactory(scopedContext: Context) : ScopedDialogFactory<ThirdComponent>(scopedContext) {
    public override fun createDialog(context: Context, component: ThirdComponent): Dialog {
        return ThirdScreenDialog(context, component)
    }
}

internal class ThirdScreenDialog(context: Context, component: ThirdComponent) : AlertDialog(context) {
    @Inject @field:Named("dialogMessage") lateinit var message: String

    init {
        component.inject(this)
        setTitle("Third Dialog")
        setMessage(message)
        setButton(AlertDialog.BUTTON_NEGATIVE, "Done") { _, _ -> }
    }
}
