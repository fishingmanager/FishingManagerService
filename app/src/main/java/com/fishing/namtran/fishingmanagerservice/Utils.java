package com.fishing.namtran.fishingmanagerservice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

/**
 * Created by nam.tran on 11/1/2017.
 */

public class Utils {

    public static void Alert(Context context, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void Redirect(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        //EditText editText = (EditText) findViewById(R.id.email);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }
}
