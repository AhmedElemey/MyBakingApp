package com.example.ahmed.mybakingapp.Widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.ahmed.mybakingapp.Activity.MainActivity;
import com.example.ahmed.mybakingapp.Adapter.RecipeAdapter;
import com.example.ahmed.mybakingapp.R;

import java.util.ArrayList;

public class IngredientWidgetProvider extends AppWidgetProvider {

    public static final String TAG = IngredientWidgetProvider.class.getSimpleName();
    public static final String RECIPE_INDEX_EXTRA = "recipe_index_extra";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_provider);

        int recipeId = PreferenceManager.getDefaultSharedPreferences(context).getInt(RECIPE_INDEX_EXTRA, 0);

        Log.d(TAG, "updateAppWidget: " + recipeId);
        Intent listIntent = new Intent(context, WidgetListService.class);
        listIntent.putExtra(WidgetListService.EXTRA_WIDGET_RECIPE_ID, recipeId);

        views.setRemoteAdapter(R.id.widget_ingredient_list, listIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredient_list);
                  ////////////// 1
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_provider);
//        views.setOnClickPendingIntent(R.id.nn , pendingIntent);
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);

        ////////////// 2
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_provider);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            setRemoteAdapter(context, views);
//        }
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
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

//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
//        views.setRemoteAdapter(R.id.widget_list, new Intent(context, WidgetService.class));
//    }

}

