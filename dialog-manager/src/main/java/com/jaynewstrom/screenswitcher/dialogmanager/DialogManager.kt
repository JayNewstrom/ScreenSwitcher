package com.jaynewstrom.screenswitcher.dialogmanager

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.screenSwitcherDataIfActive
import com.jaynewstrom.screenswitcher.screenmanager.ScreenManager
import com.jaynewstrom.screenswitcher.setupForViewExtensions
import java.lang.ref.WeakReference

class DialogManager(isTransitioning: () -> Boolean) {
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
fun View.dialogDisplayer(): DialogDisplayer? {
    val screenSwitcherData = screenSwitcherDataIfActive() ?: return null
    val screenManager = screenSwitcherData.hostView.getTag(R.id.screen_manager) as ScreenManager
    val dialogManager = screenSwitcherData.hostView.getTag(R.id.dialog_manager) as DialogManager
    return DialogDisplayer(
        screenSwitcherData.screen,
        screenManager,
        dialogManager,
        screenSwitcherData.screenSwitcherState
    )
}

class DialogDisplayer internal constructor(
    private val screen: Screen,
    private val screenManager: ScreenManager,
    private val dialogManager: DialogManager,
    private val state: ScreenSwitcherState
) {
    fun show(dialogFactory: DialogFactory) {
        dialogManager.show(WrapperDialogFactory(dialogFactory))
    }

    private inner class WrapperDialogFactory(private val dialogFactory: DialogFactory) : DialogFactory {
        override fun createDialog(context: Context): Dialog {
            val dialog = dialogFactory.createDialog(context)
            val contentView = dialog.findViewById<View>(android.R.id.content)
            contentView.setTag(R.id.screen_switcher_screen, screen)
            val parent = contentView.parent as View
            parent.setTag(R.id.screen_manager, screenManager)
            parent.setTag(R.id.dialog_manager, dialogManager)
            parent.setupForViewExtensions(screenManager.screenSwitcher!!, state)
            return dialog
        }
    }
}