package com.example.liuwd8.healthfood;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class DynamicReceiver extends BroadcastReceiver {
    private static final String DYNAMICACTION = "com.example.liuwd8.MyDynamicFilter";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DYNAMICACTION)) {
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setAction("Detail collection");
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            //对Builder进行配置，此处仅选取了几个
            builder.setContentTitle("已收藏")   //设置通知栏标题：发件人
                    .setContentText(intent.getStringExtra("foodName"))   //设置通知栏显示内容：短信内容
                    .setTicker("您有一条新消息")   //通知首次出现在通知栏，带上升动画效果的
                    .setSmallIcon(R.mipmap.empty_star)   //设置通知小ICON（通知栏），可以用以前的素材，例如空星
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);

            String id = "notificationId_1";
            String name="notificationName_1";

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{0,100,0});
                manager.createNotificationChannel(mChannel);
                builder.setChannelId(id);
                notify = builder.build();
            } else {
                notify = builder.build();
            }
            notify.flags =  Notification.FLAG_AUTO_CANCEL;
            manager.notify(0, notify);
        }
    }
}

