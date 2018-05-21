package com.jnewstrom.screenswitcher.dialoghub

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.screenmanager.ScreenManager
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

/**
 * Returns the dialogDisplayer if this view is associated with the current screen. Null otherwise.
 */
tailrec fun View.dialogDisplayer(): DialogDisplayer? {
    val parent = parent as? ViewGroup ?: return null
    val screen = getTag(R.id.screen_switcher_screen) as? Screen?
    if (screen != null) {
        val screenManager = parent.getTag(R.id.screen_manager) as ScreenManager
        return if (screenManager.isActiveScreen(screen)) {
            val dialogHub = parent.getTag(R.id.dialog_hub) as DialogHub
            DialogDisplayer(screen, screenManager, dialogHub)
        } else {
            null
        }
    }
    return parent.dialogDisplayer()
}

class DialogDisplayer internal constructor(
    private val screen: Screen,
    private val screenHub: ScreenManager,
    private val dialogHub: DialogHub
) {
    fun show(dialogFactory: DialogFactory) {
        dialogHub.show(WrapperDialogFactory(dialogFactory))
    }

    private inner class WrapperDialogFactory(private val dialogFactory: DialogFactory) : DialogFactory {
        override fun createDialog(context: Context): Dialog {
            val dialog = dialogFactory.createDialog(context)
            val contentView = dialog.findViewById<View>(android.R.id.content)
            contentView.setTag(R.id.screen_switcher_screen, screen)
            val parent = contentView.parent as View
            parent.setTag(R.id.screen_manager, screenHub)
            parent.setTag(R.id.dialog_hub, dialogHub)
            return dialog
        }
    }
}
