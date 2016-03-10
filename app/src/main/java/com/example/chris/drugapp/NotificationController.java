package com.example.chris.drugapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * This class is responsible for determining when to alert users.
 *
 * Created by Chris on 23/02/2016.
 */
public class NotificationController extends BroadcastReceiver{
    NotificationManager notificationManager;
    Context mContext;
    String[] drugArray;

    // These are used to control the frequency of notifying.
    int doseIncraseSpamCount = 0;
    int freqIncraseSpamCount = 0;

    /**
     * DEFINED VALUES
     */
    final String alertString = "Usage Alert";
    final String doseIncreaseString = "Dose increases are too common, try decreasing dose.";
    final String freqIncreaseString = "Frequency of use is increasing, try to dose less often.";
    final float avgIncreaseLimit = 0.1f; // 10% increase
    final float avgFreqLimit = 0.5f; // 2 times as frequent
    final int requiredUseCount = 3; // No alerts unless there are enough events
    final int spamCountRequired = 3;

    //TODO add a time frame limit, only look at doses in the past 1 or 2 months


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
            if (isFreqIncrease(list, drug)) {
                createNotification(mContext, drug, freqIncreaseString, alertString);
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
            if (avgIncrease > (avgIncreaseLimit * avgDose) &&
                    counter >= (requiredUseCount+doseIncraseSpamCount)) {
                doseIncraseSpamCount += spamCountRequired;
                //If the avg increase is to much and the drug is used past the threshold, return true
                return true;
            }
        }

        return false;
    }

    /**
     * Checks to see if the frequency of use has been increasing
     *
     * If the final 2 uses time distance is less than the frequency limit(based on avg freq)
     * then it will return true. Assuming the uses limit has been past.
     *
     * @param list the list of events
     * @param drug the drug to check
     * @return yes or no
     */
    private boolean isFreqIncrease(ArrayList<Event> list, String drug){
        Date lastDate = null;
        int counter = 0; // number of occurrences of the drug
        int totalDuration =0;


        //Sort the array for date to be in order
        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event lhs, Event rhs) {
                if(lhs.getDate() == null || rhs.getDate() == null)
                    return 0;
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });


        for(Event event : list){
            if(drug.equals(event.getDrug())) { //check the right drug
                counter++;
                if (lastDate != null) {
                    totalDuration += event.getDate().compareTo(lastDate);
                }
                lastDate = event.getDate();
            }
        }

        int size = list.size()-1;
        if (counter > 0 && size > 2) {
            Date finalDate = list.get(size).getDate();
            Date secondLastDate = list.get(size-1).getDate();
            double avgFreq = totalDuration / counter;
            double lastFreq = finalDate.compareTo(secondLastDate);

            if (lastFreq < (avgFreqLimit * avgFreq) &&
                    counter >= (requiredUseCount+freqIncraseSpamCount)) {
                freqIncraseSpamCount += spamCountRequired;
                return true;
            }
        }

        return false;
    }


}
