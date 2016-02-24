package com.example.chris.drugapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

/**
 * This class is responsible for determining when to alert users.
 *
 * Created by Chris on 23/02/2016.
 */
public class NotificationController extends BroadcastReceiver{
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert){
        PendingIntent notiIntent = PendingIntent.getActivity(context, 0, new
                Intent(context, Home.class),0);

        NotificationCompat.Builder mBuilder = new
                NotificationCompat.Builder(context)
                .setContentTitle(msg)
                .setContentText(msgText)
                .setTicker(msgAlert)
                .setSmallIcon(R.drawable.right_arrow);

        mBuilder.setContentIntent(notiIntent);

        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, mBuilder.build());

    }




}
