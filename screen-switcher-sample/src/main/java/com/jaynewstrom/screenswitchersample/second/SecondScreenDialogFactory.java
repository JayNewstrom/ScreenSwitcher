package com.jaynewstrom.screenswitchersample.second;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import com.jnewstrom.screenswitcher.dialoghub.DialogFactory;

import javax.inject.Inject;

final class SecondScreenDialogFactory implements DialogFactory {
    @Inject SecondScreenDialogFactory() {
    }

    @Override public Dialog createDialog(Context context) {
        return new AlertDialog.Builder(context).setTitle("Second Dialog").setMessage("Rotate to test!").create();
    }
}
