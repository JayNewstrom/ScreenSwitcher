package screenswitchersample.first

import android.app.Dialog
import android.content.Context
import com.jaynewstrom.screenswitcher.dialogmanager.DialogFactory
import screenswitchersample.core.view.ViewPresenter
import javax.inject.Inject

internal class NavigateDialogFactory @Inject constructor() : DialogFactory {
    override fun createDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.navigate_dialog)
        NavigateDialogPresenter(dialog)
        return dialog
    }
}

internal class NavigateDialogPresenter(private val dialog: Dialog) : ViewPresenter(dialog) {
    @Inject lateinit var navigator: FirstNavigator

    init {
        component<FirstComponent>().inject(this)
        bindClick(R.id.navigate_to_second_screen_button) {
            navigator.pushToSecondScreen(view)
            dialog.dismiss()
        }
    }
}
