package com.jnewstrom.screenswitcher.dialoghub

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import java.lang.ref.WeakReference

class DialogHub(isTransitioning: () -> Boolean) {
    private val dialogCallbackHelper = DialogCallbackHelper(isTransitioning)

    private var dialogInformationList: MutableList<DialogInformation> = mutableListOf()
    private var savedDialogFactories: List<SavedDialogFactory>? = null
    private var activity: Activity? = null

    fun attachActivity(activity: Activity) {
        this.activity = activity
    }

    fun dropActivity(activity: Activity) {
        if (this.activity === activity) {
            this.activity = null
        }
    }

    fun show(dialogFactory: DialogFactory) {
        show(dialogFactory, null)
    }

    private fun show(dialogFactory: DialogFactory, savedState: Bundle?) {
        val activity = activity ?: throw IllegalStateException("DialogHub is not attached to the activity.")
        val dialog = dialogFactory.createDialog(activity)
        if (savedState != null) {
            dialog.onRestoreInstanceState(savedState)
        } else {
            dialog.show()
        }
        dialogCallbackHelper.bootstrap(dialog)
        dialogInformationList.add(DialogInformation(dialog, dialogFactory))
    }

    fun saveState() {
        val items = mutableListOf<SavedDialogFactory>()
        for (information in dialogInformationList) {
            val dialog: Dialog? = information.dialogWeakReference.get()
            if (dialog != null && dialog.isShowing) {
                items.add(SavedDialogFactory(information.dialogFactory, dialog.onSaveInstanceState()))
                dialog.dismiss()
            }
        }
        dialogInformationList = mutableListOf()
        savedDialogFactories = items
    }

    fun restoreState() {
        val savedDialogFactories = savedDialogFactories ?: return
        dialogInformationList = mutableListOf()
        for (savedDialogFactory in savedDialogFactories) {
            show(savedDialogFactory.dialogFactory, savedDialogFactory.savedState)
        }
        this.savedDialogFactories = null
    }

    private class DialogInformation constructor(dialog: Dialog, val dialogFactory: DialogFactory) {
        val dialogWeakReference: WeakReference<Dialog> = WeakReference(dialog)
    }

    private class SavedDialogFactory constructor(val dialogFactory: DialogFactory, val savedState: Bundle)
}
