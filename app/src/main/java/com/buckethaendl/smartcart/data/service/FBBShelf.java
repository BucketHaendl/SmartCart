package com.buckethaendl.smartcart.data.service;

/**
 * A very general implementation of an FBB shelf
 * The only information it is holding is the FBB number of the shelf
 *
 * Created by Cedric on 04.08.16.
 */
public class FBBShelf {

    protected String fbbNr;

    public FBBShelf(String fbbNr) {

        this.fbbNr = fbbNr;

    }

    public String getFbbNr() {
        return fbbNr;
    }
}
