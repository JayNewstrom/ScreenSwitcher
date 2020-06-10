package com.jaynewstrom.screenswitchersample.second

import android.view.View
import androidx.annotation.LayoutRes
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import javax.inject.Inject
import javax.inject.Provider

internal class SecondPresenter private constructor(private val view: View, component: SecondComponent) {
    companion object {
        @LayoutRes fun layoutId() = R.layout.second_view

        fun bindView(view: View, component: SecondComponent) {
            val presenter = SecondPresenter(view, component)
            view.setTag(R.id.presenter, presenter)
        }
    }

    @Inject lateinit var popListener: SecondPopListener
    @Inject lateinit var dialogFactoryProvider: Provider<SecondScreenDialogFactory>
    @Inject lateinit var navigator: SecondNavigator

    private val confirmPopButton: View

    init {
        component.inject(this)
        confirmPopButton = view.findViewById(R.id.btn_confirm_pop)
        confirmPopButton.visibility = if (popListener.showingConfirm) View.VISIBLE else View.GONE

        view.findViewById<View>(R.id.btn_third).setOnClickListener {
            navigator.goToThirdScreen(view)
        }

        view.findViewById<View>(R.id.btn_confirm_pop).setOnClickListener {
            popListener.popConfirmed()
            view.screenTransitioner()?.pop()
        }

        view.findViewById<View>(R.id.btn_show_second_dialog).setOnClickListener {
            view.dialogDisplayer()?.show(dialogFactoryProvider.get())
        }
    }

    fun showConfirmPop() {
        popListener.showingConfirm = true
        confirmPopButton.visibility = View.VISIBLE
    }
}
