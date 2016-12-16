package com.jnewstrom.screenswitcher.dialoghub;

import android.app.Dialog;
import android.content.Context;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;

public abstract class ScopedDialogFactory<C> implements DialogFactory {
    private final ConcreteWall<C> concreteWall;

    public ScopedDialogFactory(Context scopedContext) {
        concreteWall = Concrete.findWall(scopedContext);
    }

    @Override public final Dialog createDialog(Context context) {
        Context scopedContext = concreteWall.createContext(context);
        return createDialog(scopedContext, concreteWall.getComponent());
    }

    protected abstract Dialog createDialog(Context context, C component);
}
