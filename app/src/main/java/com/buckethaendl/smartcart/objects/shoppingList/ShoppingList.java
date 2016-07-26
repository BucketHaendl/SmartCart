package com.buckethaendl.smartcart.objects.shoppinglist;

import com.buckethaendl.smartcart.App;
import com.buckethaendl.smartcart.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * An object representing a shopping list for SmartCart
 *
 * Created by Cedric on 18.03.2016.
 */
public class ShoppingList extends ArrayList<ShoppingListItem> implements Serializable {

    public transient static final long serialVersionUID = 1L;

    private String name;
    private Calendar date;
    private int iconId; //todo can be changed to icons or something else...
    private boolean done;

    public ShoppingList(String name, Calendar date) {

        this(name, date, android.R.color.white);

    }

    public ShoppingList(String name, int hour, int minute) {

        this(name, null, R.drawable.apple_icon);
        this.setDate(hour, minute);

    }

    public ShoppingList(String name, Calendar date, int iconId) {

        this(name, date, iconId, null);

    }

    public ShoppingList(String name, Calendar date, int iconId, List<ShoppingListItem> items) {

        this(name, date, iconId, items, false);

    }

    public ShoppingList(String name, Calendar date, int iconId, List<ShoppingListItem> items, boolean done) {

        super();

        this.name = name;
        this.date = date;
        this.done = done;
        this.iconId = iconId;

        if(items != null) this.addAll(items);

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

    /**
     * Gets the date of the list in a readable form of one of the following:
     * > today
     * > yesterday
     * > XX.YY.
     * > XX.YY.ZZZZ
     * Can be used for the shopping_list_overview_listitem_date_textview
     * @return a formatted string showing date information
     */
    public String getDateFormatted() {

        Calendar current = Calendar.getInstance();

        if(current.get(Calendar.DAY_OF_YEAR) == this.date.get(Calendar.DAY_OF_YEAR)) { //when created on the same day (today)

            return App.getGlobalResources().getString(R.string.shopping_list_overview_listitem_date_today);

        }

        else if((current.get(Calendar.DAY_OF_YEAR) - this.date.get(Calendar.DAY_OF_YEAR)) == 1) { //when created one day back (yesterday)

            return App.getGlobalResources().getString(R.string.shopping_list_overview_listitem_date_yesterday);

        }

        else {

            int dayOfMonth = this.date.get(Calendar.DAY_OF_MONTH);
            int month = this.date.get(Calendar.MONTH);
            int year = this.date.get(Calendar.YEAR);

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy", Locale.GERMANY); //todo set locale depending on location (US, ...)

            return format.format(current.getTime());

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

    public boolean isDone() {
        return this.done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    //ShoppingListItems
    /* todo was removed bc. this is now the list ITSELF (this object)
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

    }*/

    //Reminders
    /*
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

    }*/

    //SharedContacts
    /*
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

    }*/

    @Override
    public String toString() {

        return String.format(Locale.GERMANY, "[List Info] Name: %s, Date: %s, Color: %d, Items: %s", name, getDateFormatted(), this.iconId, this.toArray().toString());

    }

}
