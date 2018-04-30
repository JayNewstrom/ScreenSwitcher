package com.jaynewstrom.screenswitchersample.second

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.jaynewstrom.screenswitchersample.R
import com.jaynewstrom.screenswitchersample.ScreenManager
import com.jaynewstrom.screenswitchersample.third.ThirdScreenFactory
import com.jnewstrom.screenswitcher.dialoghub.DialogHub
import javax.inject.Inject
import javax.inject.Provider

internal class SecondView(context: Context, component: SecondComponent) : LinearLayout(context) {
    @Inject lateinit var screenManager: ScreenManager
    @Inject lateinit var presenter: SecondScreenPresenter
    @Inject lateinit var dialogHub: DialogHub
    @Inject lateinit var dialogFactoryProvider: Provider<SecondScreenDialogFactory>

    @BindView(R.id.btn_confirm_pop) lateinit var confirmPopButton: View

    init {
        id = R.id.second_screen
        orientation = LinearLayout.VERTICAL
        setBackgroundResource(android.R.color.holo_orange_light)
        component.inject(this)
        LayoutInflater.from(context).inflate(R.layout.second_view, this, true)
        ButterKnife.bind(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.takeView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.dropView(this)
    }

    @OnClick(R.id.btn_third) fun onThirdScreenButtonPressed() {
        screenManager.push(ThirdScreenFactory.create())
    }

    fun showConfirmPop() {
        confirmPopButton.visibility = View.VISIBLE
    }

    @OnClick(R.id.btn_confirm_pop) fun onPopConfirmed() {
        presenter.popConfirmed()
        screenManager.pop()
    }

    @OnClick(R.id.btn_show_second_dialog) fun onShowSecondDialogPressed() {
        dialogHub.show(dialogFactoryProvider.get())
    }

    public override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState(), confirmPopButton.visibility)
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        confirmPopButton.visibility = savedState.confirmPopVisibility
    }

    internal class SavedState : View.BaseSavedState {

        val confirmPopVisibility: Int

        constructor(superState: Parcelable, confirmPopVisibility: Int) : super(superState) {
            this.confirmPopVisibility = confirmPopVisibility
        }

        constructor(`in`: Parcel) : super(`in`) {
            confirmPopVisibility = `in`.readInt()
        }

        override fun writeToParcel(destination: Parcel, flags: Int) {
            super.writeToParcel(destination, flags)
            destination.writeInt(confirmPopVisibility)
        }

        companion object {
            @JvmField val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}
