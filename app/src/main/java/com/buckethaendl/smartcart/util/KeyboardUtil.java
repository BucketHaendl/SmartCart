package com.buckethaendl.smartcart.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Cedric on 27.07.16.
 */
public class KeyboardUtil {

    /**
     * Tries to hide the keyboard in the given activity
     * @param activity The activity to hide the keyboard from
     * @return if the operation worked or failed
     */
    public static boolean hideKeyboard(Activity activity) {

        try {

            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            return true;

        }

        catch (Exception e) { //ignore

            return false;

        }

    }

}
