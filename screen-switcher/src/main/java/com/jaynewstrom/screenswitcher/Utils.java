package com.jaynewstrom.screenswitcher;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.FrameLayout;

final class Utils {

    private Utils() {
    }

    static View createViewWithDrawMatching(View view) {
        FrameLayout frameLayout = new FrameLayout(view.getContext());
        Bitmap bitmap = bitmapFromView(view);
        frameLayout.setForeground(new BitmapDrawable(view.getContext().getResources(), bitmap));
        return frameLayout;
    }

    private static Bitmap bitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
