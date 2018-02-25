package com.kania.hyeget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Implementation of App Widget functionality.
 */
public class HyegetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //TODO implement follow setImageToWidget()
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String keyWithId = HyegetConstants.KEY_IMAGE_URI_PREFIX + appWidgetId;
        String savedUriString = preferences.getString(keyWithId, null);
        if (savedUriString != null) {
            Log.d("hyeget_log", "saved uri = " + savedUriString);
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.hyeget_provider);
//            Bitmap bitmap = ImageUtils.getResizedBitmapIfNeed(context, Uri.parse(savedUriString));
//            views.setImageViewBitmap(R.id.widget_image_target, bitmap);
//            appWidgetManager.updateAppWidget(appWidgetId, views);
        } else {
            Log.d("hyeget_log", "no saved uri");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        //TODO how i update on reboot for saved image
        Log.d("hyeget_log", "onUpdate() called!");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        //TODO implement follow setImageToWidget()
        /*
        for (int id : appWidgetIds)
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            String keyWithId = HyegetConstants.KEY_IMAGE_URI_PREFIX + id;
            editor.remove(keyWithId);
            Log.d("hyeget_log", "remove preference key : " + keyWithId);
        }
        */
        super.onDeleted(context, appWidgetIds);
    }
}

