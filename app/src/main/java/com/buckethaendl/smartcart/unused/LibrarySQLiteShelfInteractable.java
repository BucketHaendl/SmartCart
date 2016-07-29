package com.buckethaendl.smartcart.unused;

import android.content.Context;

import com.buckethaendl.smartcart.data.local.LibraryListener;
import com.buckethaendl.smartcart.objects.instore.Shelf;

import java.util.List;

/**
 * Created by Cedric on 27.07.16.
 */
public interface LibrarySQLiteShelfInteractable {

    void loadLibrary(Context context);
    void loadLibrary(Context context, LibraryListener<List<Shelf>> listener);

}
