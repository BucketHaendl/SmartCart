package com.buckethaendl.smartcart.data.local.file;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Cedric on 20.07.16.
 */
public interface FileInteractable {

    <E extends Serializable> void loadLibrary(Context context);
    <E extends Serializable> void saveLibrary(Context context, E content);

    <E extends Serializable> void loadLibrary(Context context, FileLibraryListener<E> listener);
    <E extends Serializable> void saveLibrary(Context context, E content, FileLibraryListener<E> listener);

}
