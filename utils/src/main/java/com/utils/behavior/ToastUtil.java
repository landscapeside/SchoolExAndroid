package com.utils.behavior;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

	public static void show(Context context, int resId) {
		show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
	}

	public static void show(Context context, int resId, int duration) {
		show(context, context.getResources().getText(resId), duration);
	}

	public static void show(Context context, CharSequence text) {
		show(context, text, Toast.LENGTH_SHORT);
	}

	public static void show(Context context, int resId, Object... args) {
		show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
	}

	public static void show(Context context, String format, Object... args) {
		show(context, String.format(format, args), Toast.LENGTH_SHORT);
	}

	public static void show(Context context, int resId, int duration, Object... args) {
		show(context, String.format(context.getResources().getString(resId), args), duration);
	}

	public static void show(Context context, String format, int duration, Object... args) {
		show(context, String.format(format, args), duration);
	}

	public static void show(Context context, CharSequence text, int duration) {
		showSigle(context, text, duration);
	}

	private static Toast mToast;

	private static void showSigle(Context context, CharSequence text, int duration) {
		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(context, text, duration);
		mToast.show();
	}
}
