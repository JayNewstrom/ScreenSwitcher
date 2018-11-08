package com.jaynewstrom.screenswitchersample.second

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import com.jaynewstrom.screenswitchersample.core.inflate
import javax.inject.Inject
import javax.inject.Provider

internal class SecondPresenter private constructor(private val view: View, component: SecondComponent) {
    companion object {
        fun createView(context: Context, container: ViewGroup, component: SecondComponent): View {
            val view = container.inflate(layoutResId = R.layout.second_view, context = context)
            val presenter = SecondPresenter(view, component)
            view.setTag(R.id.presenter, presenter)
            return view
        }
    }

    @Inject lateinit var popListener: SecondPopListener
    @Inject lateinit var dialogFactoryProvider: Provider<SecondScreenDialogFactory>
    @Inject lateinit var navigator: SecondNavigator

    private val confirmPopButton: View

    init {
        component.inject(this)
        confirmPopButton = view.findViewById<View>(R.id.btn_confirm_pop)
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
