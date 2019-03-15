package com.example.liuwd8.healthfood;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class HealthFoodWidget extends AppWidgetProvider {
    private static final String WIDGETSTATICACTION = "com.example.liuwd8.MyWidgetStaticFilter";
    Bundle bundle;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews updateView = new RemoteViews(context.getPackageName(), R.layout.health_food_widget);//实例化RemoteView,其对应相应的Widget布局
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction("Widget update");
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        updateView.setOnClickPendingIntent(R.id.widget_image, pi); //设置点击事件
        ComponentName me = new ComponentName(context, HealthFoodWidget.class);
        appWidgetManager.updateAppWidget(me, updateView);
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && intent.getAction().equals(WIDGETSTATICACTION)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            bundle = intent.getExtras();
            if (bundle != null) {
                RemoteViews updateView = new RemoteViews(context.getPackageName(), R.layout.health_food_widget);

                updateView.setTextViewText(R.id.appwidget_text, "今日推荐 " + bundle.getString("foodName"));

                Intent intent1 = new Intent(context, DetailActivity.class);
                intent1.putExtras(bundle);
                PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                updateView.setOnClickPendingIntent(R.id.widget_image, pi); //设置点击事件

                ComponentName me = new ComponentName(context, HealthFoodWidget.class);
                appWidgetManager.updateAppWidget(me, updateView);
            }
        }
        super.onReceive(context, intent);
    }
}

