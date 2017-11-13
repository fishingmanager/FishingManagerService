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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fishing.namtran.fishingmanagerservice.dbconnection.CustomerManager;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Customers;
import com.fishing.namtran.fishingmanagerservice.dbconnection.FishingManager;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Fishings;
import com.fishing.namtran.fishingmanagerservice.dbconnection.KeepFishing;
import com.fishing.namtran.fishingmanagerservice.dbconnection.KeepFishingManager;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Settings;
import com.fishing.namtran.fishingmanagerservice.dbconnection.SettingsManager;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.round;

/**
 * A login screen that offers login via email/password.
 */
public class TakeFishActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private CustomerActionTask mCustomerTask = null;

    // UI references.
    private AutoCompleteTextView mFullNameView;
    private EditText mMobileView;
    private EditText mKeepFishView;
    private EditText mTakeFishView;
    private EditText mTotalFishView;
    private EditText mFeeDoFishView;
    private EditText mNoteView;
    private View mProgressView;
    private View mSubmitFormView;
    private String mFishingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_fish);

        mFishingId = getIntent().getStringExtra("fishingId");

        // Set up the login form.
        mMobileView = (EditText) findViewById(R.id.mobile);
        mFullNameView = (AutoCompleteTextView) findViewById(R.id.fullname);
        mKeepFishView = (EditText) findViewById(R.id.keep_fish);
        mTakeFishView = (EditText) findViewById(R.id.take_fish);
        mTotalFishView = (EditText) findViewById(R.id.total_fish);
        mFeeDoFishView = (EditText) findViewById(R.id.fee_do_fish);
        mNoteView = (EditText) findViewById(R.id.note);
        mSubmitFormView = findViewById(R.id.update_take_fish_form);
        mProgressView = findViewById(R.id.update_take_fish_progress);

        double keepFish = 0.0;
        Cursor fishings = (new FishingManager(getApplicationContext())).getFishingEntriesById(mFishingId);

        if(fishings.moveToNext())
        {
            mFullNameView.setText(fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties.FULLNAME)));
            mMobileView.setText(fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties.MOBILE)));

            keepFish = fishings.getDouble(fishings.getColumnIndexOrThrow(KeepFishing.Properties.TOTAL_FISH));
            mNoteView.setText(fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.NOTE)));
        }
        fishings.close();

        //
        double takeFish = Double.parseDouble(mTakeFishView.getText().toString());

        mKeepFishView.setText(keepFish + "");
        mTotalFishView.setText((keepFish - takeFish) + "");

        //Events action
        Button mUpdateTakeFishButton = (Button) findViewById(R.id.update_take_fish_button);
        mUpdateTakeFishButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSubmit();
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
                String feeDoFishText = mFeeDoFishView.getText().toString();
                int feeDoFish = 0;

                if(!feeDoFishText.equals(""))
                {
                    feeDoFish = Integer.parseInt(mFeeDoFishView.getText().toString());
                }
                mFeeDoFishView.setText(feeDoFish + "");
            }
        });

        searchCustomers();
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
        mTotalFishView.setText(BigDecimal.valueOf(keepFish).subtract(BigDecimal.valueOf(takeFish)) + "");
        return false;
    }

    private void searchCustomers()
    {
        final ArrayAdapter<String> listAdapter;

        //Search text
        final ListView itemList = (ListView)findViewById(R.id.listView);
        String [] listViewAdapterContent;

        //Get customers from database
        CustomerManager customerManager = new CustomerManager(getApplicationContext());
        Cursor searchCustomers = customerManager.getSearchAllCustomers();

        int i = 0;
        listViewAdapterContent = new String[searchCustomers.getCount()];
        while (searchCustomers.moveToNext())
        {
            listViewAdapterContent[i] = searchCustomers.getString(searchCustomers.getColumnIndexOrThrow(Customers.Properties.FULLNAME)) + " - " + searchCustomers.getString(searchCustomers.getColumnIndexOrThrow(Customers.Properties.MOBILE));
            i++;
        }

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listViewAdapterContent);
        itemList.setTextFilterEnabled(true);
        itemList.setAdapter(listAdapter);
        itemList.setVisibility(View.GONE);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // make Toast when click
                //Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_LONG).show();
                String item = listAdapter.getItem(position);
                String[] fullname = item.split("-");
                mFullNameView.setText(fullname[0].trim());
                mMobileView.setText(fullname[1].trim());
                itemList.setVisibility(View.GONE);
            }
        });

        mFullNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                itemList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mFullNameView.getText().toString().equals("") || listAdapter.isEmpty()) {
                    itemList.setVisibility(View.GONE);
                    mMobileView.setText("");
                }
            }
        });
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
        mFullNameView.setError(null);

        // Store values at the time of the login attempt.
        String fullName = mFullNameView.getText().toString();
        String mobile = mMobileView.getText().toString();
        String totalFish = mTotalFishView.getText().toString();
        String keepFish = mKeepFishView.getText().toString();
        String takeFish = mTakeFishView.getText().toString();
        String feeDoFish = mFeeDoFishView.getText().toString();
        String note = mNoteView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid mobile, if the user entered one.
        if (TextUtils.isEmpty(fullName)) {
            mFullNameView.setError(getString(R.string.error_field_required));
            focusView = mFullNameView;
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
            mCustomerTask = new CustomerActionTask(mobile, keepFish, takeFish, totalFish, feeDoFish, note);
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

        private final String mMobile;
        private final String mKeepFish;
        private final String mTakeFish;
        private final String mTotalFish;
        private final String mFeeDoFish;
        private final String mNote;

        CustomerActionTask(String mobile, String keepFish, String takeFish, String totalFish, String feeDoFish, String note) {
            mMobile = mobile;
            mKeepFish = keepFish;
            mTakeFish = takeFish;
            mTotalFish = totalFish;
            mFeeDoFish = feeDoFish;
            mNote = note;
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
                CustomerManager customerManager = new CustomerManager(getApplicationContext());
                String custId = customerManager.checkCustomerExisted(mMobile) + "";

                KeepFishingManager keepFishingManager = new KeepFishingManager(getApplicationContext());

                keepFishingManager.updateKeepFishingEntry(custId, "0", "0", mKeepFish, mTakeFish, mTotalFish, mNote + " - " + getString(R.string.fee_do_fish) + " : " + mFeeDoFish);
                Utils.Redirect(getApplicationContext(), ManagerCustomerActivity.class);

            } else {
                Utils.Alert(getApplicationContext(), getString(R.string.action_error));
            }
        }

        @Override
        protected void onCancelled() {
            mCustomerTask = null;
            showProgress(false);
        }
    }
}

