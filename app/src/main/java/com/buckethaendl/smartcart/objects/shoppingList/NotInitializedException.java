package com.buckethaendl.smartcart.objects.shoppingList;

import android.widget.ImageView;

/**
 * Created by Cedric on 19.03.2016.
 */
public class NotInitializedException extends RuntimeException {

    public NotInitializedException() {

        super("The component was not initialized via it's serialization / load method yet");

    }

    public NotInitializedException(String message) {

        super(message);

    }

}
