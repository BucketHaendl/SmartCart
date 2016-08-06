package com.buckethaendl.smartcart.data.local.sqlite;

import com.buckethaendl.smartcart.data.local.LibraryListener;
import com.buckethaendl.smartcart.data.service.FBBShelf;
import com.buckethaendl.smartcart.objects.instore.Shelf;

/**
 * Created by Cedric on 27.07.16.
 */
public interface SQLiteShelfInteractable {

    Shelf loadShelfSync(FBBShelf rawShelf);
    void loadShelfAsync(FBBShelf rawShelf, LibraryListener<Shelf> listener);

}
