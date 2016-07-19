package com.buckethaendl.smartcart.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.buckethaendl.smartcart.R;

/**
 * Created by Cedric on 21.03.2016.
 */
public class DialogBuildingSite {

    private DialogBuildingSite() {

    }

    public static AlertDialog buildErrorDialog(Context context, String errorMessage) {

        String title = context.getString(R.string.error_dialog_error_title);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(android.R.drawable.stat_notify_error)
                .setMessage(errorMessage)
                .setCancelable(false)
                .setNegativeButton(R.string.error_dialog_ignore, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }

                })
                .create();

        return dialog;

    }


}
