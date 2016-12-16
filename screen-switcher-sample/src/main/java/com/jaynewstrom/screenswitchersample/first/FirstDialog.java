package com.jaynewstrom.screenswitchersample.first;

import android.app.Dialog;
import android.content.Context;

import com.jaynewstrom.screenswitchersample.R;

final class FirstDialog extends Dialog {
    FirstDialog(Context context) {
        super(context);
        setContentView(R.layout.first_dialog);
    }
}
