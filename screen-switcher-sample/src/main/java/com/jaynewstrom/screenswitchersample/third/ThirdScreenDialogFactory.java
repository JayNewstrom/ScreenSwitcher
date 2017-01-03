package com.jaynewstrom.screenswitchersample.third;

import android.app.Dialog;
import android.content.Context;

import com.jnewstrom.screenswitcher.dialoghub.ScopedDialogFactory;

final class ThirdScreenDialogFactory extends ScopedDialogFactory<ThirdComponent> {
    ThirdScreenDialogFactory(Context scopedContext) {
        super(scopedContext);
    }

    @Override public Dialog createDialog(Context context, ThirdComponent component) {
        return new ThirdScreenDialog(context, component);
    }
}
