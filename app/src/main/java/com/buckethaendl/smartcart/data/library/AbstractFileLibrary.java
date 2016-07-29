package com.buckethaendl.smartcart.data.library;

import com.buckethaendl.smartcart.data.local.file.FileInteractable;

import java.util.ArrayList;

/**
 * A generall abstract library.
 * An abstract library is an ArrayList in the background
 * TODO not used by now!
 *
 * Created by Cedric on 25.07.16.
 */
public abstract class AbstractFileLibrary<E> extends ArrayList<E> implements Library {

    public static final String TAG = AbstractFileLibrary.class.getName();

    private static AbstractFileLibrary instance;

    private FileInteractable file;
    private boolean listsInitialized = false;

    public AbstractFileLibrary(FileInteractable file) {

        this.file = file;

    }

}
