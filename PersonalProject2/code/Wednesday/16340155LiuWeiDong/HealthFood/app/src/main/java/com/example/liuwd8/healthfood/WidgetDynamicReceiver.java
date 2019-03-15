package com.example.liuwd8.healthfood;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class WidgetDynamicReceiver extends BroadcastReceiver {
    private static final String WIDGETDYNAMICACTION = "com.example.hasee.myapplication2.MyWidgetDynamicFilter";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(WIDGETDYNAMICACTION)){
            Bundle bundle = intent.getExtras();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (bundle != null) {
                RemoteViews updateView = new RemoteViews(context.getPackageName(), R.layout.health_food_widget);

                updateView.setTextViewText(R.id.appwidget_text, "已收藏 " + bundle.getString("foodName"));

                Intent intent1 = new Intent(context, MainActivity.class);
                intent1.setAction("Widget collection");
                PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                updateView.setOnClickPendingIntent(R.id.widget_image, pi); //设置点击事件

                ComponentName me = new ComponentName(context, HealthFoodWidget.class);
                appWidgetManager.updateAppWidget(me, updateView);
            }
        }
    }
}
