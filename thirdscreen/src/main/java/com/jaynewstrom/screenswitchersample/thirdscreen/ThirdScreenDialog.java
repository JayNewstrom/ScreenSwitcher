package com.jaynewstrom.screenswitchersample.thirdscreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import javax.inject.Inject;
import javax.inject.Named;

final class ThirdScreenDialog extends AlertDialog {
    @Inject @Named("dialogMessage") String message;

    ThirdScreenDialog(Context context, ThirdComponent component) {
        super(context);
        component.inject(this);
        setTitle("Third Dialog");
        setMessage(message);
        setButton(AlertDialog.BUTTON_NEGATIVE, "Done", new OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
            }
        });
    }
}
