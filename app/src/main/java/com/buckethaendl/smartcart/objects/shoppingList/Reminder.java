package com.buckethaendl.smartcart.objects.shoppingList;

import java.io.Serializable;
import java.util.Calendar;

/**
 * An object representing a notification reminder timer.
 * The Reminder keeps the time at which a notification will be sent out to remind the user to go shopping
 * Reminders can be added to shopping lists
 *
 * Created by Cedric on 18.03.2016.
 */
public class Reminder implements Serializable {

    private Calendar date;

    public Reminder() {

        this.date = Calendar.getInstance();

    }

    public Reminder(int hour, int minute) {

        this.date = Calendar.getInstance();

        this.date.set(Calendar.HOUR_OF_DAY, hour);
        this.date.set(Calendar.MINUTE, minute);

    }

    public Reminder(Calendar date) {

        this.date = date;

    }

    //Getters & Setters

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public String toString() {

        return String.format("%d:%d", this.date.get(Calendar.HOUR_OF_DAY), this.date.get(Calendar.MINUTE)); //TODO Hat noch keine Informationen über das Datum (nur Zeit bisher)

    }

}
