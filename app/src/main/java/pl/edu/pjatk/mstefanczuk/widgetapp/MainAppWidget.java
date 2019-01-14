package pl.edu.pjatk.mstefanczuk.widgetapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MainAppWidget extends AppWidgetProvider {

    public static String ACTION_WIDGET_URL = "ActionReceiverUrl";
    public static String ACTION_WIDGET_IMAGE = "ActionReceiverImage";
    public static String ACTION_WIDGET_AUDIO = "ActionReceiverAudio";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_app_widget);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
        PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.urlButton, actionPendingIntent);

        intent = new Intent(context, MainAppWidget.class);
        intent.setAction(ACTION_WIDGET_IMAGE);
        actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.imageButton, actionPendingIntent);

        intent = new Intent(context, MainAppWidget.class);
        intent.setAction(ACTION_WIDGET_AUDIO);
        actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.audioButton, actionPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean img1 = sharedPreferences.getBoolean("img1", false);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_app_widget);

        if (intent.getAction().equals(ACTION_WIDGET_IMAGE)) {
            if (!img1) {
                views.setImageViewResource(R.id.imageView, R.drawable.img1);
            } else {
                views.setImageViewResource(R.id.imageView, R.drawable.img2);
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("img1", !img1);
            editor.apply();
        } else if (intent.getAction().equals(ACTION_WIDGET_AUDIO)) {
            Log.i("onReceive", ACTION_WIDGET_AUDIO);
        }

        appWidgetManager.updateAppWidget(new ComponentName(context, MainAppWidget.class), views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
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
}

