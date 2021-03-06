package com.buckethaendl.smartcart.data.local;

/**
 * Created by Cedric on 27.07.16.
 */
public interface LibraryListener<E> {

    void onOperationStarted();
    void onOperationFinished();

    void onLoadResult(E result); //pass loaded result
    void onSetInitialized(boolean initialized);

}
