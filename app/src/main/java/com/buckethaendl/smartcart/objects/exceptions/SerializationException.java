package com.buckethaendl.smartcart.objects.exceptions;

/**
 * Created by Cedric on 21.03.2016.
 */
public class SerializationException extends Exception {

    public SerializationException() {

        super("The (de-)serialization of one or more objects didn't work and the file might be corrupt");

    }

    public SerializationException(String msg) {

        super(msg);

    }

}
