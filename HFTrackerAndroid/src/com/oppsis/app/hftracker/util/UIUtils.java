package com.oppsis.app.hftracker.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

public class UIUtils {

	public static final float SESSION_BG_COLOR_SCALE_FACTOR = 0.65f;
	public static final float SESSION_PHOTO_SCRIM_ALPHA = 0.75f;

	public static int setColorAlpha(int color, float alpha) {
		int alpha_int = Math.min(Math.max((int) (alpha * 255.0f), 0), 255);
		return Color.argb(alpha_int, Color.red(color), Color.green(color),
				Color.blue(color));
	}

	public static int scaleColor(int color, float factor, boolean scaleAlpha) {
		return Color.argb(
				scaleAlpha ? (Math.round(Color.alpha(color) * factor)) : Color
						.alpha(color), Math.round(Color.red(color) * factor),
				Math.round(Color.green(color) * factor), Math.round(Color
						.blue(color) * factor));
	}

	public static <T extends View> List<T> getViewsByTag(ViewGroup root,
			String tag, Class<T> clazz) {
		List<T> views = new ArrayList<T>();
		final int childCount = root.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final T child = (T) root.getChildAt(i);
			if (child instanceof ViewGroup) {
				views.addAll(getViewsByTag((ViewGroup) child, tag, clazz));
			}

			final Object tagObj = child.getTag();
			if (tagObj != null && tagObj.toString().contains(tag)
					&& (clazz.isInstance(child))) {
				views.add(child);
			}

		}
		return views;
	}

	public static Bitmap takeScreenShot(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay().getHeight();

		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}
}
