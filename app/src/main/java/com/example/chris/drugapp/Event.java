package com.example.chris.drugapp;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;

/**
 * An event is a data set that is used for when the user wants
 * to save an event in the calendar.
 *
 * Created by Chris on 01/02/2016.
 */
public class Event implements Serializable {

    public static final long serialVersionUID = -29238982928391l;

    public String time;
    public String drug;
    public Date date;
    public int dose;

    SimpleDateFormat format = new SimpleDateFormat("MM-dd");

    public Event(Date date, String time, String drug, int dose){
        this.time = time;
        this.drug = drug;
        this.date = date;
        this.dose = dose;
    }

    public String getDrug(){
        return drug;
    }
    public Date getDate(){
        return date;
    }
    public int getDose(){
        return dose;
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
     * validates all the info for corruction
     * @throws IllegalArgumentException if any field takes an unpermitted value.
     */
    private void validateState() {
        validateInt(dose);
        //validateString(time);
        //validateString(drug);
        validateDate(date);
    }

    /**
     * Ensure string contain only letters, spaces, and apostrophes
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
     * Ints must be non-negative.
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


    /**
     * Reads the data from the stream.
     * @param objectInputStream stream to be read from
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void readObject(ObjectInputStream objectInputStream)
    throws ClassNotFoundException, IOException{
        objectInputStream.defaultReadObject();
        date = new Date(date.getTime());

        validateState();
    }

    /**
     * Writes data to the stream
     * @param aOutputStream the stream
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream aOutputStream)
        throws IOException{
        aOutputStream.defaultWriteObject();
    }



}
