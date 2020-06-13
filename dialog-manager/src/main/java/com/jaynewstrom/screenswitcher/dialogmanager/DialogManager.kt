package com.jaynewstrom.screenswitcher.dialogmanager

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenLifecycleListener
import com.jaynewstrom.screenswitcher.screenSwitcherDataIfActive
import com.jaynewstrom.screenswitcher.screenmanager.CompositeScreenLifecycleListener
import java.lang.ref.WeakReference

class DialogManager(compositeScreenLifecycleListener: CompositeScreenLifecycleListener) {
    private var dialogInformationList: MutableList<DialogInformation> = mutableListOf()
    private var savedDialogFactories: List<SavedDialogFactory>? = null
    private var activity: Activity? = null

    init {
        compositeScreenLifecycleListener.register(RemoveDialogInformationListener())
    }

    fun attachActivity(activity: Activity) {
        this.activity = activity
    }

    fun dropActivity(activity: Activity) {
        if (this.activity === activity) {
            this.activity = null
        }
    }

    internal fun show(dialogFactory: DialogFactory, screen: Screen? = null, savedState: Bundle? = null) {
        val activity = activity ?: throw IllegalStateException("DialogHub is not attached to the activity.")
        val dialog = dialogFactory.createDialog(activity)
        if (savedState != null) {
            dialog.onRestoreInstanceState(savedState)
        } else {
            dialog.show()
        }
        dialogInformationList.add(DialogInformation(dialog, dialogFactory, screen))
    }

    fun saveState() {
        val items = mutableListOf<SavedDialogFactory>()
        for (information in dialogInformationList) {
            val dialog: Dialog? = information.dialogWeakReference.get()
            if (dialog != null && dialog.isShowing) {
                items.add(
                    SavedDialogFactory(information.dialogFactory, dialog.onSaveInstanceState(), information.screen)
                )
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
            show(savedDialogFactory.dialogFactory, savedDialogFactory.screen, savedDialogFactory.savedState)
        }
        this.savedDialogFactories = null
    }

    private class DialogInformation(dialog: Dialog, val dialogFactory: DialogFactory, val screen: Screen?) {
        val dialogWeakReference: WeakReference<Dialog> = WeakReference(dialog)
    }

    private class SavedDialogFactory(val dialogFactory: DialogFactory, val savedState: Bundle, val screen: Screen?)

    private inner class RemoveDialogInformationListener : ScreenLifecycleListener {
        override fun onScreenAdded(screen: Screen) {}

        override fun onScreenBecameActive(screen: Screen) {}

        override fun onScreenBecameInactive(screen: Screen) {}

        override fun onScreenRemoved(screen: Screen) {
            for (dialogInformation in ArrayList(dialogInformationList)) {
                if (dialogInformation.screen == screen) {
                    val dialog = dialogInformation.dialogWeakReference.get()
                    if (dialog != null && dialog.isShowing) {
                        dialog.dismiss()
                    }
                    dialogInformationList.remove(dialogInformation)
                }
            }
        }
    }
}

/**
 * Returns the dialogDisplayer if this view is associated with the current screen. Null otherwise.
 */
fun View.dialogDisplayer(): DialogDisplayer? {
    val screenSwitcherData = screenSwitcherDataIfActive() ?: return null
    val dialogManager = Concrete.getComponent<DialogManagerComponent>(context).dialogManager
    val wall = Concrete.findWall<ConcreteWall<*>>(context)
    return DialogDisplayer(
        screenSwitcherData.screen,
        wall,
        dialogManager
    )
}

class DialogDisplayer internal constructor(
    private val screen: Screen,
    private val wall: ConcreteWall<*>,
    private val dialogManager: DialogManager
) {
    fun show(dialogFactory: DialogFactory) {
        dialogManager.show(WrapperDialogFactory(dialogFactory), screen)
    }

    private inner class WrapperDialogFactory(private val dialogFactory: DialogFactory) : DialogFactory {
        override fun createDialog(context: Context): Dialog {
            return dialogFactory.createDialog(wall.createContext(context))
        }
    }
}
