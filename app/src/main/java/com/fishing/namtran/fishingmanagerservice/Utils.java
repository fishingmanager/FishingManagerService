package com.fishing.namtran.fishingmanagerservice;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by nam.tran on 11/1/2017.
 */

public class Utils extends FragmentActivity {

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

    /*public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }*/

    public static void GetTime(Context context, final EditText etext)
    {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        etext.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
