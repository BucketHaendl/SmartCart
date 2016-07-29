package com.buckethaendl.smartcart.data.local.sqlite;

import com.buckethaendl.smartcart.data.local.LibraryListener;
import com.buckethaendl.smartcart.data.service.WaSaFBBShelf;
import com.buckethaendl.smartcart.objects.instore.Shelf;

/**
 * Created by Cedric on 27.07.16.
 */
public interface SQLiteShelfInteractable {

    void loadShelfAsync(WaSaFBBShelf rawShelf, LibraryListener<Shelf> listener);

}
