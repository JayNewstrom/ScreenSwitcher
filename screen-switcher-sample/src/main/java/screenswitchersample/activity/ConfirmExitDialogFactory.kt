package screenswitchersample.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import com.jaynewstrom.screenswitcher.dialogmanager.DialogFactory
import screenswitchersample.R
import screenswitchersample.core.view.ViewPresenter

internal class ConfirmExitDialogFactory : DialogFactory {
    override fun createDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.confirm_exit_dialog)
        ConfirmExitPresenter(dialog)
        return dialog
    }
}

private class ConfirmExitPresenter(dialog: Dialog) : ViewPresenter(dialog) {
    init {
        bindClick(R.id.exit_button) {
            context.asActivity().finish()
        }
        bindClick(R.id.cancel_button) {
            dialog.dismiss()
        }
    }
}

private fun Context.asActivity(): Activity {
    if (this is Activity) {
        return this
    }
    if (this is ContextWrapper) {
        return baseContext.asActivity()
    }
    throw IllegalArgumentException("context is not an instance of Activity")
}
