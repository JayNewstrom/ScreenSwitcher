package com.jaynewstrom.screenswitchersample.second;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jaynewstrom.screenswitchersample.R;
import com.jaynewstrom.screenswitchersample.ScreenManager;
import com.jaynewstrom.screenswitchersample.third.ThirdScreen;
import com.jnewstrom.screenswitcher.dialoghub.DialogHub;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

final class SecondView extends LinearLayout {

    @Inject ScreenManager screenManager;
    @Inject SecondScreenPresenter presenter;
    @Inject DialogHub dialogHub;
    @Inject Provider<SecondScreenDialogFactory> dialogFactoryProvider;

    @BindView(R.id.btn_confirm_pop) View confirmPopButton;

    SecondView(Context context, SecondComponent component) {
        super(context);
        setId(R.id.second_screen);
        setOrientation(VERTICAL);
        setBackgroundResource(android.R.color.holo_orange_light);
        component.inject(this);
        LayoutInflater.from(context).inflate(R.layout.second_view, this, true);
        ButterKnife.bind(this);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    @OnClick(R.id.btn_third) void onThirdScreenButtonPressed() {
        screenManager.push(new ThirdScreen());
    }

    public void showConfirmPop() {
        confirmPopButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_confirm_pop) void onPopConfirmed() {
        presenter.popConfirmed();
        screenManager.pop();
    }

    @OnClick(R.id.btn_show_second_dialog) void onShowSecondDialogPressed() {
        dialogHub.show(dialogFactoryProvider.get());
    }

    @Override public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), confirmPopButton.getVisibility());
    }

    @Override public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        confirmPopButton.setVisibility(savedState.confirmPopVisibility);
    }

    static final class SavedState extends BaseSavedState {

        final int confirmPopVisibility;

        SavedState(Parcelable superState, int confirmPopVisibility) {
            super(superState);
            this.confirmPopVisibility = confirmPopVisibility;
        }

        SavedState(Parcel in) {
            super(in);
            confirmPopVisibility = in.readInt();
        }

        @Override public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(confirmPopVisibility);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
