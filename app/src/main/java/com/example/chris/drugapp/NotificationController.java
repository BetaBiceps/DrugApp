package com.example.chris.drugapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

/**
 * This class is responsible for determining when to alert users.
 *
 * Created by Chris on 23/02/2016.
 */
public class NotificationController extends BroadcastReceiver{
    NotificationManager notificationManager;
    Context mContext;
    String[] drugArray;

    /**
     * DEFINED VALUES
     */
    final String alertString = "Usage Alert";
    final String doseIncreaseString = "Dose increases are too common, try decreasing dose.";
    final float avgIncreaseLimit = 0.1f;
    final int requiredUseCount = 3;

    /****/


    /**
     * Creation of the controller
     * @param context context
     */
    public NotificationController(Context context) {
        mContext = context;
        drugArray = mContext.getResources().getStringArray(R.array.drugs);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }


    /**
     * The function to send a notification to the user
     * @param context context
     * @param msg the message title
     * @param msgText the body
     * @param msgAlert the alert in the header
     */
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


    /**
     * Checks all functions and drugs to see if we need to notify the user.
     * @param list the events list
     */
    public void notifyCheck(ArrayList<Event> list){
        for (String drug : drugArray) {
            if (isDoseIncrease(list, drug)) {
                createNotification(mContext, drug, doseIncreaseString, alertString);
            }
        }
    }

    /**
     * Checks to see if the doses have been increasing each time
     * If the average use increases by more than a set rate, and the count of
     * uses is more than the lower limit, return true.
     *
     * @param list the list of events
     * @param drug the drug to check
     * @return yes or no
     */
    private boolean isDoseIncrease(ArrayList<Event> list, String drug){
        int lastBiggest = 0;
        int counter = 0; // number of occurences of the drug
        int totalIncrease = 0;
        int totalDose = 0;

        // Calculate the average increase per use
        for (Event event : list){
            if(drug.equals(event.getDrug())){ //check the right drug
                counter++;
                totalDose += event.getDose();
                if (event.getDose() >= lastBiggest){
                    totalIncrease += event.getDose() - lastBiggest;
                    lastBiggest = event.getDose();
                } else{
                    totalIncrease -= event.getDose() - lastBiggest;
                }
            }
        }

        if (counter > 0) {
            double avgIncrease = totalIncrease / counter;
            double avgDose = totalDose / counter;
            if (avgIncrease > (avgIncreaseLimit * avgDose) && counter >= requiredUseCount) {
                //If the avg increase is to much and the drug is used past the threshold, return true
                return true;
            }
        }

        return false;
    }


}
