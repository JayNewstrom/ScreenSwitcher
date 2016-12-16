package com.jaynewstrom.screenswitchersample.first;

import android.app.Dialog;
import android.content.Context;

import com.jnewstrom.screenswitcher.dialoghub.DialogFactory;

import javax.inject.Inject;

final class FirstDialogFactory implements DialogFactory {
    @Inject FirstDialogFactory() {
    }

    @Override public Dialog createDialog(Context context) {
        return new FirstDialog(context);
    }
}
