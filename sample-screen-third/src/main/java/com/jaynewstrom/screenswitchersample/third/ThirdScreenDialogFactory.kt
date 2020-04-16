package com.jaynewstrom.screenswitchersample.third

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.screenswitcher.dialogmanager.DialogFactory
import javax.inject.Inject
import javax.inject.Named

internal class ThirdScreenDialogFactory : DialogFactory {
    override fun createDialog(context: Context): Dialog {
        return ThirdScreenDialog(context)
    }
}

internal class ThirdScreenDialog(context: Context) : AlertDialog(context) {
    @Inject @field:Named("dialogMessage") lateinit var message: String

    init {
        val component = Concrete.getComponent<ThirdComponent>(context)
        component.inject(this)
        setTitle("Third Dialog")
        setMessage(message)
        setButton(AlertDialog.BUTTON_NEGATIVE, "Done") { _, _ -> }
    }
}
