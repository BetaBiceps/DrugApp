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
import java.util.HashMap;
import java.util.Map;

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
    int Unique_Notification_Number = 0; // ID for notifications
    final String alertString = "Usage Alert";
    final String doseIncreaseString = "Dose increases are too common, \ntry decreasing dose.";
    final String freqIncreaseString = "Frequency of use is increasing, \ntry to dose less often.";
    final String doseHighString = "Dose is high. Use caution.";
    final String freqHighString = "Dosing again too soon. Use caution.";
    float avgIncreaseLimit = 0.25f; // 25% increase
    float avgFreqLimit = 0.5f; // 2 times as frequent
    int requiredUseCount = 3; // No alerts unless there are enough events
    final int spamCountRequired = 3; // Controls repeating messages
    final float doseLimitChangeIfMarked = 0.8f; // 80% dose limit if the drug is marked
    final float freqLimitChangeIfMarked = 0.85f; // 85% dose limit if the drug is marked
    final float avgIncreaseLimitChangedIfMarked = 0.01f; // changes limits by X if marked
    final HashMap<String,Integer> doseLimit = new HashMap<String, Integer>();//set in constructor
    final HashMap<String,Integer> freqLimit = new HashMap<String, Integer>();//set in constructor
    HashMap<String,Boolean> isMarked = new HashMap<String, Boolean>();//set in constructor

    //TODO add a time frame limit, only look at doses in the past 1 or 2 months


    /****/


    /**
     * Creation of the controller
     * @param context context
     */
    public NotificationController(Context context) {
        mContext = context;
        drugArray = mContext.getResources().getStringArray(R.array.drugs);
        setDefault();
    }


    private void setDefault(){
        doseLimit.clear();
        doseLimit.put("Meth", 50);
        doseLimit.put("Ketamine", 100);
        doseLimit.put("GHB",2000);
        doseLimit.put("Cocaine",500);
        doseLimit.put("Heroin",100);
        doseLimit.put("Marijuana",3000);
        doseLimit.put("MDMA",300);

        freqLimit.clear();
        freqLimit.put("Meth", 7);
        freqLimit.put("Ketamine", 3);
        freqLimit.put("GHB",3);
        freqLimit.put("Cocaine",7);
        freqLimit.put("Heroin",5);
        freqLimit.put("Marijuana",1);
        freqLimit.put("MDMA",30);

        isMarked.clear();
        isMarked.put("Meth", false);
        isMarked.put("Ketamine", false);
        isMarked.put("GHB",false);
        isMarked.put("Cocaine",false);
        isMarked.put("Heroin",false);
        isMarked.put("Marijuana",false);
        isMarked.put("MDMA",false);

        avgIncreaseLimit = 0.15f;
        avgFreqLimit = 0.5f;
    }

    @Override
    public void onReceive(Context context, Intent intent) {}


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

        Unique_Notification_Number++;
        notificationManager.notify(Unique_Notification_Number, mBuilder.build());

    }


    /**
     * Checks all functions and drugs to see if we need to notify the user.
     * @param list the events list
     * @param drugAdded the drug being added
     * @param dose the dose of the drug
     */
    public void notifyCheck(ArrayList<Event> list, String drugAdded, int dose){
        checkMarks(list);


        if (isDoseIncrease(list, drugAdded)) {
            createNotification(mContext, drugAdded, doseIncreaseString, alertString);
        }
        if (isFreqIncrease(list, drugAdded)) {
            createNotification(mContext, drugAdded, freqIncreaseString, alertString);
        }


        if(isDoseHigh(drugAdded, dose)){
            createNotification(mContext, drugAdded, doseHighString, alertString);
        }
        if(isFreqHigh(list, drugAdded)){
            createNotification(mContext, drugAdded, freqHighString, alertString);
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
    public boolean isDoseIncrease(ArrayList<Event> list, String drug){
        int lastBiggest = 0;
        int counter = 0; // number of occurences of the drug
        int totalIncrease = 0;
        int totalDose = 0;

        // Calculate the average increase per use
        for (Event event : list) {
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
                    counter >= (requiredUseCount)) {
                requiredUseCount += spamCountRequired;
                //If the avg increase is to much and the drug is used past the threshold, return true
                isMarked.remove(drug);
                isMarked.put(drug, true);
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
    public boolean isFreqIncrease(ArrayList<Event> list, String drug){
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


        //get the last usage
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

            if (lastFreq <= (avgFreqLimit * avgFreq) &&
                    counter >= (requiredUseCount)) {
                requiredUseCount += spamCountRequired;
                isMarked.remove(drug);
                isMarked.put(drug, true);
                return true;
            }
        }

        return false;
    }

    /**
     * Returns if the dosage is too high
     * @param drug the drug being checked
     * @param dose the dose being checked
     * @return true if higher than limit
     */
    public boolean isDoseHigh(String drug, int dose){
        if(dose >= doseLimit.get(drug)){
            isMarked.remove(drug);
            isMarked.put(drug, true);
            return true;
        }
        return false;
    }

    /**
     * Returns if the last use of a drug is too soon.
     * @param list the list of events
     * @param drug the drug being added
     * @return true if freq too high
     */
    public boolean isFreqHigh(ArrayList<Event> list, String drug){
        //Sort the array for date to be in order
        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event lhs, Event rhs) {
                if(lhs.getDate() == null || rhs.getDate() == null)
                    return 0;
                return lhs.getDate().compareTo(rhs.getDate());  //recent is last
            }
        });


        int size = list.size()-1;
        Date finalDate = list.get(size).getDate();
        Date secondLastDate = list.get(size-1).getDate();
        long lastFreq = finalDate.getTime() - secondLastDate.getTime();
        int lastFreqDays = Math.round((float)lastFreq / (24*1000*60*60));

        if (lastFreqDays < freqLimit.get(drug)){
            return true;
        }
        return false;
    }

    /**
     * Checks to see if any drug is marked for addiction case.
     * If it is, we do a few things to increase the chance of notification.
     * Clears to default every 10 entries
     */
    private void checkMarks(ArrayList<Event> list){
        for(Map.Entry<String, Boolean> entry : isMarked.entrySet()){
            String key = entry.getKey();
            Boolean bool = entry.getValue();
            int current=0;
            int count=0;

            //If the list size is a facotr of 10, we reset all settings.
            if (list.size() % 10 == 0){
                setDefault();
                break;
            }

            //If a drug is marked we make more notifications.
            if (bool == true){
                count++;
                current = doseLimit.get(key);
                doseLimit.remove(key);
                doseLimit.put(key, Math.round(current * doseLimitChangeIfMarked));

                current = freqLimit.get(key);
                freqLimit.remove(key);
                freqLimit.put(key, Math.round(current * freqLimitChangeIfMarked));

                avgIncreaseLimit -= avgIncreaseLimitChangedIfMarked;
                avgFreqLimit += avgIncreaseLimitChangedIfMarked;
            }
        }

    }




}
