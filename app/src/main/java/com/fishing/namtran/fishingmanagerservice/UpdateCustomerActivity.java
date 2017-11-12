package com.fishing.namtran.fishingmanagerservice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import com.fishing.namtran.fishingmanagerservice.dbconnection.Customers;
import com.fishing.namtran.fishingmanagerservice.dbconnection.FishingManager;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Fishings;
import com.fishing.namtran.fishingmanagerservice.dbconnection.KeepFishing;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Settings;
import com.fishing.namtran.fishingmanagerservice.dbconnection.SettingsManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.round;

/**
 * A login screen that offers login via email/password.
 */
public class UpdateCustomerActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private CustomerActionTask mCustomerTask = null;

    // UI references.
    private AutoCompleteTextView mFullNameView;
    private EditText mMobileView;
    private EditText mDatInView;
    private EditText mDateOutView;
    private CheckBox mFeedTypeView;
    private EditText mKeepFishView;
    private EditText mTakeFishView;
    private EditText mTotalFishView;
    private EditText mTotalHoursView;
    private EditText mFeeDoFishView;
    private EditText mTotalMoneyView;
    private View mProgressView;
    private View mSubmitFormView;
    private String mFishingId;
    private int mTotalMoney;
    private String mDateIn;
    private String mDateOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer);

        mFishingId = getIntent().getStringExtra("fishingId");

        // Set up the login form.
        mMobileView = (EditText) findViewById(R.id.mobile);
        mFullNameView = (AutoCompleteTextView) findViewById(R.id.fullname);
        mDatInView = (EditText) findViewById(R.id.date_in);
        mDateOutView = (EditText) findViewById(R.id.date_out);
        mTotalHoursView = (EditText) findViewById(R.id.total_hours);
        mFeedTypeView = (CheckBox) findViewById(R.id.feed_type);
        mKeepFishView = (EditText) findViewById(R.id.keep_fish);
        mTakeFishView = (EditText) findViewById(R.id.take_fish);
        mTotalFishView = (EditText) findViewById(R.id.total_fish);
        mFeeDoFishView = (EditText) findViewById(R.id.fee_do_fish);
        mTotalMoneyView = (EditText) findViewById(R.id.total_money);
        mSubmitFormView = findViewById(R.id.add_new_customer_form);
        mProgressView = findViewById(R.id.add_new_customer_progress);

        String totalHours = null;
        int priceFishing = 0;
        int packagePrice = 0;
        int priceFeedType = 0;
        int feedTypeStatus = 0;
        int feedType = 0;
        double keepFish = 0.0;

        Cursor fishings = (new FishingManager(getApplicationContext())).getFishingEntriesById(mFishingId);
        Cursor settings = (new SettingsManager(getApplicationContext())).getSettingEntry("1");

        if (settings.moveToNext()) {
            priceFishing = settings.getInt(settings.getColumnIndexOrThrow(Settings.Properties.PRICE_FISHING));
            packagePrice = settings.getInt(settings.getColumnIndexOrThrow(Settings.Properties.PACKAGE_FISHING));
            priceFeedType = settings.getInt(settings.getColumnIndexOrThrow(Settings.Properties.PRICE_FEED_TYPE));
        }
        settings.close();

        if(fishings.moveToNext())
        {
            mFullNameView.setText(fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties.FULLNAME)));
            mMobileView.setText(fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties.MOBILE)));
            mDateIn = fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.DATE_IN));
            mDatInView.setText(mDateIn);
            feedTypeStatus = fishings.getInt(fishings.getColumnIndexOrThrow(Fishings.Properties.FEED_TYPE));
            //mFeedTypeView.setText(feedTypeStatus == 1 ? getString(R.string.yes) : getString(R.string.no));
            mFeedTypeView.setChecked(feedTypeStatus == 1 ? true : false);
            keepFish = fishings.getDouble(fishings.getColumnIndexOrThrow(KeepFishing.Properties.KEEP_FISH));
        }
        fishings.close();

        //
        double takeFish = Double.parseDouble(mTakeFishView.getText().toString());
        feedType = feedTypeStatus == 1 ? priceFeedType : 0;
        int feeDoFish = Integer.parseInt(mFeeDoFishView.getText().toString());

        mKeepFishView.setText(keepFish + "");
        mTakeFishView.setText(takeFish + "");
        mTotalFishView.setText((keepFish - takeFish) + "");
        //mTotalMoneyView.setText(mTotalMoney + "");

        //Events action
        Button mAddNewCustomerButton = (Button) findViewById(R.id.update_customer_button);
        mAddNewCustomerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSubmit();
            }
        });

        mDateOutView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction())
                {
                    GetTimeDateOutAndCalculateFeeFishing();
                }
                return false;
            }
        });

        mKeepFishView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });

        mTakeFishView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });

        mFeeDoFishView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mFeeDoFish = mFeeDoFishView.getText().toString();
                int feeDoFish = 0;

                if(!mFeeDoFish.equals(""))
                {
                    feeDoFish = Integer.parseInt(mFeeDoFishView.getText().toString());
                }
                mTotalMoney = Integer.parseInt(mTotalMoneyView.getText().toString());
                mTotalMoneyView.setText((feeDoFish + mTotalMoney) + "");
            }
        });
    }

    private boolean calculate()
    {
        String mKeepFish = mKeepFishView.getText().toString();
        String mTakeFish = mTakeFishView.getText().toString();
        double keepFish = 0.0;
        double takeFish = 0.0;

        if(mKeepFish.equals(""))
        {
            takeFish = Double.parseDouble(mTakeFish);
        }
        else if(mTakeFish.equals(""))
        {
            keepFish = Double.parseDouble(mKeepFish);
        }
        else {
            keepFish = Double.parseDouble(mKeepFish);
            takeFish = Double.parseDouble(mTakeFish);
        }
        mTotalFishView.setText((keepFish - takeFish) + "");
        return false;
    }

    public void GetTimeDateOutAndCalculateFeeFishing()
    {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        EditText totalHours = (EditText) mTotalHoursView;
                        EditText dateOut = (EditText) mDateOutView;
                        dateOut.setText(String.format("%02d:%02d", hourOfDay, minute));

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        DateFormat sqlDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        Date currentDate = new Date();
                        String dateFishing = sqlDateFormat.format(currentDate);

                        String fullDateOut = dateFishing + " " + String.format("%02d:%02d", hourOfDay, minute) + ":00";

                        try {
                            if(dateOut != null) {
                                long diff = (dateFormat.parse(fullDateOut).getTime() - dateFormat.parse(mDateIn).getTime());
                                long diffSeconds = diff / 1000 % 60;
                                long diffMinutes = diff / (60 * 1000) % 60;
                                long diffHours = diff / (60 * 60 * 1000);
                                totalHours.setText(String.format("%02d:%02d", diffHours, diffMinutes));

                                long totalFee = 0;
                                long point2Hours = 120000;
                                long point3Hours = 170000;
                                long point4Hours = 200000;
                                long extra30Mins = 20000;

                                if(diffHours < 3)
                                {
                                    if(diffHours < 2)
                                    {
                                        totalFee = point2Hours;
                                    }
                                    else
                                    if(diffHours == 2)
                                    {
                                        totalFee = point2Hours;
                                        if(diffMinutes >= 15)
                                        {
                                            totalFee = point3Hours;
                                        }
                                    }
                                }
                                else
                                if(diffHours >= 3 && diffHours < 4) // >3h
                                {
                                    if(diffMinutes >= 15)
                                    {
                                        totalFee = point4Hours;
                                    }
                                    else
                                    {
                                        totalFee = point3Hours;
                                    }
                                }
                                else // >4h
                                {
                                    long extraHours = diffHours - 4;
                                    totalFee = point4Hours + (extraHours*2)*extra30Mins; //30 mins are 20k
                                    if(diffMinutes >= 10) // > 10mins
                                    {
                                        if(diffMinutes <= 30) {
                                            totalFee += extra30Mins;
                                        }
                                        else {
                                            totalFee += 2*extra30Mins;
                                        }
                                    }
                                }
                                mTotalMoneyView.setText(totalFee + "");
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSubmit() {
        if (mCustomerTask != null) {
            return;
        }

        // Reset errors.
        mDateOutView.setError(null);

        // Store values at the time of the login attempt.
        String dateOut = mDateOutView.getText().toString();
        String fishingId = mFishingId;
        String totalFish = mTotalFishView.getText().toString();
        String keepFish = mKeepFishView.getText().toString();
        String takeFish = mTakeFishView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid mobile, if the user entered one.
        if (TextUtils.isEmpty(dateOut)) {
            mDateOutView.setError(getString(R.string.error_field_required));
            focusView = mDateOutView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mCustomerTask = new CustomerActionTask(fishingId, dateOut, totalFish);
            mCustomerTask.execute((Void) null);
        }
    }

    private boolean isMobileValid(String mobile) {
        //TODO: Replace this with your own logic
        return mobile.length() >= 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSubmitFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSubmitFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSubmitFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSubmitFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous action task
     */
    public class CustomerActionTask extends AsyncTask<Void, Void, Boolean> {

        private final String mFishingId;
        private final String mDateOut;
        private final String mTotalFish;

        CustomerActionTask(String fishingId, String dateOut, String totalFish) {
            mFishingId = fishingId;
            mDateOut = dateOut;
            mTotalFish = totalFish;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mCustomerTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mMobileView.setError(getString(R.string.error_incorrect_password));
                mMobileView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mCustomerTask = null;
            showProgress(false);
        }
    }
}

