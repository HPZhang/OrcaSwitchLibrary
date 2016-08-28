package com.orcanote.orcaswitchlibrary.ui.activity;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private ToastUtils() {
    }

    public static void showMessage(Context context, String message) {
        getToast(context, message).show();
    }

    public static void showMessage(Context context, int resId) {
        showMessage(context, context.getString(resId));
    }

    private static Toast getToast(Context context, String message) {
        return getToast(context, message, Toast.LENGTH_LONG);
    }

    private static Toast getToast(Context context, String message, int length) {
        return Toast.makeText(context, message, length);
    }
}
