package com.example.chris.drugapp;

import android.content.Context;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Chris on 01/02/2016.
 */
public class Event implements Serializable {
    /**@serial*/
    public String time;
    /**@serial*/
    public String drug;
    /**@serial*/
    public Date date;
    /**@serial*/
    public int dose;

    SimpleDateFormat format = new SimpleDateFormat("MM-dd");

    public Event(Date date, String time, String drug, int dose){
        this.time = time;
        this.drug = drug;
        this.date = date;
        this.dose = dose;
    }

    public String getDrug(){
        return this.drug;
    }
    public Date getDate(){
        return this.date;
    }
    public String dateTostring(){
        String x = null;
        try {
            x = format.format(date);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return x;
    }
    /**
    * @throws IllegalArgumentException if any field takes an unpermitted value.
            */
    private void validateState() {
        validateInt(dose);
        validateString(time);
        validateString(drug);
        validateDate(date);
    }

    /**
     * Ensure names contain only letters, spaces, and apostrophes.
     *
     * @throws IllegalArgumentException if field takes an unpermitted value.
     */
    private void validateString(String s){
        boolean nameHasContent = (s != null) && (!s.equals(""));
        if (!nameHasContent){
            throw new IllegalArgumentException("Names must be non-null and non-empty.");
        }
        StringCharacterIterator iterator = new StringCharacterIterator(s);
        char character = iterator.current();
        while (character != StringCharacterIterator.DONE){
            boolean isValidChar =
                    (Character.isLetter(character) ||
                            Character.isSpaceChar(character) ||
                            character =='\''
                    );
            if (isValidChar) {
                //do nothing
            }
            else {
                String message = "Names can contain only letters, spaces, and apostrophes.";
                throw new IllegalArgumentException(message);
            }
            character = iterator.next();
        }
    }

    /**
     * AccountNumber must be non-negative.
     * @throws IllegalArgumentException if field takes an unpermitted value.
     */
    private void validateInt(int i){
        if (i < 0) {
            String message = "Account Number must be greater than or equal to 0.";
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Date must be after 1970.
     * @throws IllegalArgumentException if field takes an unpermitted value.
     */
    private void validateDate(Date date) {
        if(date.getTime() < 0) {
            throw new IllegalArgumentException("Date Opened must be after 1970.");
        }
    }


    protected void readObject(ObjectInputStream objectInputStream)
    throws ClassNotFoundException, IOException{
        objectInputStream.defaultReadObject();
        date = new Date(date.getTime());

        validateState();
    }

    protected void writeObject(ObjectOutputStream aOutputStream)
        throws IOException{
        aOutputStream.defaultWriteObject();
    }



    public void save(String filename, Event theObject,
                            Context ctx) {
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = ctx.openFileOutput(filename, Context.MODE_APPEND);
            oos = new ObjectOutputStream(fos);

            theObject.writeObject(oos);
            oos.close();
            fos.close();

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Event> readFile(String filename, Context ctx) {
        FileInputStream fis;
        ObjectInputStream ois;
        ArrayList<Event> ev = new ArrayList<Event>();

        try {
            fis = ctx.openFileInput(filename);
            ois = new ObjectInputStream(fis);

            while(true) {
                try{
                    this.readObject(ois);
                    ev.add(this);
                } catch (NullPointerException | EOFException e){
                    e.printStackTrace();
                    ois.close();
                    break;
                }
            }
            ois.close();
            fis.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return ev;



    }

}
