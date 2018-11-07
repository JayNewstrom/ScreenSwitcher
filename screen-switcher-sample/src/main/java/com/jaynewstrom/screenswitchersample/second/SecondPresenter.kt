package com.jaynewstrom.screenswitchersample.second

import android.content.Context
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import com.jaynewstrom.screenswitchersample.R
import com.jaynewstrom.screenswitchersample.core.inflate
import com.jaynewstrom.screenswitchersample.third.ThirdScreenFactory
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

    @BindView(R.id.btn_confirm_pop) lateinit var confirmPopButton: View

    init {
        component.inject(this)
        ButterKnife.bind(this, view)
        confirmPopButton.visibility = if (popListener.showingConfirm) View.VISIBLE else View.GONE
    }

    @OnClick(R.id.btn_third) fun onThirdScreenButtonPressed() {
        view.screenTransitioner()?.push(ThirdScreenFactory.create())
    }

    fun showConfirmPop() {
        popListener.showingConfirm = true
        confirmPopButton.visibility = View.VISIBLE
    }

    @OnClick(R.id.btn_confirm_pop) fun onPopConfirmed() {
        popListener.popConfirmed()
        view.screenTransitioner()?.pop()
    }

    @OnClick(R.id.btn_show_second_dialog) fun onShowSecondDialogPressed() {
        view.dialogDisplayer()?.show(dialogFactoryProvider.get())
    }
}
