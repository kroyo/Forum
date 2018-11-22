package com.kroyoshi.forum.utils;

import android.content.Context;
import android.graphics.Color;

import com.github.johnpersano.supertoasts.SuperToast;

/**
 * Toast
 */
public class T {
    public static void show(Context context, String text) {
        SuperToast.cancelAllSuperToasts();
        SuperToast superToast = new SuperToast(context.getApplicationContext());
        superToast.setAnimations(SuperToast.Animations.FLYIN);
        superToast.setBackground(SuperToast.Background.ORANGE);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setText(text);
        superToast.show();
    }
}
