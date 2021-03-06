package com.fishing.namtran.fishingmanagerservice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fishing.namtran.fishingmanagerservice.dbconnection.CustomerManager;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Customers;
import com.fishing.namtran.fishingmanagerservice.dbconnection.KeepFishing;
import com.fishing.namtran.fishingmanagerservice.dbconnection.KeepFishingManager;
import com.fishing.namtran.fishingmanagerservice.dbconnection.LogsKeepFishing;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private EditText mLogView;
    private View mProgressView;
    private View mSubmitFormView;
    private Cursor SearchCustomerResults;
    private double mTotalFish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_fish);

        // Set up the login form.
        mMobileView = (EditText) findViewById(R.id.mobile);
        mFullNameView = (AutoCompleteTextView) findViewById(R.id.fullname);
        mKeepFishView = (EditText) findViewById(R.id.keep_fish);
        mTakeFishView = (EditText) findViewById(R.id.take_fish);
        mTotalFishView = (EditText) findViewById(R.id.total_fish);
        mFeeDoFishView = (EditText) findViewById(R.id.fee_do_fish);
        mNoteView = (EditText) findViewById(R.id.note);
        mLogView = (EditText) findViewById(R.id.log);
        mSubmitFormView = findViewById(R.id.update_take_fish_form);
        mProgressView = findViewById(R.id.update_take_fish_progress);

        CustomerManager customerManager = new CustomerManager(getApplicationContext());
        SearchCustomerResults = customerManager.getSearchAllCustomers();

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

        searchCustomers(SearchCustomerResults);
    }

    private boolean calculate()
    {
        String mKeepFish = mKeepFishView.getText().toString();
        String mTakeFish = mTakeFishView.getText().toString();
        double keepFish = 0.0;
        double takeFish = 0.0;

        if(mKeepFish.equals(""))
        {
            if(!mTakeFish.equals(""))
                takeFish = Double.parseDouble(mTakeFish);
        }
        else if(mTakeFish.equals(""))
        {
            if(!mKeepFish.equals(""))
                keepFish = Double.parseDouble(mKeepFish);
        }
        else {
            keepFish = Double.parseDouble(mKeepFish);
            takeFish = Double.parseDouble(mTakeFish);
        }
        mTotalFishView.setText(BigDecimal.valueOf(keepFish).add(BigDecimal.valueOf(mTotalFish)).subtract(BigDecimal.valueOf(takeFish)) + "");
        return false;
    }

    private void searchCustomers(Cursor searchCustomers)
    {
        final ArrayAdapter<String> listAdapter;

        //Search text
        final ListView itemList = (ListView)findViewById(R.id.listView);
        String [] listViewAdapterContent;
        final Map<String, String> notes = new HashMap<String, String>();
        final Map<String, String> logs = new HashMap<String, String>();

        int i = 0;
        listViewAdapterContent = new String[searchCustomers.getCount()];
        while (searchCustomers.moveToNext())
        {
            listViewAdapterContent[i] = searchCustomers.getString(searchCustomers.getColumnIndexOrThrow(Customers.Properties.FULLNAME)) + " - " + searchCustomers.getString(searchCustomers.getColumnIndexOrThrow(Customers.Properties.MOBILE)) + " - "
                                        + searchCustomers.getString(searchCustomers.getColumnIndexOrThrow(KeepFishing.Properties.TOTAL_FISH));
            notes.put(searchCustomers.getString(searchCustomers.getColumnIndexOrThrow(Customers.Properties.MOBILE)), searchCustomers.getString(searchCustomers.getColumnIndexOrThrow(KeepFishing.Properties.NOTE)));
            logs.put(searchCustomers.getString(searchCustomers.getColumnIndexOrThrow(Customers.Properties.MOBILE)), searchCustomers.getString(searchCustomers.getColumnIndexOrThrow(Customers.Properties._ID)));
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
                mKeepFishView.setText("");
                mTakeFishView.setText("");
                mFullNameView.setText(fullname[0].trim());
                mMobileView.setText(fullname[1].trim());
                mTotalFish = Double.parseDouble(fullname[2].trim());
                mTotalFishView.setText(mTotalFish + "");
                mNoteView.setText(notes.get(fullname[1].trim()));

                //Get keep fishing detail
                Cursor logsCuror = (new KeepFishingManager(getApplicationContext())).getLogsKeepFishing(logs.get(fullname[1].trim()));
                String logsDetail = "";

                while (logsCuror.moveToNext())
                {
                    int status = logsCuror.getInt(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.STATUS));

                    if(status == 1) {
                        logsDetail = "\n*" + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.DATE_TIME)) + ": " + " - " + getString(R.string.buy_fish) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.BUY_FISH))
                                + " - " + getString(R.string.total_fish) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.TOTAL_FISH))
                                + " - " + getString(R.string.total_money) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.TOTAL_MONEY_BUY_FISH)) + "\n" + logsDetail;
                    } else {
                        logsDetail =  "\n*" + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.DATE_TIME)) + ": " + " - " + getString(R.string.keep_fish) + ": " +  logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.KEEP_FISH))
                                + " - " + getString(R.string.take_fish) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.TAKE_FISH)) + " - " + getString(R.string.total_fish) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.TOTAL_FISH))
                                + " - " + getString(R.string.fee_do_fish) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.FEE_DO_FISH)) + "\n" + logsDetail;
                    }
                }
                logsCuror.close();
                mLogView.setText(logsDetail);
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
        String log = mLogView.getText().toString();

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
            mKeepFish = keepFish.equals("") ? "0" : keepFish;
            mTakeFish = takeFish.equals("") ? "0" : takeFish;
            mTotalFish = totalFish.equals("") ? "0" : totalFish;
            mFeeDoFish = feeDoFish.equals("") ? "0" : feeDoFish;
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

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentDate = new Date();
            String dateTakeFish = dateFormat.format(currentDate);

            if (success) {
                finish();
                CustomerManager customerManager = new CustomerManager(getApplicationContext());
                String custId = customerManager.checkCustomerExisted(mMobile) + "";

                KeepFishingManager keepFishingManager = new KeepFishingManager(getApplicationContext());

                keepFishingManager.updateKeepFishingEntry(custId, "0", "0", mKeepFish, mTakeFish, mTotalFish, null, mNote);
                keepFishingManager.createLogsKeepFishingEntry(custId, mKeepFish, mTakeFish, mTotalFish, mFeeDoFish, "0", "0", "0", "", dateTakeFish);
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

