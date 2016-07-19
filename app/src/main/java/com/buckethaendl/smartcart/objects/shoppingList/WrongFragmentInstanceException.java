package com.buckethaendl.smartcart.objects.shoppingList;

/**
 * Created by Cedric on 22.03.2016.
 */
public class WrongFragmentInstanceException extends RuntimeException {

    public WrongFragmentInstanceException() {

        super("The fragment is not instance of a valid type the application is trying to work with");

    }

    public WrongFragmentInstanceException(String message) {

        super(message);

    }

}
