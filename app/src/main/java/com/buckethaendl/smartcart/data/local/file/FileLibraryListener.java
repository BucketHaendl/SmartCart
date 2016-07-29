package com.buckethaendl.smartcart.data.local.file;

import java.io.Serializable;

/**
 * Created by Cedric on 20.07.16.
 */
public interface FileLibraryListener<E extends Serializable> {

    void onOperationStarted();
    void onOperationFinished();

    void onLoadResult(E result); //pass loaded result
    void onSetInitialized(boolean initialized);

}
