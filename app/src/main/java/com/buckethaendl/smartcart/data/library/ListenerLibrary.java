package com.buckethaendl.smartcart.data.library;

import java.util.List;

/**
 * Created by Cedric on 20.07.16.
 */
public interface ListenerLibrary extends Library {

    void addListener(LibraryRefreshListener listener);
    void removeListener(LibraryRefreshListener listener);

    List<LibraryRefreshListener> getListeners();
    void setListeners(List<LibraryRefreshListener> listeners);

    void notifyListeners();

}
