package com.buckethaendl.smartcart.objects.shoppingList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import com.buckethaendl.smartcart.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * An object representing a shopping list for SmartCart
 *
 * Created by Cedric on 18.03.2016.
 */
public class ShoppingList implements Serializable {

    public static final long SERIALIZATION_ID = 1L;
    private static transient Resources resources;

    private ShoppingListState listState; //TODO not implemented by now --> do so + implement in constructors

    private String name;
    private Calendar date;
    private int colorId;
    private List<ShoppingListItem> items;
    private List<Reminder> reminders;
    private List<Contact> sharedContacts;

    public static void setStaticResources(Resources res) {

        ShoppingList.resources = res;

    }

    public static Resources getStaticResources() {

        return ShoppingList.resources;

    }

    public ShoppingList(String name, Calendar date) {

        this(name, date, android.R.color.white);

    }

    public ShoppingList(String name, int hour, int minute) {

        this(name, null, android.R.color.white);

        this.setDate(hour, minute);

    }

    public ShoppingList(String name, Calendar date, int colorId) {

        this(name, date, colorId, new ArrayList<ShoppingListItem>());

    }

    public ShoppingList(String name, Calendar date, int colorId, List<ShoppingListItem> items) {

        this(name, date, colorId, items, new ArrayList<Reminder>(), new ArrayList<Contact>());

    }

    public ShoppingList(String name, Calendar date, int colorId, List<ShoppingListItem> items, List<Reminder> reminders, List<Contact> sharedContacts) {

        this.name = name;
        this.date = date;
        this.colorId = colorId;
        this.items = items;
        this.reminders = reminders;
        this.sharedContacts = sharedContacts;

        this.listState = ShoppingListState.DONE; //TODO remove and program better!! this is just for testing (constant setting)


    }

    public ShoppingListState getListState() {
        return listState;
    }

    public void setListState(ShoppingListState listState) {
        this.listState = listState;
    }

    //Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getDate() {
        return date;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    /**
     * @return a formated date as it can be used for the shopping_list_overview_listitem_date_textview
     */
    public String getDateFormatted() {

        if(resources == null) throw new NotInitializedException("The static resources object of the ShoppingList.class was never set!");

        Calendar current = Calendar.getInstance();

        if(current.get(Calendar.DAY_OF_YEAR) == this.date.get(Calendar.DAY_OF_YEAR)) { //when created on the same day (today)

            return resources.getString(R.string.shopping_list_overview_listitem_date_today);

        }

        else if((current.get(Calendar.DAY_OF_YEAR) - this.date.get(Calendar.DAY_OF_YEAR)) == 1) { //when created one day back (yesterday)

            return resources.getString(R.string.shopping_list_overview_listitem_date_yesterday);

        }

        else if(current.get(Calendar.YEAR) == this.date.get(Calendar.YEAR)){

            int dayOfMonth = this.date.get(Calendar.DAY_OF_MONTH);
            int month = this.date.get(Calendar.MONTH);

            return String.format("%2d.%2d.", dayOfMonth, month); //TODO Watch out! Ist im amerikanischen raum nicht MM/DD/YYYY statt DD/MM/YYYY üblich?!

        }

        else {

            int dayOfMonth = this.date.get(Calendar.DAY_OF_MONTH);
            int month = this.date.get(Calendar.MONTH);
            int year = this.date.get(Calendar.YEAR);

            return String.format("%2d.%2d.%2d", dayOfMonth, month, year); //TODO Watch out! Ist im amerikanischen raum nicht MM/DD/YYYY statt DD/MM/YYYY üblich?! Dann braucht man einen Format String je nach locale!

        }

    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setDate(int hour, int minute) {

        this.date = Calendar.getInstance();

        this.date.set(Calendar.HOUR_OF_DAY, hour);
        this.date.set(Calendar.MINUTE, minute);

    }

    //ShoppingListItems

    public ShoppingListItem getItem(int index) {

        if(this.items.get(index) == null) throw new ArrayIndexOutOfBoundsException("The list " + this.getName() + " doesn't have item entry " + index);

        else return this.items.get(index);

    }

    public void addItem(ShoppingListItem item) {

        this.items.add(item);

    }

    public boolean removeItem(int index) {

        if(this.items.get(index) == null) throw new ArrayIndexOutOfBoundsException("The list " + this.getName() + " doesn't have item entry " + index);

        else return (this.items.remove(index) != null);

    }

    public boolean removeItem(ShoppingListItem item) {

        if(!this.items.contains(item)) throw new IllegalArgumentException("The item " + item + " isn't contained in " + this.getName());

        else return this.items.remove(item);

    }

    public List<ShoppingListItem> getItems() {

        return this.items;

    }

    public int getItemsCount() {

        return this.items.size();

    }

    //Reminders

    public Reminder getReminder(int index) {

        if(this.reminders.get(index) == null) throw new ArrayIndexOutOfBoundsException("The list " + this.getName() + " doesn't have reminder entry " + index);

        else return this.reminders.get(index);

    }

    public void addReminder(Reminder reminder) {

        this.reminders.add(reminder);

    }

    public boolean removeReminder(int index) {

        if(this.items.get(index) == null) throw new ArrayIndexOutOfBoundsException("The list " + this.getName() + " doesn't have reminder entry " + index);

        else return (this.reminders.remove(index) != null);

    }

    public boolean removeReminder(Reminder reminder) {

        if(!this.reminders.contains(reminder)) throw new IllegalArgumentException("The reminder " + reminder + " isn't contained in " + this.getName());

        else return this.reminders.remove(reminder);

    }

    public List<Reminder> getReminders() {

        return this.reminders;

    }

    //SharedContacts

    public Contact getSharedContact(int index) {

        if(this.sharedContacts.get(index) == null) throw new ArrayIndexOutOfBoundsException("The list " + this.getName() + " doesn't have sharedContact entry " + index);

        else return this.sharedContacts.get(index);

    }

    public void addSharedContact(Contact sharedContact) {

        this.sharedContacts.add(sharedContact);

    }

    public boolean removeSharedContact(int index) {

        if(this.sharedContacts.get(index) == null) throw new ArrayIndexOutOfBoundsException("The list " + this.getName() + " doesn't have sharedContact entry " + index);

        else return (this.sharedContacts.remove(index) != null);

    }

    public boolean removeSharedContact(Contact sharedContact) {

        if(!this.sharedContacts.contains(sharedContact)) throw new IllegalArgumentException("The sharedContact " + sharedContact + " isn't contained in " + this.getName());

        else return this.sharedContacts.remove(sharedContact);

    }

    public List<Contact> getSharedContacts() {

        return this.sharedContacts;

    }

    @Override
    public String toString() {

        return String.format("[List Info] Name: %s, Date: %s, Color: %d, Items: %s, Reminders: %s, SharedContacts: %s", name, getDateFormatted(), this.colorId, items.toString(), reminders.toString(), sharedContacts.toString());

    }

    /**
     * The type of the shopping list
     * Mainly determines the shown icon of the list and other functions
     */
    public enum ShoppingListState {

        DEFAULT,
        URGENT,
        SHARED,
        DONE;

        //TODO implement

        public int getIconResourceId() {

            return android.R.color.transparent; //TODO change to real symbol

        }

    }

}
